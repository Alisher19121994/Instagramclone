package com.example.instagram.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.instagram.R
import com.example.instagram.fragment.fragmentAdapter.ViewPagerAdapter
import com.example.instagram.fragment.menu.*
import com.example.instagram.network.connections.HomeListener
import com.example.instagram.network.connections.UploadListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView


/**
 * This MainActivity contains view pager with 5 fragments and pages can be
 * controlled by BottomNavigationView
 */
class MainActivity : BaseActivity(), HomeListener, UploadListener {

    val TAG = MainActivity::class.java.simpleName
    private lateinit var viewPager: ViewPager
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var homeFragment: HomeFragment
    private lateinit var upLoadFragment: UploadFragment
    var index = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    override fun scrollToHome() {
        index = 0
        scrollBYIndex(index)
    }

    override fun scrollToUpload() {
        index = 2
        scrollBYIndex(index)
    }

    private fun scrollBYIndex(index: Int) {
        viewPager.currentItem = index
        bottomNavigationView.menu.getItem(index).isChecked = true
    }

    private fun initViews() {
        viewPager = findViewById(R.id.viewPager_main_id)
        bottomNavigationView = findViewById(R.id.bottomNavigationView_main_id)
        openFragments()
        /***
         * Home and upload Fragments are global for communication purpose!
         */
        homeFragment = HomeFragment()
        upLoadFragment = UploadFragment()
        bottomNavigationView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.home_id -> viewPager.currentItem = 0
                R.id.search_id -> viewPager.currentItem = 1
                R.id.upload_id -> viewPager.currentItem = 2
                R.id.favorite_id -> viewPager.currentItem = 3
                R.id.profile_id -> viewPager.currentItem = 4
            }
            true
        })
        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                index = position
                bottomNavigationView.menu.getItem(index).isChecked = true
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun openFragments() {
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(HomeFragment())
        viewPagerAdapter.addFragment(SearchFragment())
        viewPagerAdapter.addFragment(UploadFragment())
        viewPagerAdapter.addFragment(FavoriteFragment())
        viewPagerAdapter.addFragment(ProfileFragment())
        viewPager.adapter = viewPagerAdapter
    }
}