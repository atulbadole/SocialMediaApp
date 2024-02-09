package com.capgemini.socialmediaapp.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.capgemini.socialmediaapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SignUpActivity : AppCompatActivity() {
    lateinit var BackButton : FloatingActionButton
    lateinit var SignIn : Button
    lateinit var LoginButton : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        BackButton = findViewById(R.id.s_Backbutton)
        SignIn = findViewById(R.id.s_SignUp)
        LoginButton = findViewById(R.id.s_login)

        BackButton.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        SignIn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        LoginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}