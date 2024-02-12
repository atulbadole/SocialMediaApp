package com.capgemini.socialmediaapp.view


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capgemini.socialmediaapp.R
import com.capgemini.socialmediaapp.model.comment.Comment
import com.capgemini.socialmediaapp.model.user.User

class CommentAdapter(
    val comments: List<Comment>,
    val userMap : MutableMap<Long, User>,
    val currentUserId: Long,
    val ctx : Context,
    val clickListener: (Comment)->Unit
) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_list_item, parent, false) as CardView
        return CommentViewHolder(view)
    }
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        val user=userMap[comment.userId]!!
        val isCurrentUser = comment.userId == currentUserId
        holder.updateEditButtonVisibility(comment.userId==currentUserId)
        holder.commentNameTextView.setText(user.name)
        if (user.profileImage.length>0){
            Glide.with(ctx).load(user.profileImage).into(holder.userProfilePhoto)
        }
        holder.commentTextView.setText(comment.commentMessage)
        holder.time_buttonB.text = getTimePassedString(comment.timestamp)
        holder.time_buttonB.visibility = View.VISIBLE
        holder.editButton.visibility = if (isCurrentUser) View.VISIBLE else View.GONE
        holder.commentTextView.setEnabled(false)
        holder.editButton.setOnClickListener{
            if(holder.editButton.text.toString().equals("Edit")){
                holder.editButton.setText("Save")
                holder.commentTextView.isEnabled = true
            }else{
                holder.editButton.setText("Edit")
                holder.commentTextView.isEnabled = false
                if(holder.commentTextView.text.toString().trim().length==0){
                    showMessage(holder.itemView.context, "Empty comments are not allowed")
                    holder.commentTextView.setText(comment.commentMessage)
                }else{
                    comment.commentMessage = holder.commentTextView.text.toString()
                    clickListener(comment)
                }
            }
        }
    }

    override fun getItemCount(): Int = comments.size

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val commentNameTextView: TextView = itemView.findViewById(R.id.commentNameTextView)
        val commentTextView: EditText = itemView.findViewById(R.id.commentTextView)
        val editButton: TextView = itemView.findViewById(R.id.editButton)
        val time_buttonB: TextView = itemView.findViewById(R.id.time_buttonB)
        val userProfilePhoto : ImageView= itemView.findViewById(R.id.commentImageView)

        fun updateEditButtonVisibility(isCurrentUserComment: Boolean) {
            editButton.visibility = if (isCurrentUserComment) View.VISIBLE else View.GONE
        }
    }
}