package com.example.instagram.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.WindowManager
import com.example.instagram.R
import com.example.instagram.network.authManager.AuthManager


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
}