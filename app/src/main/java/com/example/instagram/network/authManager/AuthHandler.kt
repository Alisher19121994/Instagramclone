package com.example.instagram.network.authManager

interface AuthHandler {
    fun onSuccess(uId: String)
    fun onError(exception: Exception?)
}