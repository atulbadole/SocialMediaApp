package com.capgemini.socialmediaapp.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.capgemini.socialmediaapp.R
import com.capgemini.socialmediaapp.viewModel.user.UserViewModal
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SignUpActivity : AppCompatActivity() {
    lateinit var BackButton: FloatingActionButton
    lateinit var SignIn: Button
    lateinit var LoginButton: TextView
    lateinit var SignupEmail: EditText
    lateinit var Username: EditText
    lateinit var SignupPassword: EditText

    lateinit var userViewModal : UserViewModal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        userViewModal = ViewModelProvider(this).get(UserViewModal::class.java)
        BackButton = findViewById(R.id.s_Backbutton)
        SignIn = findViewById(R.id.s_SignUp)
        LoginButton = findViewById(R.id.s_login)
        SignupEmail = findViewById(R.id.s_Email)
        Username = findViewById(R.id.SignupUsername)
        SignupPassword = findViewById(R.id.s_Password)

        BackButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        LoginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        SignIn.setOnClickListener {
            if (validateDetails(
                    SignupEmail.text.toString(),
                    SignupPassword.text.toString(),
                    Username.text.toString()
                )
            ) {
                userViewModal.addUser(Username.text.toString(),
                                        SignupEmail.text.toString(),
                                        SignupPassword.text.toString())
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                showToast("SignUp Successful")
            }else{
                showToast("Invalid SignUp Credentials")
            }

        }
    }

    fun isUsernameValid(username: String):
            Boolean {
        return username.length in 3..20
    }

    fun isEmailvalid(email: String): Boolean {
        val atIndex = email.indexOf('@')
        val dotIndex = email.lastIndexOf(".com")


        return atIndex > 0 && dotIndex > atIndex + 1 && dotIndex < email.length - 1

    }

    fun isPasswordValid(password: String): Boolean {
        return password.length >= 8
    }

    private fun validateDetails(email: String, password: String, username: String): Boolean {
        return isEmailvalid(email) && isPasswordValid(password) && isUsernameValid(username)
    }
}

