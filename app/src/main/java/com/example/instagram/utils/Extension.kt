package com.example.instagram.utils

import android.app.Activity
import android.os.Message
import android.widget.Toast

object Extension {

    fun Activity.toast(message: String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()

    }
}