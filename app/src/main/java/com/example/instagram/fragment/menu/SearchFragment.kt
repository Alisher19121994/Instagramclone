package com.example.instagram.fragment.menu

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instagram.R
import com.example.instagram.adapter.SearchAdapter
import com.example.instagram.model.User
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import java.util.*


/**
 * In SearchFragment,user can search and follow or unfollow friends
 */
class SearchFragment : BaseFragment() {
    private var serverData = ArrayList<User>()
    var searchableLocalData = ArrayList<User>()

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

        view.recyclerview_search_id.layoutManager = LinearLayoutManager(activity)
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
    }

    fun usersByKeyWord(keyWord: String) {
        if (keyWord.isEmpty()) refreshAdapter(serverData)

        searchableLocalData.clear()

        for (user in serverData)
            if (user.fullname.toLowerCase(Locale.ROOT).startsWith(keyWord.lowercase())
            || user.emailAddress.toLowerCase(Locale.ROOT).startsWith(keyWord.lowercase()))

                searchableLocalData.add(user)

        refreshAdapter(searchableLocalData)
    }

    private fun refreshAdapter(users: ArrayList<User>) {
        val adapter = SearchAdapter(activity, users)
        recyclerview_search_id.adapter = adapter
    }

    fun loadData() {}
}