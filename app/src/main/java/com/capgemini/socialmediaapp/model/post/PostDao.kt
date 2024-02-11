package com.capgemini.socialmediaapp.model.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.capgemini.socialmediaapp.model.post.Post

@Dao
interface PostDao {

    @Insert
    suspend fun addPost(newPost : Post)

    @Update
    suspend fun updatePost(updatedPost : Post)

    @Query("select * from post")
    fun getAllPost() : LiveData<List<Post>>

    @Query("select * from post, user where user.userId==post.userId and post.userId==:id")
    fun getPostsOfAUser(id : Long) : List<Post>

    @Query("select * from post where post.postId==:id")
    fun getPost(id : Long) : Post

}