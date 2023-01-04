package com.example.instagram.fragment.menu


import android.app.Activity
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
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.instagram.R
import com.example.instagram.network.connections.UploadListener
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import kotlinx.android.synthetic.main.fragment_upload.view.*


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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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

    private var photoLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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
            listener?.scrollToHome()
            editTextCaption.text.clear()
        }
    }
}