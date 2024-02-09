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
    val allPosts = MutableLiveData<List<Post>?>()

    fun addPost(userId : Long,
                imageArray : ByteArray = byteArrayOf(),
                textContent : String = "") {
        viewModelScope.launch {
            try{
                repo.addPost(userId, imageArray, textContent)
            }catch (e: Exception){
                Log.d("PostViewModal", "Error while adding post : ${e.localizedMessage}")
            }
        }
    }

    fun updatePost(updatedPost: Post) : Boolean{
        return try{
            repo.updatePost(updatedPost)
            true
        }catch (e: Exception){
            Log.d("PostViewModal", "Error while updating post of postId : ${updatedPost.postId}")
            false
        }
    }

    fun getPostsOfAUser(userId: Long) : LiveData<List<Post>?> {
        return try{
            repo.getPostsOfAUser(userId)
        }catch (e: Exception){
            Log.d("PostViewModal", "Error while getting posts for userId : ${userId}")
            MutableLiveData<List<Post>?>(null)
        }
    }

    fun getAllPosts() : LiveData<List<Post>?> {
        val posts = repo.getAllPosts()
        posts.value?.sortedByDescending {
            it.timestamp
        }
        return posts
    }
}