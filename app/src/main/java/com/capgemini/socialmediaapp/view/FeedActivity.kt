package com.capgemini.socialmediaapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
    var updatedPostList = mutableListOf<Post>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        userViewModal = ViewModelProvider(this).get(UserViewModal::class.java)
        postViewModel = ViewModelProvider(this).get(PostViewModal::class.java)

        userViewModal.fetchCurrentUserDetails(this)
        recyclerView = findViewById(R.id.feedRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        userViewModal.currentUser.observe(this){ currentUser ->
            currentUser?.let {
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
                        Log.d("fromfeedactivity", "posts fetched : ${sortedPosts.size}")
                        CoroutineScope(Dispatchers.Main).launch {
                            if(users.size>0){
                                Log.d("fromfeedactivity", "users : ${users.size},map : ${map.size} , posts : ${sortedPosts.size}")
                                recyclerView.adapter = FeedViewAdapter(sortedPosts,
                                    map,
                                    this@FeedActivity,
                                    currentUser.userId,
                                    {updatedFeed-> updatedPostList.add(updatedFeed) })
                                    { post : Post, editClicked : Boolean ->
                                        val intent = Intent(this@FeedActivity, PostDetailActivity::class.java)
                                        intent.putExtra("postId", post.postId)
                                        intent.putExtra("editClicked", editClicked)
                                        intent.putExtra("currentUserId", currentUser.userId)
                                        intent.putExtra("userIdOfCurrentPost", post.userId)
                                        startActivity(intent)
                                    }
                            }
                        }
                    }
                }
            }
        }
    }

    fun openCreatePostActivity(view: View) {
        val intent = Intent(this, CreatePostActivity::class.java)
        startActivity(intent)
    }

    override fun onStop() {
        super.onStop()
        for(post in updatedPostList){
            postViewModel.updatePost(post)
        }
        updatedPostList.clear()
    }
}