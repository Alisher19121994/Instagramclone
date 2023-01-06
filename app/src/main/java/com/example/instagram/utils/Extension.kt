package com.example.instagram.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.provider.Settings
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import com.example.instagram.R

object Extension {


    @SuppressLint("HardwareIds")
    fun getDevice(context: android.content.Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun Activity.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    }

    fun dialogDeleteDouble(
        context: android.content.Context?,
        title: String?,
        dialogListener: DialogListener
    ) {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_delete_double)
        dialog.setCanceledOnTouchOutside(true)

        val titleButton = dialog.findViewById<TextView>(R.id.title_id)
        val confirmButton = dialog.findViewById<TextView>(R.id.delete_id)
        val cancelButton = dialog.findViewById<TextView>(R.id.cancel_id)

        titleButton.text = title

        confirmButton.setOnClickListener {
            dialog.dismiss()
            dialogListener.onCallback(true)
        }
        cancelButton.setOnClickListener {
            dialog.dismiss()
            dialogListener.onCallback(false)
        }
        dialog.show()

    }

    fun dialogDeleteSingle(
        context: android.content.Context?,
        title: String?,
        dialogListener: DialogListener
    ) {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_delete)
        dialog.setCanceledOnTouchOutside(true)

        val titleButton = dialog.findViewById<TextView>(R.id.title_id)
        val confirmButton = dialog.findViewById<TextView>(R.id.delete_id)

        titleButton.text = title
        confirmButton.setOnClickListener {
            dialog.dismiss()
            dialogListener.onCallback(true)
        }
        dialog.show()

    }

}