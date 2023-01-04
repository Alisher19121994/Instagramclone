package com.example.instagram.network.databaseManager

import com.example.instagram.model.User

interface DBUsersHandler {

    fun onSuccess(user: ArrayList<User>)
    fun onError(exception: Exception)
}