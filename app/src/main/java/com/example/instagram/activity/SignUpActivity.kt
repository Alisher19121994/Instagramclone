package com.example.instagram.activity

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.example.instagram.R
import com.example.instagram.model.User
import com.example.instagram.network.PrefsManager
import com.example.instagram.network.authManager.AuthHandler
import com.example.instagram.network.authManager.AuthManager
import com.example.instagram.network.databaseManager.DBUserHandler
import com.example.instagram.network.databaseManager.DatabaseManager
import com.example.instagram.utils.Extension
import com.example.instagram.utils.Extension.toast


/**
 * In this SignUpActivity that, user can sign up  by using email,fullname,password
 * as well as this activity extended from BaseActivity for some initial implementations
 */
class SignUpActivity : BaseActivity() {

    val TAG = SignUpActivity::class.java.simpleName
    private lateinit var fullname: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initViews()
    }

    private fun initViews() {
        openPage()
    }

    private fun openPage() {
        fullname = findViewById(R.id.sign_up_fullname_id)
        email = findViewById(R.id.sign_up_email_id)
        password = findViewById(R.id.sign_up_password_id)
        confirmPassword = findViewById(R.id.sign_up_confirm_password_id)

        val signUpButton: AppCompatButton = findViewById(R.id.sign_up_button_id)
        signUpButton.setOnClickListener {

            val textFullname = fullname.text.toString().trim()
            val textEmail = email.text.toString().trim()
            val textPassword = password.text.toString().trim()

            if (textFullname.isNotEmpty() && textEmail.isNotEmpty() && textPassword.isNotEmpty()) {
                val user = User(textFullname, textEmail, textPassword,"")
                firebaseSignUp(user)
            } else if (textFullname.isEmpty()) {
                toast("Fullname must NOT be empty!")
            } else if (textEmail.isEmpty()) {
                toast("Email must NOT be empty!")
            } else if (textPassword.isEmpty()) {
                toast("Password must NOT be empty!")
            }
        }

        val buttonSignIn: TextView = findViewById(R.id.sign_up_button_sign_in_id)
        buttonSignIn.setOnClickListener { finish() }
    }

    private fun firebaseSignUp(user: User) {
        val authManager = AuthManager()

        authManager.signUp(user.emailAddress, user.password, object : AuthHandler {

            override fun onSuccess(uId: String) {
                user.uid = uId // connection

                // saved database in onSuccess
                storeUserToDatabase(user)
                toast("Signed Up")
            }

            override fun onError(exception: Exception?) {
                toast("Failed")
            }
        })
    }

    private fun storeUserToDatabase(user: User) {

        val dialog = Dialog(this)
        showLoading(dialog)

        //  user.deviceToken = PrefsManager(this).loadDeviceToken()!!
        //    user.deviceId = Extension.getDevice(this)

        val databaseManager = DatabaseManager()
        databaseManager.storeUser(user, object : DBUserHandler {

            override fun onSuccess(user: User?) {
                dismissLoading(dialog)
                openMainActivity(context)
            }

            override fun onError(exception: Exception) {
                dismissLoading(dialog)
            }
        })
    }
}