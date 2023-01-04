package com.example.instagram.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagram.R
import com.example.instagram.model.Posts
import com.google.android.material.imageview.ShapeableImageView

class ProfileAdapter(var profileFragment: FragmentActivity, var listOfPosts: ArrayList<Posts>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.profile_view, parent, false)
        return PostsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val posts = listOfPosts[position]
        if (holder is PostsViewHolder) {

            // ((PostsViewHolder) holder).caption.setText(posts.getCaption());
            val post = holder.post
            Glide.with(profileFragment).load(posts.image).into(post)
        }
    }

    override fun getItemCount(): Int {
        return listOfPosts.size
    }

    class PostsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var post: ShapeableImageView
        var caption: TextView

        init {
            post = view.findViewById(R.id.profile_post_image_id)
            caption = view.findViewById(R.id.profile_post_caption_id)
        }
    }
}