package com.example.instagram.fragment.menu

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.instagram.R
import com.example.instagram.adapter.ProfileAdapter
import com.example.instagram.model.Posts
import com.example.instagram.model.User
import com.example.instagram.network.authManager.AuthManager
import com.example.instagram.network.database.DBUserHandler
import com.example.instagram.network.database.DatabaseManager
import com.example.instagram.network.storageManager.StorageHandler
import com.example.instagram.network.storageManager.StorageManager
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*


/**
 * In ProfileFragment,user can check his/her posts and counts or change profile photo
 */
class ProfileFragment : BaseFragment() {

    var allPhoto: ArrayList<Uri> = ArrayList()
    var pickedPhoto: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {

        view.recyclerview_profile_main_id.layoutManager = GridLayoutManager(activity, 2)
        getProfileData(view)
        refreshAdapter(loadPost())
        logOut(view)
        loadUserInfo()
    }

    private fun logOut(view: View) {
        val authManager = AuthManager()
        view.profile_log_out_id.setOnClickListener {
            authManager.signOut()
            openSignInActivity(requireActivity())
        }
    }

    private fun refreshAdapter(posts: ArrayList<Posts>) {
        val adapter = activity?.let { ProfileAdapter(it, posts) }
        recyclerview_profile_main_id.adapter = adapter
    }

    private fun loadPost(): ArrayList<Posts> {
        val list: ArrayList<Posts> = ArrayList()
        list.add(Posts("https://manchestersightseeingtours.com/wp-content/uploads/Manchester-United-Football-Ground-NCNManchester-Marketing-1-525x350.jpg"))
        list.add(Posts("https://www.si.com/.image/c_limit%2Ccs_srgb%2Cq_auto:good%2Cw_700/MTkxMTk2ODg3MjA5MzU0NDc1/imago1011329909h.webp"))
        list.add(Posts("https://i2-prod.manchestereveningnews.co.uk/incoming/article23387908.ece/ALTERNATES/s810/0_GettyImages-1384570282.jpg"))
        list.add(Posts("https://i2-prod.football.london/incoming/article23788990.ece/ALTERNATES/s458/0_Marcus-Rashford.jpg"))
        list.add(Posts("https://i2-prod.manchestereveningnews.co.uk/sport/football/football-news/article23692634.ece/ALTERNATES/s615b/0_GettyImages-1313311867.jpg"))
        return list
    }

    private fun loadUserInfo() {
        val databaseManager = DatabaseManager()
        val authManager = AuthManager()
        databaseManager.loadUser(authManager.currentUser()!!.uid, object : DBUserHandler {
            override fun onSuccess(user: User?) {
                if (user != null) {

                    profile_fullname_id.text = user.fullname
                    profile_email_id.text = user.emailAddress

                    Glide.with(requireActivity())
                        .load(user.userImage)
                        .placeholder(R.drawable.defaultimage)
                        .error(R.drawable.defaultimage)
                        .into(profile_image_id)
                }
            }

            override fun onError(exception: Exception?) {}
        })
    }

    private fun getProfileData(view: View) {
        view.profile_image_id.setOnClickListener { pickPhotoFromGallery() }
    }

    private fun pickPhotoFromGallery() {
        FishBun.with(requireActivity())
            .setImageAdapter(GlideAdapter())
            .setMaxCount(1)
            .setMinCount(1)
            .setSelectedImages(allPhoto)
            .startAlbumWithActivityResultCallback(photoLauncher)
    }

    private var photoLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                allPhoto =
                    result.data!!.getParcelableArrayListExtra(FishBun.INTENT_PATH) ?: arrayListOf()
                pickedPhoto = allPhoto[0]
                uploadUserPhoto()
            }
        }

    private fun uploadUserPhoto() {

        if (pickedPhoto == null) return

        StorageManager.uploadUserPhoto(pickedPhoto, object : StorageHandler {
            override fun onSuccess(imageUri: String?) {
                val databaseManager = DatabaseManager()
                databaseManager.updateUserImage(imageUri)
                profile_image_id.setImageURI(pickedPhoto)
            }

            override fun onError(exception: Exception?) {}
        })
    }
}