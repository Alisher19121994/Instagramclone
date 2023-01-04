package com.example.instagram.fragment.menu


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instagram.R
import com.example.instagram.adapter.HomeAdapter
import com.example.instagram.model.Posts
import com.example.instagram.network.connections.HomeListener
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*


/**
 * In this HomeFragment,user can check his/her posts and friends posts
 */
class HomeFragment : BaseFragment() {

    private var listener: HomeListener?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {

        view.recyclerview_home_id.layoutManager = LinearLayoutManager(activity)
        setCamera(view)
        refreshAdapter(loadPost())
        countDownTimer()
    }

    fun countDownTimer() {
        val dialog = Dialog(requireContext())
        showLoading(dialog)
        val countDownTimer = object : CountDownTimer(1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                dismissLoading(dialog)
            }
        }.start()
    }

    private fun refreshAdapter(posts: ArrayList<Posts>) {
        val adapter = HomeAdapter(activity, posts)
        recyclerview_home_id.adapter = adapter
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

    private fun setCamera(view: View) {
        view.home_camera_id.setOnClickListener { listener?.scrollToUpload() }
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