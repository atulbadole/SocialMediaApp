package com.capgemini.socialmediaapp.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.capgemini.socialmediaapp.model.comment.Comment
import com.capgemini.socialmediaapp.model.comment.CommentDao
import com.capgemini.socialmediaapp.model.post.LikeConverter
import com.capgemini.socialmediaapp.model.post.Post
import com.capgemini.socialmediaapp.model.post.PostDao
import com.capgemini.socialmediaapp.model.user.User
import com.capgemini.socialmediaapp.model.user.UserDao

@Database(entities = [User::class, Post::class, Comment::class], version = 1, exportSchema = false)
@TypeConverters(LikeConverter::class, DateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao() : UserDao
    abstract fun postDao() : PostDao
    abstract fun commentDao() : CommentDao

    companion object {
        private var instance : AppDatabase? = null

        fun getInstance(context : Context) : AppDatabase{
            return instance?:createNewInstance(context)
        }

        private fun createNewInstance(context: Context): AppDatabase {
            this.instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "socialMediaApp.db").build()
            return this.instance as AppDatabase
        }
    }

}