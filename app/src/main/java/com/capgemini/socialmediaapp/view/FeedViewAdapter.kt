package com.capgemini.socialmediaapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.capgemini.socialmediaapp.R
import com.capgemini.socialmediaapp.model.post.Post
import com.capgemini.socialmediaapp.model.user.User

class FeedViewAdapter(val feedList : MutableList<Post>, val userList : MutableList<User>) : RecyclerView.Adapter<FeedViewAdapter.FeedHolder>() {

    inner class FeedHolder(view : View) : ViewHolder(view){
        val userProfileImage : ImageView = view.findViewById(R.id.post_user_profile_image)
        val username : TextView = view.findViewById(R.id.feed_username)
        val textContent : TextView = view.findViewById(R.id.post_content)
        val image : TextView = view.findViewById(R.id.post_content_image)
        val postTime : TextView = view.findViewById(R.id.feed_time)
        val feedEditBtn : CardView = view.findViewById(R.id.feed_edit_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedHolder {
        val  view = LayoutInflater.from(parent.context).inflate(R.layout.feed_page_post_card, parent, false)
        return FeedHolder(view)
    }

    override fun getItemCount(): Int {
        return feedList.size
    }

    override fun onBindViewHolder(holder: FeedHolder, position: Int) {

    }
}