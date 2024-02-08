package com.capgemini.socialmediaapp.view

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.capgemini.socialmediaapp.model.post.Post
import com.capgemini.socialmediaapp.model.user.User

class FeedViewAdapter(val feedList : MutableList<Post>, val userList : MutableList<User>) : RecyclerView.Adapter<FeedViewAdapter.FeedHolder>() {

    inner class FeedHolder(view : View) : ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return feedList.size
    }

    override fun onBindViewHolder(holder: FeedHolder, position: Int) {
        TODO("Not yet implemented")
    }
}