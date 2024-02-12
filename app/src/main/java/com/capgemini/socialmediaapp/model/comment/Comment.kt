package com.capgemini.socialmediaapp.model.comment

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.capgemini.socialmediaapp.model.post.Post
import com.capgemini.socialmediaapp.model.user.User
import java.time.LocalDateTime

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"]
        ),
        ForeignKey(
            entity = Post::class,
            parentColumns = ["postId"],
            childColumns = ["postId"]
        )
    ]
)
data class Comment(
    var commentMessage : String,
    val postId : Long,
    val userId : Long,
    var timestamp : LocalDateTime,
    @PrimaryKey(autoGenerate = true) val commentId : Long = 0L
) {
}