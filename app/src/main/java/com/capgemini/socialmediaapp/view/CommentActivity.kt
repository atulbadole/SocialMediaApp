package com.capgemini.socialmediaapp.view


import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capgemini.socialmediaapp.R
import com.capgemini.socialmediaapp.model.comment.Comment
import com.capgemini.socialmediaapp.model.user.User
import com.capgemini.socialmediaapp.viewModel.comment.CommentViewModal
import com.capgemini.socialmediaapp.viewModel.user.UserViewModal
import java.time.LocalDateTime

class CommentActivity : AppCompatActivity() {

    private lateinit var commentEditText: EditText
    private lateinit var commentViewModel : CommentViewModal
    private lateinit var userViewModel : UserViewModal
    private lateinit var commentRecyclerView: RecyclerView
    private lateinit var sendButton: ImageView
    private lateinit var profilePhoto : ImageView
    private var currentUserId: Long = -1L
    private var postId = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
        commentRecyclerView = findViewById(R.id.commentRecyclerView)
        commentRecyclerView.layoutManager = LinearLayoutManager(this)
        commentEditText = findViewById(R.id.commentEditText)
        commentViewModel=ViewModelProvider(this).get(CommentViewModal::class.java)
        userViewModel=ViewModelProvider(this).get(UserViewModal::class.java)
        sendButton = findViewById(R.id.send_button)
        profilePhoto = findViewById(R.id.comment_add_profile_photo)

        currentUserId = intent.getLongExtra("currentUserId", -1L)
        postId = intent.getLongExtra("postId",-1L)

        userViewModel.allUsers.observe(this){ users ->
            Log.d("fromcommentactivity", "fetched users : ${users}")
            users?.let {
                val map = mutableMapOf<Long, User>()
                for( i in users){
                    map.put(i.userId, i)
                }
                commentViewModel.getComments(postId)
                val currentuser = map[currentUserId]!!
                try{
                    if(currentuser.profileImage.length>0){
                        Glide.with(this@CommentActivity).load(currentuser.profileImage).into(profilePhoto)
                    }
                }catch (e : Exception){
                    Log.d("fromcommentactivity","Error while setting user profile image : ${e.localizedMessage}")
                }
                commentViewModel.allCommentsData.observe(this){ commentList ->
                    Log.d("fromcommmentactivity", "fetched comments : ${commentList}")
                     commentList?.let {
                         var sortedComments = commentList.sortedWith({ comment1, comment2 ->
                             comment2.timestamp.compareTo(comment1.timestamp)
                         })
                        commentRecyclerView.adapter=CommentAdapter(
                            sortedComments,
                            map,
                            currentUserId,
                            this,
                            { updatedComment ->
                                commentViewModel.updateComment(updatedComment)
                            }
                        )
                    }
                }
            }
        }

        sendButton.setOnClickListener {
            val commentText = commentEditText.text.toString().trim()
            if (commentText.isNotEmpty()) {
                Log.d("fromcommentactivity", "data being sended : ${postId}, ${currentUserId}")
                commentViewModel.addComment(commentText, postId, currentUserId)
                commentEditText.text.clear()
            }else{
                showMessage(this, "Cannot add empty comment")
            }
        }

        commentViewModel.isAdded.observe(this){
            commentViewModel.getComments(postId)
        }
    }
}