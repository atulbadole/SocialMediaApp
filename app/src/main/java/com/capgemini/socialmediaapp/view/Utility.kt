package com.capgemini.socialmediaapp.view

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.time.Duration
import java.time.LocalDateTime

fun showMessage(ctx : Context, message : String){
    Toast.makeText(ctx, message, Toast.LENGTH_LONG).show()
}

fun getTimePassedString(dateTime: LocalDateTime) : String {
    val currentTime = LocalDateTime.now()
    val dura = Duration.between(dateTime, currentTime)

    return when {
        dura.toDays()>0 -> "${dura.toDays()} days ago"
        dura.toHours()>0 -> "${dura.toHours()} hours ago"
        dura.toMinutes()>0 -> "${dura.toMinutes()} minutes ago"
        else -> "Just now"
    }
}

//val pickImageLauncher = AppCompatActivity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//    if (result.resultCode == Activity.RESULT_OK) {
//        result.data?.data?.let{
//            val imageUri = it
//            postImage.setImageURI(imageUri)
//            updatedPost!!.imageArray = getImagePath(imageUri)
//        }
//    }
//}

fun getImagePath(imageUri: Uri, contentResolver : ContentResolver): String {
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = contentResolver.query(imageUri, projection, null, null)
    cursor?.use {
        val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        it.moveToFirst()
        return it.getString(columnIndex)
    }
    return ""
}