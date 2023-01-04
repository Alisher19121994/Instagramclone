package com.example.instagram.network.database

import com.example.instagram.model.User
import com.example.instagram.network.authManager.AuthManager
import com.google.firebase.firestore.FirebaseFirestore


class DatabaseManager {
    private var USER_PATH = "users"
    private var POST_PATH = "posts"
    private var FEED_PATH = "feeds"
    private var FOLLOWING_PATH = "following"
    private var FOLLOWERS_PATH = "followers"

    private var database = FirebaseFirestore.getInstance()

    fun storeUser(user: User, dbUserHandler: DBUserHandler) {
        database.collection(USER_PATH).document(user.uid!!).set(user).addOnSuccessListener {

            dbUserHandler.onSuccess(user)
        }.addOnFailureListener { e -> dbUserHandler.onError(e) }
    }


    fun loadUser(uid: String?, dbUserHandler: DBUserHandler) {
        database.collection(USER_PATH).document(uid!!).get().addOnSuccessListener { result ->
            if (result.exists()) {

                val fullname = result.getString("fullname")
                val email = result.getString("email")
                val password = result.getString("password")
                val userImage = result.getString("userImage")

                val user = User(fullname!!, email!!, password, userImage)
                user.uid = uid
                dbUserHandler.onSuccess(user)
            } else {
                dbUserHandler.onSuccess(null)
            }
        }.addOnFailureListener { e -> dbUserHandler.onError(e) }
    }

    fun updateUserImage(imageUri: String?) {
        val authManager = AuthManager()
        database.collection(USER_PATH).document(authManager.currentUser()!!.uid)
            .update("userImage", imageUri)
    }

    fun loadUsers(dbUsersHandler: DBUsersHandler) {
        database.collection(USER_PATH).get().addOnCompleteListener { task ->

            val usersList = ArrayList<User>()

            if (task.isSuccessful) {
                for (document in task.result) {

                }
            } else {
                dbUsersHandler.onError(task.exception)
            }
        }
    }

}