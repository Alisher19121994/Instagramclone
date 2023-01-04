package com.example.instagram.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagram.R
import com.example.instagram.model.Posts
import com.google.android.material.imageview.ShapeableImageView

class HomeAdapter(var homeFragment: FragmentActivity?, var listOfPosts: ArrayList<Posts>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.home_view, parent, false)
        return PostsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val posts: Posts = listOfPosts[position]
        if (holder is PostsViewHolder) {
            val post = holder.post
            Glide.with(homeFragment!!).load(posts.image).into(post)
        }
    }

    override fun getItemCount(): Int {
        return listOfPosts.size
    }

    class PostsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var profile: ShapeableImageView? = null
        var post: ShapeableImageView = view.findViewById(R.id.home_view_full_image_id)
        var fullname: TextView? = null
        var time: TextView? = null
        var caption: TextView? = null
        var more: ImageView? = null
        var liked: ImageView? = null
        var share: ImageView? = null


    }
}