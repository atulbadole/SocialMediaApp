package com.capgemini.socialmediaapp.view


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.capgemini.socialmediaapp.R

class LoginActivity : AppCompatActivity() {
    lateinit var  SignUpButton : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        SignUpButton = findViewById(R.id.l_Signup)

        SignUpButton.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)

        }

    }
}