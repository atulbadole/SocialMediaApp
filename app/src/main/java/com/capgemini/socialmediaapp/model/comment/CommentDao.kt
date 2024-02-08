package com.capgemini.socialmediaapp.model.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.capgemini.socialmediaapp.model.comment.Comment
import com.capgemini.socialmediaapp.model.post.Post

@Dao
interface CommentDao {
    @Insert
    suspend fun addComment(newComment: Comment)

    @Update
    suspend fun updateComment(updatedCommet : Comment)

    @Query("select * from comment where postId=:id")
    fun getComments(id : Long) : LiveData<List<Comment>?>

}