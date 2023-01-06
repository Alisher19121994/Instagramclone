package com.example.instagram.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.WindowManager
import com.example.instagram.R
import com.example.instagram.network.PrefsManager
import com.example.instagram.network.authManager.AuthManager
import com.example.instagram.utils.Extension.toast
import com.google.firebase.messaging.FirebaseMessaging


/**
 * In this SplashActivity, user can visit to SignInActivity or MainActivity
 * within a few second
 */
class SplashActivity : BaseActivity() {

    val TAG = SplashActivity::class.java.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash)
        initViews()
    }

    private fun initViews() {
        countDownTimer()
        //loadFCMToken()
    }

    private fun countDownTimer() {
        object : CountDownTimer(2000, 1000) {

            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                val authManager = AuthManager()
                if (authManager.isSignedIn()) {
                    openMainActivity(this@SplashActivity)
                } else {
                    openSignInActivity(this@SplashActivity)

                }
            }
        }.start()
    }

    private fun loadFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (!it.isSuccessful) {
                toast("Fetching FCM registration token is failed")
                return@addOnCompleteListener
            }

            // Getting new FCM  registration token
            // Saved it in locally to use later
            val token = it.result
            PrefsManager(this).storeDeviceToken(token.toString())
        }
    }
}