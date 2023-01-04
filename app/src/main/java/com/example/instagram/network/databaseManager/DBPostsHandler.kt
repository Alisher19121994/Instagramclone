package com.example.instagram.network.databaseManager

import com.example.instagram.model.Posts

interface DBPostsHandler {

    fun onSuccess(posts: ArrayList<Posts>)
    fun onError(exception: Exception)

}