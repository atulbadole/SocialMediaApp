package com.capgemini.socialmediaapp.view


import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capgemini.socialmediaapp.R
import com.capgemini.socialmediaapp.model.comment.Comment
import com.capgemini.socialmediaapp.model.user.User
import com.capgemini.socialmediaapp.viewModel.comment.CommentViewModal
import com.capgemini.socialmediaapp.viewModel.user.UserViewModal
import java.time.LocalDateTime

class CommentActivity : AppCompatActivity() {

    private lateinit var adapter: CommentAdapter
    private lateinit var commentEditText: EditText
    private lateinit var commentViewModel : CommentViewModal
    private lateinit var userViewModel : UserViewModal
    private lateinit var commentRecyclerView: RecyclerView
    private lateinit var sendButton: ImageView
    private val comments = mutableListOf<Comment>()
    private val currentUserId: Long = 123456789 // Replace with the actual user ID
    private  var lastCommentTime: LocalDateTime?=null
    private  var editedCommentPosition:Int?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
        commentRecyclerView = findViewById(R.id.commentRecyclerView)
        commentEditText = findViewById(R.id.commentEditText)
        commentViewModel=ViewModelProvider(this).get(CommentViewModal::class.java)
        userViewModel=ViewModelProvider(this).get(UserViewModal::class.java)
        sendButton = findViewById(R.id.send_button)
        userViewModel.allUsers.observe(this){ users ->
            users?.let {
                val map = mutableMapOf<Long, User>()
                for( i in users){
                    map.put(i.userId, i)
                }

                commentViewModel.allCommentsData.observe(this){ commentList ->
                     commentList?.let {
                        commentRecyclerView.adapter=CommentAdapter(
                            commentList,map,currentUserId, this,{ commentObject,edited ->
                                if(edited){
                                    commentViewModel.updateComment(commentObject)
                                }
                                else{
                                    commentViewModel.addComment(commentObject)
                                }

                            }
                        )


                    }
                }
            }
        }


        commentRecyclerView.adapter = adapter
        commentRecyclerView.layoutManager = LinearLayoutManager(this)
        sendButton.setOnClickListener {
            val commentText = commentEditText.text.toString().trim()
            if (commentText.isNotEmpty()) {
                val currentTime = LocalDateTime.now()
                lastCommentTime = currentTime
                if (editedCommentPosition != null) {
                    var editedComment = comments[editedCommentPosition!!]
                    editedComment.commentText = commentText
                    editedComment.time = currentTime
                    adapter.notifyItemChanged(editedCommentPosition!!)
                    editedCommentPosition = null
                }
                else{

                }
                commentEditText.text.clear()
                commentRecyclerView.scrollToPosition(0)
            }
        }
    }
}