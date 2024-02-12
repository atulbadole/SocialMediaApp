package com.capgemini.socialmediaapp.view


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.capgemini.socialmediaapp.R
import com.capgemini.socialmediaapp.model.user.User
import com.capgemini.socialmediaapp.viewModel.user.UserViewModal

class LoginActivity : AppCompatActivity() {
    lateinit var  SignUpButton : TextView
    lateinit var SignInButton : Button
    lateinit var LoginEmail : EditText
    lateinit var LoginPassword : EditText
    lateinit var currentUser : MutableLiveData<User?>
    lateinit var userViewModal : UserViewModal
    var loginBtnClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        userViewModal = ViewModelProvider(this).get(UserViewModal::class.java)
        currentUser = userViewModal.currentUser
        SignUpButton = findViewById(R.id.l_Signup)
        SignInButton = findViewById(R.id.l_Signin)
        LoginEmail = findViewById(R.id.l_Email)
        LoginPassword = findViewById(R.id.l_Password)

        SignUpButton.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        currentUser.observe(this){
            if(loginBtnClicked){
                loginBtnClicked = false
                if(it!=null){
                    val intent = Intent(this,FeedActivity::class.java)
                    startActivity(intent)
                    finish()
                    showToast("Login Successful")
                }else{
                    showToast( "Invalid Login Credentials")
                }
            }
        }
        SignInButton.setOnClickListener{
            if(validateDetails(LoginEmail.text.toString(), LoginPassword.text.toString())){
                userViewModal.login(LoginEmail.text.toString(),LoginPassword.text.toString())
                loginBtnClicked = true
            }else{
                showToast("Invalid Login Credentials")
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