package com.capgemini.socialmediaapp.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ContentInfoCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capgemini.socialmediaapp.R
import com.capgemini.socialmediaapp.model.post.Post
import com.capgemini.socialmediaapp.model.user.User
import com.capgemini.socialmediaapp.viewModel.post.PostViewModal
import com.capgemini.socialmediaapp.viewModel.user.UserViewModal
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProfilePageActivity : AppCompatActivity() {
    lateinit var backButton: FloatingActionButton
    lateinit var profilepic : ImageView
    lateinit var userBio : EditText
    lateinit var userName : EditText
    lateinit var userViewModal: UserViewModal
    lateinit var postViewModal: PostViewModal
    lateinit var recyclerView : RecyclerView
    var userId  = -1L
    var currentUserId = -1L
    lateinit var updateduserData : User
    val updatedPostList = mutableListOf<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)
        supportActionBar?.hide()
        profilepic = findViewById(R.id.profilepic)
        userBio = findViewById(R.id.profile_page_bio)
        userName = findViewById(R.id.profile_page_user_name)
        recyclerView = findViewById(R.id.profile_page_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)

        backButton = findViewById(R.id.backB)
        backButton.setOnClickListener {
            finish()
        }

        userViewModal = ViewModelProvider(this).get(UserViewModal::class.java)
        postViewModal = ViewModelProvider(this).get(PostViewModal::class.java)

        userId = intent.getLongExtra("userId", -1L)
        currentUserId = intent.getLongExtra("currentUserId", -1L)

        userViewModal.userData.observe(this){ user ->
            user?.let {
                postViewModal.getPostsOfAUser(userId)

                postViewModal.postsOfAUser.observe(this){ posts ->
                    posts?.let{
                        recyclerView.adapter = FeedViewAdapter(
                            posts,
                            mutableMapOf(userId to user),
                            currentUserId,
                            { updatedPost ->
                                updatedPostList.add(updatedPost)
                            },
                            ::openPostDetailPage,
                            ::openCommentPage,
                            ::openProfilePage
                        )
                    }
                }

                updateduserData = user
                userName.setText(user.name)
                userBio.setText(user.bio)
                if(user.profileImage.length>0){
                    Glide.with(this).load(user.profileImage).into(profilepic)
                }
                if(user.userId!=currentUserId){
                    disableEditTexts()
                }else{
                    profilepic.setOnClickListener {
                        openGallery()
                    }
                }
            }
        }

        userViewModal.getuserDetails(userId)
    }

    private fun disableEditTexts() {
        for(view in listOf(userName, userBio)){
            view.isClickable = false
            view.isFocusable = false
        }
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let{
                val imageUri = it
                profilepic.setImageURI(imageUri)
                updateduserData.profileImage = getImagePath(imageUri, contentResolver )
            }
        }
    }

    private fun openGallery(){
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(galleryIntent)
    }

    fun logoutClick(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        userViewModal.logout()
        startActivity(intent)
    }

    override fun onStop() {
        super.onStop()
        updateduserData.name = userName.text.toString()
        updateduserData.bio = userBio.text.toString()
        userViewModal.updateUser(updateduserData)
        for(post in updatedPostList){
            postViewModal.updatePost(post)
        }
    }

    fun openPostDetailPage(post : Post, editClicked : Boolean){
        val intent = Intent(this@ProfilePageActivity, PostDetailActivity::class.java)
        intent.putExtra("postId", post.postId)
        intent.putExtra("editClicked", editClicked)
        intent.putExtra("currentUserId", currentUserId)
        intent.putExtra("userIdOfCurrentPost", post.userId)
        startActivity(intent)
    }

    private fun openCommentPage(postId: Long) {
        var intent = Intent(this, CommentActivity::class.java)
        intent.putExtra("postId", postId)
        intent.putExtra("currentUserId", currentUserId)
        startActivity(intent)
    }

    fun openProfilePage(userId : Long){
        val i = Intent(this, ProfilePageActivity::class.java)
        i.putExtra("userId", userId)
        i.putExtra("currentUserId", currentUserId)
        startActivity(i)
    }
}