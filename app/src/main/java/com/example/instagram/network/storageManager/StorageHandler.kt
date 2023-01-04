package com.example.instagram.network.storageManager

interface StorageHandler {
    fun onSuccess(imageUri: String)

    fun onError(exception: Exception?)
}