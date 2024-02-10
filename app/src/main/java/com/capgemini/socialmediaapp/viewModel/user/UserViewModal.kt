package com.capgemini.socialmediaapp.viewModel.user

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capgemini.socialmediaapp.model.Repository
import com.capgemini.socialmediaapp.model.user.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.os.Bundle
import android.view.Display.Mode
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async

class UserViewModal(val ctx : Application) : AndroidViewModel(ctx) {
    val repo = Repository(ctx)
    val isAdded = MutableLiveData<Boolean>(false)
    val isUpdated = MutableLiveData<Boolean>(false)
    val userData = MutableLiveData<User?>(null)
    val currentUser = MutableLiveData<User?>(null)
    val allUsers = repo.getAllUsers()
    fun fetchCurrentUserDetails(con : Context) {
        val pref = con.applicationContext.getSharedPreferences("final", MODE_PRIVATE)
        Log.d("userviewmodal", "${pref.all}")
        val userId = pref.getLong("loggedInUserID", -1L)
        Log.d("userviewmodal", "${userId}")
        if(userId!=-1L){
            viewModelScope.launch(Dispatchers.Default) {
                currentUser.postValue(repo.getuserDetails(userId))
            }
        }
    }

    fun addUser(name : String,
                email : String,
                password : String,
                bio : String = ""
    ) {
        viewModelScope.launch {
            try{
                repo.addUser(name, email, password,bio)
                isAdded.postValue(true)
            }catch(e: Exception){
                Log.d("UserViewModal", "Error while adding user : ${e.localizedMessage}")
                isAdded.postValue(false)
            }
        }
    }

    fun updateUser(updatedUser: User) {
        viewModelScope.launch{
            try{
                repo.updateUser(updatedUser)
                isUpdated.postValue(true)
            }catch(e: Exception){
                Log.d("UserViewModal", "Error while updating user having userId : ${updatedUser.userId}, error message : ${e.localizedMessage}")
                isUpdated.postValue(false)
            }
        }
    }

    fun getuserDetails(userId: Long) {
        val data = viewModelScope.async {
             try{
                userData.postValue(repo.getuserDetails(userId))
            }catch(e: Exception){
                Log.d("UserVewModal", "Error while getting user details for userId : ${userId}, error message : ${e.localizedMessage}")
                 userData.postValue(null)
            }
        }
    }

    fun login(username : String, password: String, ctx: Context) {
        if(password==""){

        }else{
            val loginTask = CoroutineScope(Dispatchers.Default).launch {
                var data = repo.login(username, password)
                CoroutineScope(Dispatchers.Main).launch {
                    currentUser.postValue(data)
//                    data?.let{
//                        var pref = ctx.getSharedPreferences("socialmedia", MODE_PRIVATE)
//                        var editor = pref.edit()
//                        editor.putLong("userId",it.userId)
//                        editor.commit()
//                    }
                    Log.d("loginModal","${data}")
                }
            }
        }
    }

}