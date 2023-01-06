package com.example.instagram.fragment.menu

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagram.R
import com.example.instagram.adapter.SearchAdapter
import com.example.instagram.model.User
import com.example.instagram.network.authManager.AuthManager
import com.example.instagram.network.databaseManager.DBFollowHandler
import com.example.instagram.network.databaseManager.DBUserHandler
import com.example.instagram.network.databaseManager.DBUsersHandler
import com.example.instagram.network.databaseManager.DatabaseManager
import kotlinx.android.synthetic.main.fragment_search.view.*
import java.util.*


/**
 * In SearchFragment,user can search and follow or unfollow friends
 */
class SearchFragment : BaseFragment() {
    private var serverData = ArrayList<User>()
    var searchableLocalData = ArrayList<User>()
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {

        recyclerView = view.findViewById(R.id.recyclerview_search_id)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        searchItems(view)
    }

    private fun searchItems(view: View) {
        view.search_main_id.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val keyWord = s.toString().trim()
                usersByKeyWord(keyWord)
            }
        })
        loadData()
    }

    fun usersByKeyWord(keyWord: String) {
        if (keyWord.isEmpty()) refreshAdapter(serverData)

        searchableLocalData.clear()

        for (user in serverData)
            if (user.fullname.toLowerCase(Locale.ROOT).startsWith(keyWord.lowercase())
                || user.emailAddress.toLowerCase(Locale.ROOT).startsWith(keyWord.lowercase())
            )

                searchableLocalData.add(user)

        refreshAdapter(searchableLocalData)
    }

    private fun refreshAdapter(users: ArrayList<User>) {
        val adapter = SearchAdapter(this, users)
        recyclerView.adapter = adapter
    }

    private fun loadData() {
        val dialog = Dialog(requireContext())
        showLoading(dialog)

        val authManager = AuthManager()
        val uid = authManager.currentUser()!!.uid

        val databaseManager = DatabaseManager()

        // getting my data
        databaseManager.loadUsers(object : DBUsersHandler {

            override fun onSuccess(user: ArrayList<User>) {

                val getDatabaseManager = DatabaseManager()
                getDatabaseManager.loadFollowing(uid,object : DBUsersHandler{

                    // my loadFollowing
                    override fun onSuccess(followings: ArrayList<User>) {
                        dismissLoading(dialog)
                        serverData.clear()
                        serverData.addAll(mergedUser(uid, user, followings))
                        refreshAdapter(serverData)

                    }

                    override fun onError(exception: Exception) {

                    }
                })
            }

            override fun onError(exception: Exception) {
                dismissLoading(dialog)
            }
        })

    }
    private fun mergedUser(uid: String, user: ArrayList<User>, following: ArrayList<User>): ArrayList<User> {

        val items = ArrayList<User>()

        for (indexOfUser in user){

            for (indexOfFollowing in following){

                if (indexOfUser.uid == indexOfFollowing.uid) {
                    indexOfUser.isFollowed = true
                    break
                }
            }
            if (uid != indexOfUser.uid){
                items.add(indexOfUser)
            }
        }
        return items
    }

    fun followOrUnfollow(to: User) {

        val authManager = AuthManager()
        val uid = authManager.currentUser()!!.uid

        if (!to.isFollowed) {

            followUser(uid, to)
        } else {
            unFollowUser(uid, to)
        }
    }

    private fun followUser(uid: String, to: User) {

        val databaseManager = DatabaseManager()

        // getting my data
        databaseManager.loadUser(uid, object : DBUserHandler {

            override fun onSuccess(me: User?) {
                val getDatabaseManager = DatabaseManager()

                // do something
                getDatabaseManager.followUser(me!!, to, object : DBFollowHandler {

                    override fun onSuccess(isDone: Boolean) {
                        // store posts to my feeds
                        to.isFollowed = true
                        val getDatabaseManagerS = DatabaseManager()
                 getDatabaseManagerS.storePostsToMyFeed(uid,to)

                    }

                    override fun onError(exception: Exception) {

                    }
                })
            }

            override fun onError(exception: Exception) {

            }
        })
    }

    private fun unFollowUser(uid: String, to: User) {
        val databaseManager = DatabaseManager()

        // getting user at first
        databaseManager.loadUser(uid, object : DBUserHandler {

            override fun onSuccess(me: User?) {
                val getDatabaseManager = DatabaseManager()

                getDatabaseManager.unFollowUser(me!!, to, object : DBFollowHandler {

                    override fun onSuccess(isDone: Boolean) {
                        to.isFollowed = false

                        // remove posts to my feeds

                        val getDatabaseManagerT = DatabaseManager()
                        getDatabaseManagerT.removePostsToMyFeed(uid,to)
                    }

                    override fun onError(exception: Exception) {

                    }
                })
            }

            override fun onError(exception: Exception) {

            }
        })

    }
}