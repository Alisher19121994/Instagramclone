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
import com.example.instagram.fragment.menu.FavoriteFragment
import com.example.instagram.model.Posts
import com.example.instagram.network.authManager.AuthManager
import com.google.android.material.imageview.ShapeableImageView


class FavoriteAdapter(
    var favoriteFragment: FavoriteFragment,
    private var listOfPosts: ArrayList<Posts>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.favorite_view, parent, false)
        return PostsViewHolder(view)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val posts: Posts = listOfPosts[position]

        if (holder is PostsViewHolder) {


            holder.fullname.text = posts.fullname
            holder.time.text = posts.currentDate
            holder.caption.text = posts.caption


            holder.liked.setOnClickListener {
                if (posts.isLiked) {

                    posts.isLiked = false
                    holder.liked.setImageResource(R.drawable.ic_baseline_favorite_like_24)

                } else {
                    posts.isLiked = true
                    holder.liked.setImageResource(R.drawable.ic_baseline_favorite_red_24)
                }
                favoriteFragment.likeOrUnlikePost(posts)
            }

            val authManager = AuthManager()
            val uid = authManager.currentUser()!!.uid

            // ids is the same...???
            if (uid == posts.uid){

                holder.more.visibility = View.VISIBLE

            }else{

                holder.more.visibility = View.GONE

            }

            holder.more.setOnClickListener {
                favoriteFragment.showDeleteDialog(posts)
            }


            Glide.with(favoriteFragment).load(posts.userImage)
                .placeholder(R.drawable.defaultimage)
                .error(R.drawable.defaultimage)
                .into(holder.profile)

            Glide.with(favoriteFragment).load(posts.image).into(holder.post)
        }
    }

    override fun getItemCount(): Int {
        return listOfPosts.size
    }

    class PostsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var profile: ShapeableImageView = view.findViewById(R.id.favorite_image_round_id)
        var post: ShapeableImageView = view.findViewById(R.id.favorite_view_full_image_id)
        var fullname: TextView = view.findViewById(R.id.favorite_view_fullname_id)
        var time: TextView = view.findViewById(R.id.favorite_view_time_id)
        var caption: TextView = view.findViewById(R.id.favorite_view_et_caption_id)
        var more: ImageView = view.findViewById(R.id.favorite_view_more_id)
        var liked: ImageView = view.findViewById(R.id.favorite_like_id)
        var share: ImageView = view.findViewById(R.id.favorite_share_id)

    }
}