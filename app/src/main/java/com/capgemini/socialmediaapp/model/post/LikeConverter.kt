package com.capgemini.socialmediaapp.model.post

import androidx.room.TypeConverter

class LikeConverter {
    @TypeConverter
    fun fromString(s : String) = if(s.length>0) s.split(",").map { it.toLong() } else listOf()

    @TypeConverter
    fun toString(likeList : List<Long>) = if(likeList.size>0)likeList.joinToString(",") else ""
}