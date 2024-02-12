
package com.capgemini.socialmediaapp.view
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.capgemini.socialmediaapp.R
import com.capgemini.socialmediaapp.viewModel.post.PostViewModal
import com.capgemini.socialmediaapp.viewModel.user.UserViewModal


class CreatePostActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private lateinit var imageView: LinearLayout
    private lateinit var clearImageView : LinearLayout
    private lateinit var postimage: ImageView
    private lateinit var userProfileImage : ImageView
    lateinit var username : TextView
    lateinit var userViewModal : UserViewModal
    lateinit var postViewModal: PostViewModal
    lateinit var postTextContent : EditText
    lateinit var postCreateButton : Button
    var postButtonClicked = false
    var postImagePath = ""



    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let{
                val imageUri = it
                postimage.setImageURI(imageUri)
                postImagePath = getImagePath(imageUri, contentResolver)
                Log.d("localimagepath", "${postImagePath}")
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        imageView = findViewById(R.id.post_detail_add_image)
        postimage = findViewById(R.id.c_post_imageview)
        username = findViewById(R.id.c_post_username)
        postTextContent = findViewById(R.id.c_post_text_content)
        postCreateButton = findViewById(R.id.c_postButton)
        clearImageView = findViewById(R.id.post_detail_clear_image)
        userProfileImage = findViewById(R.id.c_user_profilepicture)
        userViewModal = ViewModelProvider(this).get(UserViewModal::class.java)
        postViewModal = ViewModelProvider(this).get(PostViewModal::class.java)
        imageView.setOnClickListener {
            openGallery()
        }
        clearImageView.setOnClickListener {
            postimage.setImageDrawable(null)
            postImagePath = ""
        }
        userViewModal.currentUser.observe(this){user ->
            user?.let {
                username.text = user?.name
                try{
                    Glide.with(this).load(user.profileImage).into(userProfileImage)
                }catch (e: Exception){
                    Log.d("fromcreatepostActivity", "Error while setting profile image : ${e.localizedMessage}")
                }
            }
        }
        userViewModal.fetchCurrentUserDetails(this)

        postViewModal.isAdded.observe(this){
            if(postButtonClicked){
                postButtonClicked = false
                if(it){
                    Toast.makeText(this, "Post created successfully", Toast.LENGTH_LONG).show()
                    finish()
                }else{
                    Toast.makeText(this, "Error while creating post, Please try again later", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun openGallery(){
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(galleryIntent)
    }

    fun createPost(view: View) {
        postViewModal.addPost(userViewModal.currentUser.value!!.userId,postImagePath, postTextContent.text.toString())
        postButtonClicked = true
    }
}
