package com.capgemini.socialmediaapp.viewModel.post

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.capgemini.socialmediaapp.model.Repository
import com.capgemini.socialmediaapp.model.post.Post
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class PostViewModal(application: Application) : AndroidViewModel(application) {

    val repo = Repository(application)
    val isAdded = MutableLiveData<Boolean>(false)
    val isUpdated = MutableLiveData<Boolean>(false)
    val allPosts = repo.getAllPosts()
    val postsOfAUser = MutableLiveData<List<Post>?>()

    fun addPost(userId : Long,
                imageArray : String,
                textContent : String = "") {
        viewModelScope.launch {
            try{
                repo.addPost(userId, imageArray, textContent)
                isAdded.postValue(true)
            }catch (e: Exception){
                Log.d("PostViewModal", "Error while adding post : ${e.localizedMessage}")
                isAdded.postValue(false)
            }
        }
    }

    fun updatePost(updatedPost: Post) {
        viewModelScope.launch {
            try{
                repo.updatePost(updatedPost)
                isUpdated.postValue(true)
            }catch (e: Exception){
                Log.d("PostViewModal", "Error while updating post of postId : ${updatedPost.postId}")
                isUpdated.postValue(false)
            }
        }
    }

    fun getPostsOfAUser(userId: Long){
        viewModelScope.launch {
            try{
                postsOfAUser.postValue(repo.getPostsOfAUser(userId))
            }catch (e: Exception){
                Log.d("PostViewModal", "Error while getting posts for userId : ${userId}")
                postsOfAUser.postValue(MutableLiveData<List<Post>?>(null).value)
            }
        }
    }

}