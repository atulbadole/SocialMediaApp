package com.capgemini.socialmediaapp.viewModel.comment

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.capgemini.socialmediaapp.model.Repository
import com.capgemini.socialmediaapp.model.comment.Comment
import com.capgemini.socialmediaapp.model.post.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CommentViewModal(application: Application) : AndroidViewModel(application) {

    val repo = Repository(application)
    val isAdded = MutableLiveData<Boolean>(false)
    val isUpdated = MutableLiveData<Boolean>(false)
    var allCommentsData = MutableLiveData<List<Comment>>()

    fun addComment(message : String, postId : Long, userId: Long) {
        viewModelScope.launch(Dispatchers.Default) {
            try{
                repo.addComment(message, postId,userId)
                isAdded.postValue(true)
            }catch (e: Exception){
                Log.d("CommentViewModal", "Error while adding comment : ${e.localizedMessage}")
                isAdded.postValue(false)
            }
        }
    }

    fun updateComment(updatedComment: Comment) {
        viewModelScope.launch(Dispatchers.Default) {
            try{
                repo.updateComment(updatedComment)
                isUpdated.postValue(true)
            }catch (e : Exception){
                Log.d("CommentViewModal", "Error while updating comment : ${e.localizedMessage}")
                isUpdated.postValue(false)
            }
        }
    }

    fun getComments(id : Long){
        //allCommentsData.value = repo.getComments(id)
       // allCommentsData.postValue(repo.getComments(id).value)
        viewModelScope.launch(Dispatchers.Default) {
            try{
                val data = repo.getComments(id)
                allCommentsData.postValue(data)
            }catch (e: Exception){
                Log.d("fromcommentviewmodel", "Error while fetching comments of postId : ${id}")
                allCommentsData.postValue(listOf())
            }
        }
    }
}