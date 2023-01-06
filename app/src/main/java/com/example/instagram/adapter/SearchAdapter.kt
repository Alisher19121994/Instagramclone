package com.example.instagram.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagram.R
import com.example.instagram.fragment.menu.SearchFragment
import com.example.instagram.model.User
import com.google.android.material.imageview.ShapeableImageView

class SearchAdapter(var searchFragment: SearchFragment, var listOfPosts: ArrayList<User>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.search_view, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val user: User = listOfPosts[position]
        if (holder is UserViewHolder) {

            holder.fullname.text = user.fullname
            holder.emailAddress.text = user.emailAddress
            holder.follow.setOnClickListener {

                if (!user.isFollowed) {
                    holder.follow.text = searchFragment.getString(R.string.following)
                } else {
                    holder.follow.text = searchFragment.getString(R.string.follow)
                }
                searchFragment.followOrUnfollow(user)
            }

            if (!user.isFollowed) {
                holder.follow.text = searchFragment.getString(R.string.follow)
            } else {
                holder.follow.text = searchFragment.getString(R.string.following)

            }

            Glide.with(searchFragment).load(user.userImage)
                .placeholder(R.drawable.defaultimage)
                .error(R.drawable.defaultimage)
                .into(holder.profile)
        }
    }

    override fun getItemCount(): Int {
        return listOfPosts.size
    }

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var profile: ShapeableImageView
        var fullname: TextView
        var emailAddress: TextView
        var follow: TextView

        init {
            profile = view.findViewById(R.id.search_profile_image_id)
            fullname = view.findViewById(R.id.search_fullname_id)
            emailAddress = view.findViewById(R.id.search_email_id)
            follow = view.findViewById(R.id.search_follow_id)
        }
    }
}


