package com.capgemini.socialmediaapp.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.capgemini.socialmediaapp.R
import com.capgemini.socialmediaapp.model.post.Post
import com.capgemini.socialmediaapp.model.user.User
import com.capgemini.socialmediaapp.viewModel.user.UserViewModal
import java.time.Duration
import java.time.LocalDateTime

class FeedViewAdapter(val feedList : List<Post>,
                      val userDetailsMap : Map<Long, User>,
                      val ctx : Context,
                      val currentUserId : Long,
                      val updateFeed : (Post)->Unit
                    ) : RecyclerView.Adapter<FeedViewAdapter.FeedHolder>() {

    inner class FeedHolder(view : View) : ViewHolder(view){
        val userProfileImage : ImageView = view.findViewById(R.id.post_user_profile_image)
        val username : TextView = view.findViewById(R.id.feed_username)
        val textContent : TextView = view.findViewById(R.id.post_content)
        val image : ImageView = view.findViewById(R.id.post_content_image)
        val postTime : TextView = view.findViewById(R.id.feed_time)
        val feedEditBtn : CardView = view.findViewById(R.id.feed_edit_btn)
        val likeBtn : LinearLayout = view.findViewById(R.id.feed_like_btn)
        val likeImageView : ImageView = view.findViewById(R.id.feed_like_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedHolder {
        val  view = LayoutInflater.from(parent.context).inflate(R.layout.feed_page_post_card, parent, false)
        return FeedHolder(view)
    }

    override fun getItemCount(): Int {
        return feedList.size
    }

    override fun onBindViewHolder(holder: FeedHolder, position: Int) {
        var feed = feedList[position]
        val user = userDetailsMap[feed.userId]!!
        holder.username.text = user.name
        holder.textContent.text = feed.textContent
        if(feed.imageArray.length>0){
            Log.d("fromFeedAdapter", "adding image")
            Glide.with(ctx).load(feed.imageArray).into(holder.image)
        }
        holder.postTime.text = getTimePassedString(feed.timestamp)
        if(feed.likes.contains(currentUserId)){
            holder.likeImageView.setImageResource(R.drawable.red_heart)
        }else{
            holder.likeImageView.setImageResource(R.drawable.white_heart)
        }
        holder.likeBtn.setOnClickListener {
            var updatedFeed = feed
            var list = feed.likes.toMutableList()
            if(feed.likes.contains(currentUserId)){
                list.remove(currentUserId)
                updatedFeed.likes = list
            }else{
                list.add(currentUserId)
                updatedFeed.likes = list
            }
            updateFeed(updatedFeed)
        }
    }

    fun getTimePassedString(dateTime: LocalDateTime) : String {
        val currentTime = LocalDateTime.now()
        val dura = Duration.between(dateTime, currentTime)

        return when {
            dura.toDays()>0 -> "${dura.toDays()} days ago"
            dura.toHours()>0 -> "${dura.toHours()} hours ago"
            dura.toMinutes()>0 -> "${dura.toMinutes()} minutes ago"
             else -> "Just now"
        }
    }
}