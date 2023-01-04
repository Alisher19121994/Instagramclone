package com.example.instagram.fragment.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagram.R
import com.example.instagram.adapter.FavoriteAdapter
import com.example.instagram.model.Posts
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.android.synthetic.main.fragment_favorite.view.*


/**
 * In FavoriteFragment,user can check his/her liked posts
 */
class FavoriteFragment : BaseFragment() {

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
        view.recyclerView_favorite_id.layoutManager = LinearLayoutManager(activity)

        refreshAdapter(view,loadData())
    }

    private fun refreshAdapter(view: View,posts: ArrayList<Posts>) {
        val adapter = FavoriteAdapter(requireActivity(), posts)
        view.recyclerView_favorite_id.adapter = adapter
    }

    private fun loadData(): ArrayList<Posts> {
        val list: ArrayList<Posts> = ArrayList()
        list.add(Posts("https://manchestersightseeingtours.com/wp-content/uploads/Manchester-United-Football-Ground-NCNManchester-Marketing-1-525x350.jpg"))
        list.add(Posts("https://www.si.com/.image/c_limit%2Ccs_srgb%2Cq_auto:good%2Cw_700/MTkxMTk2ODg3MjA5MzU0NDc1/imago1011329909h.webp"))
        list.add(Posts("https://i2-prod.manchestereveningnews.co.uk/incoming/article23387908.ece/ALTERNATES/s810/0_GettyImages-1384570282.jpg"))
        list.add(Posts("https://i2-prod.football.london/incoming/article23788990.ece/ALTERNATES/s458/0_Marcus-Rashford.jpg"))
        list.add(Posts("https://i2-prod.manchestereveningnews.co.uk/sport/football/football-news/article23692634.ece/ALTERNATES/s615b/0_GettyImages-1313311867.jpg"))
        return list
    }
}