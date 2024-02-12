package com.capgemini.socialmediaapp.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capgemini.socialmediaapp.R
import com.capgemini.socialmediaapp.model.post.Post
import com.capgemini.socialmediaapp.model.user.User
import com.capgemini.socialmediaapp.viewModel.post.PostViewModal
import com.capgemini.socialmediaapp.viewModel.user.UserViewModal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FeedActivity : AppCompatActivity() {

    lateinit var userViewModal : UserViewModal
    lateinit var postViewModel : PostViewModal
    lateinit var recyclerView : RecyclerView
//    var users = LiveData<List<User>>()
    lateinit var currentUser : User
    var updatedPostList = mutableListOf<Post>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        userViewModal = ViewModelProvider(this).get(UserViewModal::class.java)
        postViewModel = ViewModelProvider(this).get(PostViewModal::class.java)

        userViewModal.fetchCurrentUserDetails(this)
        recyclerView = findViewById(R.id.feedRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        checkForStoragePermission()

        userViewModal.currentUser.observe(this){ curUser ->
            curUser?.let {
                currentUser = curUser
                userViewModal.allUsers.observe(this){ users ->
                    Log.d("fromfeedactivity", "users fetched : ${users.size}")
                    val map = mutableMapOf<Long, User>()
                    for( i in users){
                        map.put(i.userId, i)
                    }
                    postViewModel.allPosts.observe(this){ posts ->
                        var sortedPosts = posts.sortedWith({ post1, post2 ->
                            post2.timestamp.compareTo(post1.timestamp)
                        })
                        CoroutineScope(Dispatchers.Main).launch {
                            if(users.size>0){
                                recyclerView.adapter = FeedViewAdapter(sortedPosts,
                                    map,
                                    currentUser.userId,
                                    {updatedFeed-> updatedPostList.add(updatedFeed) },
                                    ::openPostDetailPage,
                                    ::openCommentPage,
                                    ::openProfilePage
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun openPostDetailPage(post : Post, editClicked : Boolean){
        val intent = Intent(this@FeedActivity, PostDetailActivity::class.java)
        intent.putExtra("postId", post.postId)
        intent.putExtra("editClicked", editClicked)
        intent.putExtra("currentUserId", currentUser.userId)
        intent.putExtra("userIdOfCurrentPost", post.userId)
        startActivity(intent)
    }

    private fun checkForStoragePermission() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }
    }

    fun openCreatePostActivity(view: View) {
        val intent = Intent(this, CreatePostActivity::class.java)
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==1){
            if(grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                val dialog = AlertDialog.Builder(this@FeedActivity)
                dialog.setMessage("Without  storage permission you won't be able to change profile photo and add post with images.")
                .setPositiveButton("Ok"){ dialog, id ->
                    dialog.cancel()
                }
                dialog.create()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        for(post in updatedPostList){
            postViewModel.updatePost(post)
        }
        updatedPostList.clear()
    }

    private fun openCommentPage(postId: Long) {
        var intent = Intent(this, CommentActivity::class.java)
        intent.putExtra("postId", postId)
        intent.putExtra("currentUserId", currentUser.userId)
        startActivity(intent)
    }

    fun openProfilePage(userId: Long){
        val i = Intent(this, ProfilePageActivity::class.java)
        i.putExtra("userId", userId)
        i.putExtra("currentUserId", currentUser.userId)
        startActivity(i)
    }
}