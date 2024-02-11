package com.capgemini.socialmediaapp.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.capgemini.socialmediaapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProfilePageActivity : AppCompatActivity() {
    lateinit var backButton: FloatingActionButton
    lateinit var profilepicI : ImageView
    private val REQUEST_IMAGE_SELECT =1
    lateinit var editpicture: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        profilepicI = findViewById(R.id.profilepic)
        editpicture=findViewById(R.id.editPicB)


        backButton = findViewById(R.id.backB)
        backButton.setOnClickListener {
            finish()
        }

        editpicture.setOnClickListener {
            getContent.launch("image/*")
        }
    }

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            profilepicI.setImageURI(it)
        }
    }

    fun logoutClick(view: View) {
//        val i = Intent(this, loginActivity::class.java)
//        startActivity(i)
    }


}