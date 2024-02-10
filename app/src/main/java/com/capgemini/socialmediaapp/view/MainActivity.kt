package com.capgemini.socialmediaapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.capgemini.socialmediaapp.R
import com.capgemini.socialmediaapp.viewModel.user.UserViewModal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask


class MainActivity : AppCompatActivity() {

    lateinit var userViewModal: UserViewModal
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userViewModal = ViewModelProvider(this).get(UserViewModal::class.java)
        Timer().schedule(object : TimerTask(){
            override fun run() {
//                CoroutineScope(Dispatchers.Main).launch {
//                    userViewModal.currentUser.observe(this@MainActivity){
//                        if(it==null){
                            val intent = Intent(this@MainActivity, LoginActivity::class.java)
                            startActivity(intent)
//                        }else{
//                            val intent = Intent(this@MainActivity, FeedActivity::class.java)
//                            startActivity(intent)
//                        }
                        finish()
//                    }
//                }
            }
        }, 2000)
    }
}