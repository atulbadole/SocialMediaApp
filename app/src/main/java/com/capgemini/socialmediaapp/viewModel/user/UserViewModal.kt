package com.capgemini.socialmediaapp.viewModel.user

import android.app.Application
import android.content.Context
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
class UserViewModal(val ctx : Application) : AndroidViewModel(ctx) {
    val repo = Repository(ctx)

    val currentUser = MutableLiveData<User?>(null)

    fun addUser(name : String,
                email : String,
                password : String,
                bio : String = "",
                profileImage : ByteArray = byteArrayOf()
    ) : Boolean{
        try{
            repo.addUser(name, email, password,bio, profileImage)
            return true
        }catch(e: Exception){
            Log.d("UserViewModal", "Error while adding user : ${e.localizedMessage}")
            return false
        }
    }

    fun updateUser(updatedUser: User) : Boolean{
        try{
            repo.updateUser(updatedUser)
            return true
        }catch(e: Exception){
            Log.d("UserViewModal", "Error while updating user having userId : ${updatedUser.userId}, error message : ${e.localizedMessage}")
            return false
        }
    }

    fun getuserDetails(userId: Long) : LiveData<User?> {
        try{
            return repo.getuserDetails(userId)
        }catch(e: Exception){
            Log.d("UserVewModal", "Error while getting user details for userId : ${userId}, error message : ${e.localizedMessage}")
            return MutableLiveData<User?>(null)
        }
    }

    fun login(username : String, password: String) {
        if(password==""){

        }else{
            val loginTask = CoroutineScope(Dispatchers.Default).launch {
                var data = repo.login(username, password)
                CoroutineScope(Dispatchers.Main).launch {
                    currentUser.value = data
                }
            }
        }
    }

}