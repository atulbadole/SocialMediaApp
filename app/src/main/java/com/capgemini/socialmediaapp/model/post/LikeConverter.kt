package com.capgemini.socialmediaapp.model.post

import androidx.room.TypeConverter

class LikeConverter {
    @TypeConverter
    fun fromString(s : String) = s.split(",").map { it.toLong() }

    @TypeConverter
    fun toString(likeList : List<Long>) = likeList.joinToString { "," }
}