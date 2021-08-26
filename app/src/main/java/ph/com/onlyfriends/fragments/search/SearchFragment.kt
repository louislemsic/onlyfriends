package ph.com.onlyfriends.fragments.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        dbFriendDatabase = FirebaseDatabase.getInstance().getReference(Collections.Friends.name)
        initRecyclerView(view)

        return view
    }

    private fun initRecyclerView(view: View) {

        this.rvUserSearchList = view.findViewById(R.id.rv_search_users)
        this.lmUserSearchManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        this.rvUserSearchList.layoutManager = this.lmUserSearchManager

        this.rvUserSearchList.adapter = initFirebaseAdapter()
    }

    private fun initFirebaseAdapter(): FirebaseRecyclerAdapter<Friend, SearchViewHolder> {
        val firebaseSearchQuery: Query = dbFriendDatabase.orderByChild("name")

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