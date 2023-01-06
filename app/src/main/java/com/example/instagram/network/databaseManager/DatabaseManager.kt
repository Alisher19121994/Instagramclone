package com.example.instagram.network.databaseManager

import android.util.Log
import com.example.instagram.model.Posts
import com.example.instagram.model.User
import com.example.instagram.network.authManager.AuthManager
import com.google.firebase.firestore.FirebaseFirestore


class DatabaseManager {
    private var USER_PATH = "users"
    private var POST_PATH = "posts"
    private var FEED_PATH = "feeds"
    private var FOLLOWING_PATH = "following"
    private var FOLLOWERS_PATH = "followers"

    private var database: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun storePost(posts: Posts, dbPostHandler: DBPostHandler) {

        val reference = database.collection(USER_PATH).document(posts.uid).collection(POST_PATH)
        val id = reference.document().id
        posts.id = id

        reference.document(posts.id).set(posts).addOnSuccessListener {
            dbPostHandler.onSuccess(posts)

        }.addOnFailureListener {
            dbPostHandler.onError(it)
        }
    }

    fun storeFeeds(posts: Posts, dbPostHandler: DBPostHandler) {

        val reference = database.collection(USER_PATH).document(posts.uid).collection(FEED_PATH)
        val id = reference.document().id
        posts.id = id

        reference.document(posts.id).set(posts).addOnSuccessListener {
            dbPostHandler.onSuccess(posts)

        }.addOnFailureListener {
            dbPostHandler.onError(it)
        }
    }

    fun storeUser(user: User, dbUserHandler: DBUserHandler) {
        database.collection(USER_PATH).document(user.uid).set(user).addOnSuccessListener {

            dbUserHandler.onSuccess()

        }.addOnFailureListener { e ->
            dbUserHandler.onError(e)
        }
    }

    fun loadUser(uid: String, dbUserHandler: DBUserHandler) {

        database.collection(USER_PATH).document(uid).get().addOnSuccessListener {
            if (it.exists()) {

                val fullname: String? = it.getString("fullname")
                val email: String? = it.getString("email")
                val userImage: String? = it.getString("userImage")

                val user = User(fullname.toString(), email.toString(), userImage.toString())
                //val user = User(fullname!!, email!!, userImage!!)
                user.uid = uid ///   connection
                dbUserHandler.onSuccess(user)
            } else {
                dbUserHandler.onSuccess(null)
            }
        }.addOnFailureListener { e ->
            dbUserHandler.onError(e)
        }
    }

    fun updateUserImage(imageUri: String) {
        val authManager = AuthManager()
        database.collection(USER_PATH).document(authManager.currentUser()!!.uid)
            .update("userImage", imageUri)
    }

    // for search
    fun loadUsers(dbUsersHandler: DBUsersHandler) {
        database.collection(USER_PATH).get().addOnCompleteListener { task ->

            val usersList = ArrayList<User>()

            if (task.isSuccessful) {
                for (document in task.result) {

                    val uid = document.getString("uid")
                    val fullname = document.getString("fullname")
                    val email = document.getString("email")
                    val userImage = document.getString("userImage")

                    //  val user = User(fullname!!, email!!, userImage!!)
                    val user = User(fullname.toString(), email.toString(), userImage.toString())
                    user.uid = uid!!
                    usersList.add(user)
                }
                dbUsersHandler.onSuccess(usersList)
            } else {
                dbUsersHandler.onError(task.exception!!)
            }
        }
    }

    fun loadFeeds(uid: String, dbPostsHandler: DBPostsHandler) {
        val reference = database.collection(USER_PATH).document(uid).collection(FEED_PATH)
        reference.get().addOnCompleteListener {

            val postsList = ArrayList<Posts>()
            if (it.isSuccessful) {
                for (document in it.result) {

                    val id = document.getString("id")
                    val userid = document.getString("userid")
                    val caption = document.getString("caption")
                    val userImage = document.getString("userImage")
                    val fullname = document.getString("fullname")
                    val postImage = document.getString("postImage")
                    val currentDate = document.getString("currentDate")
                    var isLiked = document.getBoolean("isLiked")
                    if (isLiked == null) isLiked = false

                    //   val post = Posts(id!!, caption!!, postImage!!)
                    val post = Posts(id.toString(), caption.toString(), postImage.toString())
                    post.uid = userid.toString()
                    post.fullname = fullname.toString()
                    post.userImage = userImage.toString()
                    post.currentDate = currentDate.toString()
                    post.isLiked = isLiked
                    postsList.add(post)

                }
                dbPostsHandler.onSuccess(postsList)

            } else {
                dbPostsHandler.onError(it.exception!!)
            }
        }
    }

