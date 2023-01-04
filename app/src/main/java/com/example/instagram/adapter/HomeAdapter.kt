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

            holder.fullname.text = posts.fullname
            holder.time.text = posts.currentDate
            holder.caption.text = posts.caption


            Glide.with(homeFragment!!).load(posts.userImage)
                .placeholder(R.drawable.defaultimage)
                .error(R.drawable.defaultimage)
                .into(holder.profile)

            Glide.with(homeFragment!!).load(posts.image).into(holder.post)
        }
    }

    override fun getItemCount(): Int {
        return listOfPosts.size
    }

    class PostsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var profile: ShapeableImageView = view.findViewById(R.id.home_image_round_id)
        var post: ShapeableImageView = view.findViewById(R.id.home_view_full_image_id)

        var fullname: TextView = view.findViewById(R.id.home__view_fullname_id)
        var time: TextView = view.findViewById(R.id.home__view_time_id)
        var caption: TextView = view.findViewById(R.id.home_view_et_caption_id)
        var more: ImageView = view.findViewById(R.id.home_view_more_id)

       // var share: ImageView = view.findViewById(R.id.iv_share_id)
       // var liked: ImageView = view.findViewById(R.id.home_view_more_id)



    }
}