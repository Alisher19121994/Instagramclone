package com.example.instagram.network.databaseManager

import com.example.instagram.model.Posts

interface DBPostHandler {

    fun onSuccess(posts: Posts)
    fun onError(exception: Exception)

}