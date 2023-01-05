package com.example.instagram.network.databaseManager

interface DBFollowHandler {

    fun onSuccess(isDone: Boolean)
    fun onError(exception: Exception)
}