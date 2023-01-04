package com.example.instagram.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.instagram.R

open class BaseActivity : AppCompatActivity() {

    lateinit var context: Context

    open fun showLoading(dialog: Dialog) {
        dialog.setContentView(R.layout.progress_bar)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.create()
        dialog.show()
    }

    open fun dismissLoading(dialog: Dialog) {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
    }

    open fun openSignInActivity(context: Context?) {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    open fun openSignUpActivity(context: Context?) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    open fun openMainActivity(context: Context?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}