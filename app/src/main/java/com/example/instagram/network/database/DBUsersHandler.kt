package com.example.instagram.network.database

import com.example.instagram.model.User

interface DBUsersHandler {

    fun onSuccess(user: ArrayList<User?>?)
    fun onError(exception: Exception?)
}