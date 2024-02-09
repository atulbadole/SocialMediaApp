package com.capgemini.socialmediaapp.model.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val userId : Long,
    var name : String,
    val emailId : String,
    var password : String,
    var bio : String,
    var profileImage : ByteArray
) {}