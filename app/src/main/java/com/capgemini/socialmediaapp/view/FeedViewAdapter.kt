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
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.capgemini.socialmediaapp.R
import com.capgemini.socialmediaapp.model.post.Post
import com.capgemini.socialmediaapp.model.user.User
import java.time.Duration
import java.time.LocalDateTime

class FeedViewAdapter(val feedList : List<Post>,
                      val userDetailsMap : Map<Long, User>,
                      val ctx : Context,
                      val currentUserId : Long,
                      val updateFeed : (Post)->Unit,
                      val openPostDetails : (Post, Boolean)->Unit
                    ) : RecyclerView.Adapter<FeedViewAdapter.FeedHolder>() {

    inner class FeedHolder(view : View) : ViewHolder(view){
        val userProfileImage : ImageView = view.findViewById(R.id.post_user_profile_image)
        val username : TextView = view.findViewById(R.id.feed_username)
        val textContent : TextView = view.findViewById(R.id.post_content)
        val image : ImageView = view.findViewById(R.id.post_content_image)
        val postTime : TextView = view.findViewById(R.id.feed_time)
        val likeBtn : LinearLayout = view.findViewById(R.id.feed_like_btn)
        val likeImageView : ImageView = view.findViewById(R.id.feed_like_image)
        val feedEditBtn : CardView = view.findViewById(R.id.feed_edit_btn)
        val viewRoot = view

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
        holder.postTime.text = getTimePassedString(feed.timestamp)
        if(feed.likes.contains(currentUserId)){
            holder.likeImageView.setImageResource(R.drawable.red_heart)
        }else{
            holder.likeImageView.setImageResource(R.drawable.white_heart)
        }
        holder.likeBtn.setOnClickListener {
            if(user.userId!=currentUserId){
                var updatedFeed = feed
                var list = feed.likes.toMutableList()
                if(feed.likes.contains(currentUserId)){
                    list.remove(currentUserId)
                    updatedFeed.likes = list
                    holder.likeImageView.setImageResource(R.drawable.white_heart)
                }else{
                    list.add(currentUserId)
                    updatedFeed.likes = list
                    holder.likeImageView.setImageResource(R.drawable.red_heart)
                }
                updateFeed(updatedFeed)
            }else{
                showMessage(ctx, "Cannot like your own post.")
            }
        }
        try{
            if(user.profileImage.length>0){
                Glide.with(ctx).load(user.profileImage).into(holder.userProfileImage)
            }
            if(feed.imageArray.length>0){
                Log.d("fromFeedAdapter", "adding image")
                Glide.with(ctx).load(feed.imageArray).into(holder.image)
            }
        }catch (e: Exception){
            Log.d("feedviewadapter", "Error while setting image in imageView : ${e.localizedMessage}")
        }
        if(feed.userId!=currentUserId){
            holder.feedEditBtn.isClickable = false
            holder.feedEditBtn.isVisible = false
        }else{
            holder.feedEditBtn.setOnClickListener {
                openPostDetails(feed, true)
            }
        }
        holder.viewRoot.setOnClickListener {
            openPostDetails(feed, false)
        }
    }

}