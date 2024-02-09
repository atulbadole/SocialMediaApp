package com.capgemini.socialmediaapp.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capgemini.socialmediaapp.model.comment.Comment
import com.capgemini.socialmediaapp.model.post.Post
import com.capgemini.socialmediaapp.model.user.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class Repository(val ctx : Context) {

//----------------------------------- Post Section --------------------------------------------

    val postDao = AppDatabase.getInstance(ctx).postDao()

    suspend fun getAllPosts() : LiveData<List<Post>?> {
        return postDao.getAllPost()
    }

    suspend fun addPost(userId : Long,
                imageArray : ByteArray = byteArrayOf(),
                textContent : String = ""){
        postDao.addPost(Post(0,userId, LocalDateTime.now(), imageArray, textContent, listOf<Long>()))
    }

    suspend fun updatePost(updatedPost: Post){
        postDao.updatePost(updatedPost)
    }

    suspend fun getPostsOfAUser(userId: Long) : LiveData<List<Post>?> {
        return postDao.getPostsOfAUser(userId)
    }

//    ----------------------------------- Post Section Ends here ---------------------------------







//    ----------------------------------- User Section -------------------------------------------

    val userDao = AppDatabase.getInstance(ctx).userDao()

    suspend fun addUser(name : String,
                email : String,
                password : String,
                bio : String = "",
                profileImage : String = ""
    ){
        userDao.addUser(User(name, email, password, bio, profileImage))
    }

    suspend fun updateUser(updatedUser: User){
        userDao.updateUser(updatedUser)
    }

    suspend fun getuserDetails(userId: Long)  = userDao.getuserDetails(userId)

    suspend fun login(email : String, password: String)  = userDao.login(email, password)

//    ---------------------------------- User Section Ends Here ----------------------------------








//    --------------------------------- Comment Section -------------------------------------------

    val commentDao = AppDatabase.getInstance(ctx).commentDao()

    suspend fun addComment(message : String, postId : Long, userId: Long){
        commentDao.addComment(Comment(0, message, postId,userId, LocalDateTime.now()))
    }

    suspend fun updateComment(updatedComment: Comment){
        commentDao.updateComment(updatedComment)
    }

    fun getComments(id : Long) : LiveData<List<Comment>?> {
        return commentDao.getComments(id)
    }

//    -------------------------------- Comment Section Ends here ---------------------------------
}