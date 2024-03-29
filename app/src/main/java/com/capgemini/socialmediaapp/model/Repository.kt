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

    fun getAllPosts() : LiveData<List<Post>> {
        return postDao.getAllPost()
    }

    suspend fun addPost(userId : Long,
                imageArray : String,
                textContent : String = ""){
        postDao.addPost(Post(userId, LocalDateTime.now(), imageArray, textContent, listOf<Long>()))
    }

    suspend fun updatePost(updatedPost: Post){
        postDao.updatePost(updatedPost)
    }

    fun getPostsOfAUser(userId: Long) : List<Post>? {
        return postDao.getPostsOfAUser(userId)
    }

    fun getPost(id : Long) = postDao.getPost(id)

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

    fun getAllUsers() = userDao.getAllUsers()

//    ---------------------------------- User Section Ends Here ----------------------------------








//    --------------------------------- Comment Section -------------------------------------------

    val commentDao = AppDatabase.getInstance(ctx).commentDao()

    suspend fun addComment(message : String, postId : Long, userId: Long){
        commentDao.addComment(Comment( message, postId,userId, LocalDateTime.now()))
    }

    suspend fun updateComment(updatedComment: Comment){
        commentDao.updateComment(updatedComment)
    }

    fun getComments(id : Long) = commentDao.getComments(id)

//    -------------------------------- Comment Section Ends here ---------------------------------
}