
package com.capgemini.socialmediaapp.view
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
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
        userViewModal = ViewModelProvider(this).get(UserViewModal::class.java)
        curUser = userViewModal.currentUser
        imageView = findViewById(R.id.c_selectImageView)
        postimage = findViewById(R.id.c_post_imageview)
        username = findViewById(R.id.c_post_username)
        curUser.observe(this){
            username.text = it?.name
            Log.d("createActivity", "from observe ${it?.name}")
        }
        var pref = getSharedPreferences("socialmedia", MODE_PRIVATE)
        var userId = pref.getLong("username",0L)
        CoroutineScope(Dispatchers.Default).launch {
            var userData = userViewModal.getuserDetails(userId)
            CoroutineScope(Dispatchers.Main).launch {
                curUser.postValue(userData.value)
            }
        }
        Log.d("createActivity", "${curUser.value}")
        imageView.setOnClickListener {
            openGallery()
        }
    }
    private fun openGallery(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(galleryIntent)
    }

}
