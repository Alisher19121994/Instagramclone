package com.example.instagram.fragment.menu

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagram.R
import com.example.instagram.adapter.FavoriteAdapter
import com.example.instagram.model.Posts
import com.example.instagram.network.authManager.AuthManager
import com.example.instagram.network.databaseManager.DBPostHandler
import com.example.instagram.network.databaseManager.DBPostsHandler
import com.example.instagram.network.databaseManager.DatabaseManager
import com.example.instagram.utils.DialogListener
import com.example.instagram.utils.Extension
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.android.synthetic.main.fragment_favorite.view.*


/**
 * In FavoriteFragment,user can check his/her liked posts
 */
class FavoriteFragment : BaseFragment() {

    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView_favorite_id)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        loadLikedFeeds()

    }

    private fun loadLikedFeeds() {
        val dialog = Dialog(requireContext())
        showLoading(dialog)

        val authManager = AuthManager()
        val uid = authManager.currentUser()!!.uid

        val databaseManager = DatabaseManager()
        databaseManager.loadLikedFeeds(uid, object : DBPostsHandler {

            override fun onSuccess(posts: ArrayList<Posts>) {
                dismissLoading(dialog)
                refreshAdapter(posts)
            }

            override fun onError(exception: Exception) {
                dismissLoading(dialog)
            }
        })
    }

    fun likeOrUnlikePost(posts: Posts) {
        val authManager = AuthManager()
        val uid = authManager.currentUser()!!.uid
        val databaseManager = DatabaseManager()

        databaseManager.likeFeedPost(uid, posts)

        loadLikedFeeds()
    }

    fun showDeleteDialog(posts: Posts) {
        Extension.dialogDeleteDouble(
            requireContext(),
            getString(R.string.delete),
            object : DialogListener {
                override fun onCallback(isDone: Boolean) {
                    if (isDone)
                        deletePost(posts)
                }
            })
    }
    fun deletePost(posts: Posts) {
        val databaseManager = DatabaseManager()
        databaseManager.deletePost(posts,object : DBPostHandler {

            override fun onSuccess(posts: Posts) {

                loadLikedFeeds()
            }

            override fun onError(exception: Exception) {

            }
        })
    }


    private fun refreshAdapter(posts: ArrayList<Posts>) {
        val adapter = FavoriteAdapter(this, posts)
        recyclerView.adapter = adapter
    }



}