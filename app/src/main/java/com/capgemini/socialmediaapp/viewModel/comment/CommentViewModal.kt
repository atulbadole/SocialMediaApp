package com.capgemini.socialmediaapp.viewModel.comment

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capgemini.socialmediaapp.model.Repository
import com.capgemini.socialmediaapp.model.comment.Comment
import com.capgemini.socialmediaapp.model.post.Post

class CommentViewModal(application: Application) : AndroidViewModel(application) {

    val repo = Repository(application)

    fun addComment(message : String, postId : Long, userId: Long) : Boolean{
        try{
            repo.addComment(message, postId,userId)
            return true
        }catch (e: Exception){
            Log.d("CommentViewModal", "Error while adding comment : ${e.localizedMessage}")
            return false
        }
    }

    fun updateComment(updatedComment: Comment) : Boolean{
        try{
            repo.updateComment(updatedComment)
            return true
        }catch (e : Exception){
            Log.d("CommentViewModal", "Error while updating comment : ${e.localizedMessage}")
            return false
        }
    }

    fun getComments(id : Long) : LiveData<List<Comment>?> {
        return repo.getComments(id)
    }
}