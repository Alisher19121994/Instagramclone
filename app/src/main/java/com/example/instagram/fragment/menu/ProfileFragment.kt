package com.example.instagram.fragment.menu

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagram.R
import com.example.instagram.adapter.ProfileAdapter
import com.example.instagram.model.Posts
import com.example.instagram.model.User
import com.example.instagram.network.authManager.AuthManager
import com.example.instagram.network.databaseManager.DBPostsHandler
import com.example.instagram.network.databaseManager.DBUserHandler
import com.example.instagram.network.databaseManager.DBUsersHandler
import com.example.instagram.network.databaseManager.DatabaseManager
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

    lateinit var recyclerView: RecyclerView

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
        recyclerView = view.findViewById(R.id.recyclerview_profile_main_id)
        recyclerView.layoutManager = GridLayoutManager(activity, 2)
        view.profile_image_id.setOnClickListener { pickPhotoFromGallery() }
        logOut(view)
        loadUserInfo()
        loadMyPosts()
        loadMyFollowing()
        loadMyFollowers()
    }

    private fun loadMyFollowing() {
        val authManager = AuthManager()
        val uid = authManager.currentUser()!!.uid
        val databaseManager = DatabaseManager()

        databaseManager.loadFollowing(uid, object : DBUsersHandler {
            override fun onSuccess(user: ArrayList<User>) {

                profile_followings_size_id.text = user.size.toString()
            }

            override fun onError(exception: Exception) {

            }
        })
    }
    private fun loadMyFollowers() {
        val authManager = AuthManager()
        val uid = authManager.currentUser()!!.uid
        val databaseManager = DatabaseManager()

        databaseManager.loadFollowers(uid, object : DBUsersHandler {
            override fun onSuccess(user: ArrayList<User>) {

                profile_followers_size_id.text = user.size.toString()
            }

            override fun onError(exception: Exception) {

            }
        })
    }

    private fun loadMyPosts() {
        val authManager = AuthManager()
        val uid = authManager.currentUser()!!.uid
        val databaseManager = DatabaseManager()

        databaseManager.loadPosts(uid, object : DBPostsHandler {
            override fun onSuccess(posts: ArrayList<Posts>) {

                profile_posts_size_id.text = posts.size.toString()
                refreshAdapter(posts)
            }

            override fun onError(exception: Exception) {

            }
        })
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
        recyclerView.adapter = adapter
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

            override fun onError(exception: Exception) {}
        })
    }


    private fun pickPhotoFromGallery() {
        allPhoto.clear()
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

        val dialog = Dialog(requireContext())
        if (pickedPhoto == null) return
        showLoading(dialog)
        StorageManager.uploadUserPhoto(pickedPhoto!!, object : StorageHandler {
            override fun onSuccess(imageUri: String) {

                dismissLoading(dialog)
                val databaseManager = DatabaseManager()
                databaseManager.updateUserImage(imageUri) // saved photo

                profile_image_id.setImageURI(pickedPhoto) // shown in UI
            }

            override fun onError(exception: Exception?) {
                dismissLoading(dialog)
            }
        })
    }
}