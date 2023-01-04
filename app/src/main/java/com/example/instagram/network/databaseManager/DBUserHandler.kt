package com.example.instagram.network.databaseManager

import com.example.instagram.model.User

interface DBUserHandler {
    fun onSuccess(user: User? = null)

    fun onError(exception: Exception)
}