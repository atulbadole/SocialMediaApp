package com.capgemini.socialmediaapp.view

import android.content.Context
import android.widget.Toast

public fun Toast.showMessage(ctx : Context, message : String){
    Toast.makeText(ctx, message, Toast.LENGTH_LONG).show()
}