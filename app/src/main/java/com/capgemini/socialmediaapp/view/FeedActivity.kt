package com.capgemini.socialmediaapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.capgemini.socialmediaapp.R

class FeedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
    }

    fun openCreatePostActivity(view: View) {
        val intent = Intent(this, CreatePostActivity::class.java)
        startActivity(intent)
    }
}