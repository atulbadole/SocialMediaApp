package com.capgemini.socialmediaapp.view


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.capgemini.socialmediaapp.R

class LoginActivity : AppCompatActivity() {
    lateinit var  SignUpButton : TextView
    lateinit var SignInButton : Button
    lateinit var LoginEmail : EditText
    lateinit var LoginPassword : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        SignUpButton = findViewById(R.id.l_Signup)
        SignInButton = findViewById(R.id.l_Signin)
        LoginEmail = findViewById(R.id.l_Email)
        LoginPassword = findViewById(R.id.l_Password)

        SignUpButton.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)

        }
        SignInButton.setOnClickListener{
            if(validateDetails(LoginEmail.text.toString(), LoginPassword.text.toString())){
                val intent = Intent(this,FeedActivity::class.java)
                startActivity(intent)
                finish()
                Toast.makeText(this,"Login Successful",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "Invalid Login Credentials", Toast.LENGTH_LONG).show()
            }
        }


    }

    fun isEmailvalid(email: String): Boolean{
        val atIndex = email.indexOf('@')
        val dotIndex = email.lastIndexOf(".com")


        return atIndex > 0 && dotIndex > atIndex + 1 && dotIndex < email.length -1

    }
    fun isPasswordValid(password: String): Boolean{
        return password.length >= 8
    }
    private fun validateDetails(email : String, password: String): Boolean {
        return isEmailvalid(email) && isPasswordValid(password)
    }
}