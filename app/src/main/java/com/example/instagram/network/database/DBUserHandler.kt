package com.example.instagram.network.database

import com.example.instagram.model.User

interface DBUserHandler {
    fun onSuccess(user: User?)

    fun onError(exception: Exception?)
}