    fun loadPosts(uid: String, dbPostsHandler: DBPostsHandler) {
        val reference = database.collection(USER_PATH).document(uid).collection(POST_PATH)
        reference.get().addOnCompleteListener {

            val postsList = ArrayList<Posts>()
            if (it.isSuccessful) {
                for (document in it.result) {

                    val id = document.getString("id")
                    val userid = document.getString("userid")
                    val caption = document.getString("caption")
                    val userImage = document.getString("userImage")
                    val fullname = document.getString("fullname")
                    val postImage = document.getString("postImage")
                    val currentDate = document.getString("currentDate")
                    var isLiked = document.getBoolean("isLiked")
                    if (isLiked == null) isLiked = false

                    //val post = Posts(id!!,caption!!,postImage!!)
                    val post = Posts(id.toString(), caption.toString(), postImage.toString())
                    post.uid = userid.toString()
                    post.fullname = fullname.toString()
                    post.userImage = userImage.toString()
                    post.currentDate = currentDate.toString()
                    post.isLiked = isLiked
                    postsList.add(post)

                }
                dbPostsHandler.onSuccess(postsList)

            } else {
                dbPostsHandler.onError(it.exception!!)
            }
        }
    }

    fun followUser(me: User, to: User, dbFollowHandler: DBFollowHandler) {
        // User(to) is in my following
        database.collection(USER_PATH).document(me.uid).collection(FOLLOWING_PATH).document(to.uid)
            .set(to).addOnSuccessListener {

                // User(me) is in her/his  followers
                database.collection(USER_PATH).document(to.uid).collection(FOLLOWERS_PATH)
                    .document(me.uid).set(me)
                    .addOnSuccessListener {
                        dbFollowHandler.onSuccess(true)
                    }
                    .addOnFailureListener {
                        dbFollowHandler.onError(it)
                    }
            }.addOnFailureListener {
                dbFollowHandler.onError(it)
            }

    }

    fun unFollowUser(me: User, to: User, dbFollowHandler: DBFollowHandler) {
        // User(to) is in my following
        database.collection(USER_PATH).document(me.uid).collection(FOLLOWING_PATH).document(to.uid)
            .delete().addOnSuccessListener {

                // User(me) is in her/his  followers
                database.collection(USER_PATH).document(to.uid).collection(FOLLOWERS_PATH)
                    .document(me.uid).delete()
                    .addOnSuccessListener {
                        dbFollowHandler.onSuccess(true)
                    }
                    .addOnFailureListener {
                        dbFollowHandler.onError(it)
                    }
            }.addOnFailureListener {
                dbFollowHandler.onError(it)
            }

    }

    fun loadFollowing(uid: String, dbUsersHandler: DBUsersHandler) {
        database.collection(USER_PATH).document(uid).collection(FOLLOWING_PATH).get()
            .addOnCompleteListener {

                val usersList = ArrayList<User>()

                if (it.isSuccessful) {
                    for (document in it.result!!) {
                        val uid = document.getString("uid")
                        val fullname = document.getString("fullname")
                        val email = document.getString("email")
                        val userImage = document.getString("userImage")

                        val user = User(fullname.toString(), email.toString(), userImage.toString())
                        user.uid = uid.toString()
                        usersList.add(user)
                    }
                    dbUsersHandler.onSuccess(usersList)
                } else {
                    dbUsersHandler.onError(it.exception!!)
                }

            }
    }

