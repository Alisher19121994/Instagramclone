package com.example.instagram.network.storageManager

import android.net.Uri
import com.example.instagram.network.authManager.AuthManager
import com.google.firebase.storage.FirebaseStorage

object StorageManager {

    var USER_PHOTO_PATH = "users"
    var POST_PHOTO_PATH = "posts"

    private var firebaseStorage = FirebaseStorage.getInstance()
    var storageReference = firebaseStorage.reference

    fun uploadUserPhoto(uri: Uri?, storageHandler: StorageHandler?) {
        val authManager = AuthManager()
        val uploadTask = storageReference.child(USER_PHOTO_PATH).child(authManager.currentUser()!!.uid).putFile(uri!!)

        uploadTask.addOnSuccessListener { taskSnapshot ->

            val result = taskSnapshot.metadata!!.reference!!.downloadUrl
            result.addOnSuccessListener {

                val imageUrl = result.toString()
                storageHandler!!.onSuccess(imageUrl)

            }.addOnFailureListener {
                storageHandler!!.onError(it)
            }
        }
    }
}