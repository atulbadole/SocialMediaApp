package com.capgemini.socialmediaapp.model

import androidx.room.TypeConverter
import java.time.LocalDateTime

class DateTimeConverter {
    @TypeConverter
    fun fromTimestamp(s : String) : LocalDateTime {
        return LocalDateTime.parse(s)
    }
    @TypeConverter
    fun toTimestamp(time : LocalDateTime) : String {
        return time.toString()
    }
}