    fun loadFollowers(uid: String, dbUsersHandler: DBUsersHandler) {
        database.collection(USER_PATH).document(uid).collection(FOLLOWERS_PATH).get()
            .addOnCompleteListener {

                val usersList = ArrayList<User>()

                if (it.isSuccessful) {
                    for (document in it.result!!) {
                        val uid = document.getString("uid")
                        val fullname = document.getString("fullname")
                        val email = document.getString("email")
                        val userImage = document.getString("userImage")

                        val user = User(fullname.toString(), email.toString(), userImage.toString())
                        user.uid = uid.toString()
                        usersList.add(user)
                    }
                    dbUsersHandler.onSuccess(usersList)
                } else {
                    dbUsersHandler.onError(it.exception!!)
                }

            }
    }

    fun storePostsToMyFeed(uid: String, to: User) {
        loadPosts(to.uid, object : DBPostsHandler {

            override fun onSuccess(posts: ArrayList<Posts>) {
                for (post in posts) {
                    storeFeed(uid, post)
                }
            }

            override fun onError(exception: Exception) {
            }
        })
    }

    private fun storeFeed(uid: String, posts: Posts) {

        val reference = database.collection(USER_PATH).document(uid).collection(FEED_PATH)
        reference.document(posts.id).set(posts)
    }

    fun removePostsToMyFeed(uid: String, to: User) {
        loadPosts(to.uid, object : DBPostsHandler {
            override fun onSuccess(posts: ArrayList<Posts>) {

                for (post in posts) {
                    removeFeed(uid, post)
                }
            }

            override fun onError(exception: Exception) {

            }
        })
    }

    private fun removeFeed(uid: String, posts: Posts) {

        val reference = database.collection(USER_PATH).document(uid).collection(FEED_PATH)
        reference.document(posts.id).delete()
    }

    fun likeFeedPost(uid: String, posts: Posts) {

        // this is overall method,which is able to like every feeds post
        database.collection(USER_PATH).document(uid).collection(FEED_PATH).document(posts.id)
            .update("isLiked", posts.isLiked)

        // this is my post that I can click my post and show into postFragment
        if (uid == posts.uid)
            database.collection(USER_PATH).document(uid).collection(POST_PATH).document(posts.id)
                .update("isLiked", posts.isLiked)
    }

    fun loadLikedFeeds(uid: String, dbPostsHandler: DBPostsHandler) {

        val reference = database.collection(USER_PATH).document(uid).collection(FEED_PATH)
            .whereEqualTo("isLiked", true)
        // take all likes (with -> "whereEqualTo")

        reference.get().addOnCompleteListener {

            val postsList = ArrayList<Posts>()

            if (it.isSuccessful) {
                for (document in it.result) {

                    val id = document.getString("id")
                    val userid = document.getString("userid")
                    val caption = document.getString("caption")
                    val userImage = document.getString("userImage")
                    val fullname = document.getString("fullname")
                    val postImage = document.getString("postImage")
                    val currentDate = document.getString("currentDate")
                    var isLiked = document.getBoolean("isLiked")
                    if (isLiked == null) isLiked = false

                    //val post = Posts(id!!,caption!!,postImage!!)
                    val post = Posts(id.toString(), caption.toString(), postImage.toString())
                    post.uid = userid.toString()
                    post.fullname = fullname.toString()
                    post.userImage = userImage.toString()
                    post.currentDate = currentDate.toString()
                    post.isLiked = isLiked
                    postsList.add(post)

                }
                dbPostsHandler.onSuccess(postsList)

            } else {
                dbPostsHandler.onError(it.exception!!)
            }
        }
    }

    fun deletePost(posts: Posts, dbPostHandler: DBPostHandler) {

        // removed from POST
        val reference = database.collection(USER_PATH).document(posts.uid).collection(POST_PATH)
        reference.document(posts.id).delete().addOnSuccessListener {

            // if post removed OK  from POST and then did it on FEED too !
            val reference2 =
                database.collection(USER_PATH).document(posts.uid).collection(FEED_PATH)
            reference2.document(posts.id).delete().addOnSuccessListener {

                dbPostHandler.onSuccess(posts)

            }.addOnFailureListener {
                dbPostHandler.onError(it)
            }
        }.addOnFailureListener {
            dbPostHandler.onError(it)
        }

    }
}