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

    fun getAllPosts() : LiveData<List<Post>?> {
        return postDao.getAllPost()
    }

    fun addPost(userId : Long,
                imageArray : ByteArray = byteArrayOf(),
                textContent : String = ""){
        CoroutineScope(Dispatchers.Default).launch {
            postDao.addPost(Post(0,userId, LocalDateTime.now(), imageArray, textContent, listOf<Long>()))
        }
    }

    fun updatePost(updatedPost: Post){
        CoroutineScope(Dispatchers.Default).launch {
            postDao.updatePost(updatedPost)
        }
    }

    fun getPostsOfAUser(userId: Long) : LiveData<List<Post>?> {
        return postDao.getPostsOfAUser(userId)
    }

//    ----------------------------------- Post Section Ends here ---------------------------------







//    ----------------------------------- User Section -------------------------------------------

    val userDao = AppDatabase.getInstance(ctx).userDao()

    fun addUser(name : String,
                email : String,
                password : String,
                bio : String = "",
                profileImage : ByteArray = byteArrayOf()
    ){
        CoroutineScope(Dispatchers.Default).launch {
            userDao.addUser(User(9, name, email, password, bio, profileImage))
        }
    }

    fun updateUser(updatedUser: User){
        CoroutineScope(Dispatchers.Default).launch {
            userDao.updateUser(updatedUser)
        }
    }

    fun getuserDetails(userId: Long)  = userDao.getuserDetails(userId)

    fun login(email : String, password: String)  = userDao.login(email, password)

//    ---------------------------------- User Section Ends Here ----------------------------------








//    --------------------------------- Comment Section -------------------------------------------

    val commentDao = AppDatabase.getInstance(ctx).commentDao()

    fun addComment(message : String, postId : Long, userId: Long){
        CoroutineScope(Dispatchers.Default).launch {
            commentDao.addComment(Comment(0, message, postId,userId, LocalDateTime.now()))
        }
    }

    fun updateComment(updatedComment: Comment){
        CoroutineScope(Dispatchers.Default).launch {
            commentDao.updateComment(updatedComment)
        }
    }

    fun getComments(id : Long) : LiveData<List<Comment>?> {
        return commentDao.getComments(id)
    }

//    -------------------------------- Comment Section Ends here ---------------------------------
}