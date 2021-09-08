package ph.com.onlyfriends.fragments.search

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import ph.com.onlyfriends.R
import ph.com.onlyfriends.models.Collections
import ph.com.onlyfriends.models.Friend

class SearchFragment : Fragment() {

    private lateinit var rvUserSearchList: RecyclerView
    private lateinit var lmUserSearchManager: RecyclerView.LayoutManager
    private lateinit var dbFriendDatabase: DatabaseReference
    private lateinit var svSearchBar: SearchView

    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        dbFriendDatabase = FirebaseDatabase.getInstance().getReference(Collections.Friends.name)
        initComponents(view)

        return view
    }

    private fun initComponents(view: View) {
        initRecyclerView(view)

        this.svSearchBar = view.findViewById(R.id.sv_search_bar)

        this.svSearchBar.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.i(TAG,svSearchBar.query.toString())
                rvUserSearchList.adapter = initFirebaseAdapter(svSearchBar.query.toString())
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })
    }

    private fun initRecyclerView(view: View) {
        this.rvUserSearchList = view.findViewById(R.id.rv_search_users)
        this.lmUserSearchManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        this.rvUserSearchList.layoutManager = this.lmUserSearchManager
    }

    private fun initFirebaseAdapter(searchQuery: String): FirebaseRecyclerAdapter<Friend, SearchViewHolder> {
        val firebaseSearchQuery: Query = dbFriendDatabase.orderByChild("name")
            .startAt(searchQuery)
            .endAt(searchQuery + "\uf8ff")

        val options = FirebaseRecyclerOptions.Builder<Friend>()
            .setQuery(firebaseSearchQuery, Friend::class.java)
            .setLifecycleOwner(this)
            .build()

        return object : FirebaseRecyclerAdapter<Friend, SearchViewHolder>(options) {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): SearchViewHolder {
                return SearchViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.template_search, parent, false)
                )
            }

            override fun onBindViewHolder(
                holder: SearchViewHolder,
                position: Int,
                model: Friend
            ) {
                holder.bind(model)
            }
        }
    }
}