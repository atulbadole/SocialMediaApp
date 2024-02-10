package com.capgemini.socialmediaapp.model.post

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.capgemini.socialmediaapp.model.user.User
import java.time.LocalDateTime
import java.util.Date

@Entity(foreignKeys = [
    ForeignKey(
        entity = User::class,
        parentColumns = ["userId"],
        childColumns = ["userId"]
    )
])
data class Post (
    val userId : Long,
    var timestamp : LocalDateTime,
    var imageArray : String,
    var textContent : String,
    var likes : List<Long>,
    @PrimaryKey(autoGenerate = true) val postId : Long = 0L
)