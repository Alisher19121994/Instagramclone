package com.example.instagram.fragment.menu


import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.instagram.R
import com.example.instagram.model.Posts
import com.example.instagram.model.User
import com.example.instagram.network.authManager.AuthManager
import com.example.instagram.network.connections.UploadListener
import com.example.instagram.network.databaseManager.DBPostHandler
import com.example.instagram.network.databaseManager.DBUserHandler
import com.example.instagram.network.databaseManager.DatabaseManager
import com.example.instagram.network.storageManager.StorageHandler
import com.example.instagram.network.storageManager.StorageManager
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import kotlinx.android.synthetic.main.fragment_upload.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * In UpLoadFragment, user cal upload his/her posts with images and captions
 */
class UploadFragment : BaseFragment() {

    private var listener: UploadListener? = null
    private lateinit var frameLayoutPhoto: FrameLayout
    private lateinit var imageViewPhoto: ImageView
    private lateinit var editTextCaption: EditText

    private var pickedPhoto: Uri? = null
    private var allPhotos = ArrayList<Uri>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_upload, container, false)
        initViews(view)
        return view
    }

    /**
     * onAttach is for communication of Fragment
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = if (context is UploadListener) {
            context
        } else {
            throw RuntimeException(context.toString() + "must implement upload!")
        }
    }

    /**
     * onDetach is for communication of Fragment
     */
    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun initViews(view: View) {
        frameLayoutPhoto = view.findViewById(R.id.fl_photo)
        imageViewPhoto = view.findViewById(R.id.iv_photo)
        editTextCaption = view.findViewById(R.id.et_caption)
        openPages(view)
    }

    private fun openPages(view: View) {
        view.iv_close.setOnClickListener { hidePickedPhoto() }
        view.iv_pick.setOnClickListener { pickFishBunPhoto() }
        view.upload_image_on_toolbar_id.setOnClickListener { uploadNewPost() }
    }

    /**
     * Picking a photo by using FishBun Liberary
     */
    private fun pickFishBunPhoto() {
        allPhotos.clear()
        FishBun.with(this)
            .setImageAdapter(GlideAdapter())
            .setMaxCount(1)
            .setMinCount(1)
            .setSelectedImages(allPhotos)
            .startAlbumWithActivityResultCallback(photoLauncher)
    }

    private var photoLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                allPhotos =
                    result.data!!.getParcelableArrayListExtra(FishBun.INTENT_PATH) ?: arrayListOf()
                pickedPhoto = allPhotos[0]
                showPickedPhoto()
            }
        }

    private fun showPickedPhoto() {
        allPhotos.clear()
        frameLayoutPhoto.visibility = View.VISIBLE
        imageViewPhoto.setImageURI(pickedPhoto)
    }

    private fun hidePickedPhoto() {
        pickedPhoto = null
        frameLayoutPhoto.visibility = View.GONE
    }

    private fun uploadNewPost() {

        val getCaption = editTextCaption.text.toString().trim()

        if (getCaption.isNotEmpty() && pickedPhoto != null) {

            uploadThisPostPhoto(getCaption, pickedPhoto!!)

        } else if (getCaption.isEmpty()) {
            Toast.makeText(requireContext(), "Write a caption", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadThisPostPhoto(caption: String, uri: Uri) {
        val dialog = Dialog(requireContext())
        showLoading(dialog)

        StorageManager.uploadPostPhoto(uri, object : StorageHandler {

            override fun onSuccess(imageUri: String) {

                val post = Posts(caption, imageUri)
                post.currentDate = getCurrentTime()

                val authManager = AuthManager()
                val uid = authManager.currentUser()!!.uid

                val databaseManager = DatabaseManager()
                databaseManager.loadUser(uid, object : DBUserHandler {

                    override fun onSuccess(user: User?) {
                        post.uid = uid
                        post.fullname = user!!.fullname
                        post.userImage = user.userImage
                        storePostToDatabase(post)
                        dismissLoading(dialog)

                    }

                    override fun onError(exception: Exception) {
                        dismissLoading(dialog)
                    }
                })


            }

            override fun onError(exception: Exception?) {
                dismissLoading(dialog)
            }
        })

    }

    private fun storePostToDatabase(posts: Posts) {
        val databaseManager = DatabaseManager()
        databaseManager.storePost(posts, object : DBPostHandler {

            override fun onSuccess(posts: Posts) {

                storeFeedToDatabase(posts) // in the same time that data can be saved to POST and FEED
            }

            override fun onError(exception: Exception) {
            }
        })
    }

    private fun storeFeedToDatabase(posts: Posts) {
        val dialog = Dialog(requireContext())
        val databaseManager = DatabaseManager()
        databaseManager.storeFeeds(posts, object : DBPostHandler {
            override fun onSuccess(posts: Posts) {
                dismissLoading(dialog)
                resetAll()
                listener!!.scrollToHome()
            }

            override fun onError(exception: Exception) {

            }
        })
    }

    private fun resetAll() {
        allPhotos.clear()
        editTextCaption.text.clear()
        pickedPhoto = null
        frameLayoutPhoto.visibility = View.GONE
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentTime(): String {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm")
        return simpleDateFormat.format(Date())
    }
}