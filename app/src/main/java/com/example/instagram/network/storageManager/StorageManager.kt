package com.example.instagram.network.storageManager

import android.net.Uri
import com.example.instagram.network.authManager.AuthManager
import com.google.firebase.storage.FirebaseStorage

object StorageManager {

    var USER_PHOTO_PATH = "users"
    var POST_PHOTO_PATH = "posts"

    private var firebaseStorage = FirebaseStorage.getInstance()
    var storageReference = firebaseStorage.reference

    fun uploadUserPhoto(uri: Uri, storageHandler: StorageHandler) {
        val authManager = AuthManager()
        val filename = authManager.currentUser()!!.uid + ".png"

        val uploadTask = storageReference.child(USER_PHOTO_PATH).child(filename).putFile(uri)

        uploadTask.addOnSuccessListener { taskSnapshot ->

            val result = taskSnapshot.metadata!!.reference!!.downloadUrl
            result.addOnSuccessListener {

                val imageUrl = it.toString()
                storageHandler.onSuccess(imageUrl)

            }.addOnFailureListener {
                storageHandler.onError(it)
            }
        }.addOnFailureListener { error ->
            storageHandler.onError(error)
        }
    }

    fun uploadPostPhoto(uri: Uri, storageHandler: StorageHandler) {
        val authManager = AuthManager()

        // everytime we can post different photos
        val filename = authManager.currentUser()!!.uid + "_" +System.currentTimeMillis().toString() +  ".png"

        val uploadTask = storageReference.child(POST_PHOTO_PATH).child(filename).putFile(uri)

        uploadTask.addOnSuccessListener { taskSnapshot ->

            val result = taskSnapshot.metadata!!.reference!!.downloadUrl
            result.addOnSuccessListener {

                val imageUrl = it.toString()
                storageHandler.onSuccess(imageUrl)

            }.addOnFailureListener {
                storageHandler.onError(it)
            }
        }.addOnFailureListener { error ->
            storageHandler.onError(error)
        }
    }
}