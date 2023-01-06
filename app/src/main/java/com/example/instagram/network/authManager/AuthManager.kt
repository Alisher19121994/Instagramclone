package com.example.instagram.network.authManager

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class AuthManager {

    var firebaseAuth:FirebaseAuth = FirebaseAuth.getInstance()

    fun currentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    fun isSignedIn(): Boolean {
        return currentUser() != null
    }

    fun signIn(email: String, password: String, authHandler: AuthHandler) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val firebaseUserId = currentUser()!!.uid
                authHandler.onSuccess(firebaseUserId)
            } else {
                authHandler.onError(task.exception)
            }
        }
    }

    fun signUp(email: String, password: String, authHandler: AuthHandler) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUserId = currentUser()!!.uid
                    authHandler.onSuccess(firebaseUserId)
                } else {
                    authHandler.onError(task.exception)
                }
            }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }


}