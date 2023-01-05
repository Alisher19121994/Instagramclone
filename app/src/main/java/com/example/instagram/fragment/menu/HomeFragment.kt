package com.example.instagram.fragment.menu


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagram.R
import com.example.instagram.adapter.HomeAdapter
import com.example.instagram.model.Posts
import com.example.instagram.network.authManager.AuthManager
import com.example.instagram.network.connections.HomeListener
import com.example.instagram.network.databaseManager.DBPostsHandler
import com.example.instagram.network.databaseManager.DatabaseManager
import kotlinx.android.synthetic.main.fragment_home.view.*


/**
 * In this HomeFragment,user can check his/her posts and friends posts
 */
class HomeFragment : BaseFragment() {

    private var listener: HomeListener? = null
    lateinit var recyclerView: RecyclerView
    var feeds = ArrayList<Posts>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        initViews(view)
        return view
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {

        if (isVisibleToUser && feeds.size > 0){
            loadMyFeeds()
        }
    }

    private fun initViews(view: View) {

        recyclerView = view.findViewById(R.id.recyclerview_home_id)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        view.home_camera_id.setOnClickListener { listener?.scrollToUpload() }

        loadMyFeeds()
    }

    private fun loadMyFeeds() {

        val dialog = Dialog(requireContext())
        showLoading(dialog)

        val authManager = AuthManager()
        val uid = authManager.currentUser()!!.uid

        val databaseManager = DatabaseManager()
        databaseManager.loadFeeds(uid, object : DBPostsHandler {

            override fun onSuccess(posts: ArrayList<Posts>) {

                dismissLoading(dialog)
                feeds.clear()
                feeds.addAll(posts)
                refreshAdapter(feeds)

            }

            override fun onError(exception: Exception) {
                dismissLoading(dialog)
            }
        })

    }


    private fun refreshAdapter(posts: ArrayList<Posts>) {
        val adapter = HomeAdapter(activity, posts)
        recyclerView.adapter = adapter
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = if (context is HomeListener) {
            context
        } else {
            throw RuntimeException(context.toString() + "must implement upload!")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}