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
import com.example.instagram.network.databaseManager.DBUsersHandler
import com.example.instagram.network.databaseManager.DatabaseManager
import kotlinx.android.synthetic.main.fragment_search.view.*
import java.util.*
import kotlin.collections.ArrayList


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
        val adapter = SearchAdapter(activity, users)
        recyclerView.adapter = adapter
    }

    private fun loadData() {
        val dialog = Dialog(requireContext())
        showLoading(dialog)
        val databaseManager = DatabaseManager()
        databaseManager.loadUsers(object : DBUsersHandler {

            override fun onSuccess(user: ArrayList<User>) {
                dismissLoading(dialog)
                serverData.clear()
                serverData.addAll(user)
                refreshAdapter(serverData)
            }

            override fun onError(exception: Exception) {
                dismissLoading(dialog)
            }
        })

    }
}