package com.capgemini.socialmediaapp.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.capgemini.socialmediaapp.R
import com.capgemini.socialmediaapp.model.post.Post
import com.capgemini.socialmediaapp.viewModel.post.PostViewModal
import com.capgemini.socialmediaapp.viewModel.user.UserViewModal

class PostDetailActivity : AppCompatActivity() {

    lateinit var username : TextView
    lateinit var userProfileImage : ImageView
    lateinit var postText : EditText
    lateinit var postImage : ImageView
    lateinit var postTime : TextView
    lateinit var postEditButton : CardView
    lateinit var postLikeButton : LinearLayout
    lateinit var postCommentButton : LinearLayout
    lateinit var userViewModel : UserViewModal
    lateinit var postViewModal: PostViewModal
    lateinit var postLikeBtnImage : ImageView
    lateinit var imageEditingOptionsContainer : LinearLayout
    lateinit var addImageButton : LinearLayout
    lateinit var clearImageButton : LinearLayout

    var currentUserEdit = -1L
    var currentPostId = -1L
    var editBtnClickedInFeedActivity = false
    var updatedPost : Post? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        userViewModel = ViewModelProvider(this).get(UserViewModal::class.java)
        postViewModal = ViewModelProvider(this).get(PostViewModal::class.java)

        username = findViewById(R.id.post_detail_username)
        userProfileImage = findViewById(R.id.post_detail_user_profile_image)
        postText = findViewById(R.id.post_detail_text_content)
        postImage = findViewById(R.id.post_detail_content_image)
        postLikeButton = findViewById(R.id.post_detail_like_btn)
        postCommentButton = findViewById(R.id.post_detail_comment_btn)
        postTime = findViewById(R.id.post_detail_time)
        postEditButton = findViewById(R.id.post_detail_edit_btn)
        postLikeBtnImage = findViewById(R.id.post_detail_like_image)
        imageEditingOptionsContainer = findViewById(R.id.image_editing_options)
        addImageButton = findViewById(R.id.post_detail_add_image)
        clearImageButton = findViewById(R.id.post_detail_clear_image)

        addImageButton.setOnClickListener {
            openGallery()
        }

        clearImageButton.setOnClickListener {
            updatedPost!!.imageArray = ""
            postImage.setImageDrawable(null)
        }

        val postId = intent.getLongExtra("postId", -1L)
        var editBtnClicked = intent.getBooleanExtra("editClicked", false)
        val currentUserId = intent.getLongExtra("currentUserId", -1L)
        var userIdOfCurrentPost = intent.getLongExtra("userIdOfCurrentPost", -1L)

        postViewModal.postData.observe(this){ post ->
            Log.d("frompostdetailactivity", "for post : ${post}")
            post?.let{
                userViewModel.getuserDetails(post.userId)
                userViewModel.userData.observe(this@PostDetailActivity){ user ->
                    Log.d("frompostdetailactivity", "for user : ${user}")
                    user?.let {
                        updatedPost = post
                        username.text = user.name
                        postText.setText(post.textContent)
                        if(user.profileImage.length>0){
                            Glide.with(this@PostDetailActivity).load(user.profileImage).into(userProfileImage)
                        }
                        postTime.text = getTimePassedString(post.timestamp)
                        if(post.imageArray.length>0){
                            Glide.with(this@PostDetailActivity).load(post.imageArray).into(postImage)
                        }
                        if(post.userId==currentUserId){
                            postLikeButton.setOnClickListener {
                                showMessage(this@PostDetailActivity, "Cannot like your own post.")
                            }
                            postLikeBtnImage.setImageResource(R.drawable.white_heart)
                            postEditButton.setOnClickListener {
                                editBtnClicked = !editBtnClicked
                                toggleEditable(editBtnClicked)
                            }
                            toggleEditable(editBtnClicked)
                        }else{
                            postEditButton.isVisible = false
                            postEditButton.isClickable = false
                            postLikeButton.setOnClickListener {
                                var likeList = updatedPost!!.likes.toMutableList()
                                if(updatedPost!!.likes.contains(currentUserId)){
                                    postLikeBtnImage.setImageResource(R.drawable.white_heart)
                                    likeList.remove(currentUserId)
                                }else{
                                    postLikeBtnImage.setImageResource(R.drawable.red_heart)
                                    likeList.add(currentUserId)
                                }
                                updatedPost!!.likes = likeList
                            }
                            if(post.likes.contains(currentUserId)){
                                postLikeBtnImage.setImageResource(R.drawable.red_heart)
                            }else{
                                postLikeBtnImage.setImageResource(R.drawable.white_heart)
                            }
                        }
                    }
                }
            }
        }
        Log.d("frompotdetail", "fetching data for post : ${postId}")
        postViewModal.getPost(postId)
    }

    private fun openGallery(){
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(galleryIntent)
    }

    val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let{
                val imageUri = it
                postImage.setImageURI(imageUri)
                updatedPost!!.imageArray = getImagePath(imageUri)
            }
        }
    }

    private fun getImagePath(imageUri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(imageUri, projection, null, null)
        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            return it.getString(columnIndex)
        }
        return ""
    }

    fun toggleEditable(editable: Boolean){
        Log.d("fromdetailactivty", "toggelEditable : ${editable}")
        postText.isFocusable = editable
        postText.isClickable = editable
        imageEditingOptionsContainer.isClickable = editable
        imageEditingOptionsContainer.isVisible = editable
        if(editable){
            (postEditButton.getChildAt(0) as TextView).text = "Save"
            postText.isFocusable = true
            postText.isClickable = true
            postText.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE or InputType.TYPE_CLASS_TEXT
        }else{
            (postEditButton.getChildAt(0) as TextView).text = "Edit"
            updatedPost!!.textContent = postText.text.toString()
        }
    }

    override fun onStop() {
        super.onStop()
        updatedPost?.let {
            postViewModal.updatePost(it)
        }
    }
}