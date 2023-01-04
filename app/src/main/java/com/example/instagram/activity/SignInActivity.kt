package com.example.instagram.activity

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.instagram.R
import com.example.instagram.network.authManager.AuthHandler
import com.example.instagram.network.authManager.AuthManager
import com.example.instagram.utils.Extension.toast


/**
 * In this SignInActivity that, user can log in  by using email and password
 * as well as this activity extended from BaseActivity for some initial implementations
 */
class SignInActivity : BaseActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        initViews()
    }

    private fun initViews() {
        openPage()
    }

    private fun openPage() {
        progressBar = findViewById(R.id.progress_circular_sign_in_id)
        editTextEmail = findViewById(R.id.sign_in_email_id)
        editTextPassword = findViewById(R.id.sign_in_password_id)


        val signInButton: AppCompatButton = findViewById(R.id.sign_in_button_id)
        signInButton.setOnClickListener {

            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (email.isEmpty()) {
                toast("Email must NOT be empty!")
            } else if (password.isEmpty()) {
                toast("Password must NOT be empty!")
            } else {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    fireBaseSignIn(email, password)
                }
            }
        }
        val signUpButton: TextView = findViewById(R.id.sign_in_button_sign_up_id)
        signUpButton.setOnClickListener {
            openSignUpActivity(context)
        }
    }

    private fun fireBaseSignIn(email: String?, password: String?) {
        progressBar.visibility = View.VISIBLE
        val authManager = AuthManager()
        if (email != null) {
            if (password != null) {
                authManager.signIn(email, password, object : AuthHandler {
                    override fun onSuccess(uId: String?) {
                        progressBar.visibility = View.GONE
                        openMainActivity(context)
                    }

                    override fun onError(exception: Exception?) {
                        progressBar.visibility = View.GONE
                    }
                })
            }
        }
    }
}