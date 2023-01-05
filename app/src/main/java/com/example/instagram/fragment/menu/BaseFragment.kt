package com.example.instagram.fragment.menu

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.fragment.app.Fragment
import com.example.instagram.R
import com.example.instagram.activity.SignInActivity

/**
 * A simple [Fragment] subclass.
 * Use the [BaseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
open class BaseFragment : Fragment() {



    fun showLoading(dialog: Dialog) {
        dialog.setContentView(R.layout.progress_bar)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.create()
        dialog.show()
    }

    fun dismissLoading(dialog: Dialog) {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }


    fun openSignInActivity(activity: Activity) {
        val intent = Intent(requireContext(), SignInActivity::class.java)
        startActivity(intent)
        activity.finish()
    }
}