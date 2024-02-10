
package com.capgemini.socialmediaapp.view
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.capgemini.socialmediaapp.R
import com.capgemini.socialmediaapp.model.user.User
import com.capgemini.socialmediaapp.viewModel.user.UserViewModal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CreatePostActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private lateinit var imageView: ImageView
    private lateinit var postimage: ImageView
    lateinit var username : TextView
    lateinit var userViewModal : UserViewModal
    lateinit var postTextContent : EditText
    lateinit var curUser : MutableLiveData<User?>

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = result.data?.data
            postimage.setImageURI(imageUri)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        imageView = findViewById(R.id.c_selectImageView)
        postimage = findViewById(R.id.c_post_imageview)
        username = findViewById(R.id.c_post_username)
        postTextContent = findViewById(R.id.c_post_text_content)
        userViewModal = ViewModelProvider(this).get(UserViewModal::class.java)
        imageView.setOnClickListener {
            openGallery()
        }
        userViewModal.userData.observe(this){
//            Log.d("from_post_creation", "from post creation : ${it}")
            username.text = it?.name
            postTextContent.setText(it.toString())
//            Log.d("postcreation", "from observe ${it?.name}")
        }
        val pref = this.getSharedPreferences("test", MODE_PRIVATE)
        val  tempuserId = pref.getLong("loggedInUserID", -1L)
        Log.d("POSTCREATION", "${tempuserId}")
        if(tempuserId!=-1L ) userViewModal.getuserDetails(tempuserId!!)
    }
    private fun openGallery(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(galleryIntent)
    }

}
