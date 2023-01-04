package com.example.instagram.fragment.fragmentAdapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class ViewPagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    private var fragments =  ArrayList<Fragment>()

    override fun getItem(position: Int): Fragment {
        return fragments[position]!!
    }

    override fun getCount(): Int {
        return fragments.size
    }

    fun addFragment(fragment: Fragment?) {
        if (fragment != null) {
            fragments.add(fragment)
        }
    }
}