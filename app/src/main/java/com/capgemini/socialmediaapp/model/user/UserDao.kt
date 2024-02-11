package com.capgemini.socialmediaapp.model.user

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.capgemini.socialmediaapp.model.user.User

@Dao
interface UserDao {

    @Insert
    suspend fun addUser(newUser : User)

    @Update
    suspend fun updateUser(updatedUser : User)

    @Query("select * from user where userId==:id")
    suspend fun getuserDetails(id : Long) : User?

    @Query("select * from user where emailId=:userEmail and password=:password")
    fun login(userEmail : String, password : String) : User?

    @Query("select * from user")
    fun getAllUsers() : LiveData<List<User>>

}