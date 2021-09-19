package ph.com.onlyfriends.fragments.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ph.com.onlyfriends.R
import ph.com.onlyfriends.models.Collections


/**
 * A simple [Fragment] subclass.
 * Use the [NotificationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotificationFragment : Fragment() {

    // Components
    private lateinit var rvNotif: RecyclerView
    private lateinit var pbLoad: ProgressBar

    // Data
    private var typeList: MutableList<String> = mutableListOf()
    private var msgList: MutableList<String> = mutableListOf()

    // Firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseDatabase // Firebase Realtime Database
    private lateinit var user: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notification, container, false)

        // Initialize Firebase
        db = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        user = FirebaseAuth.getInstance().currentUser!!

        // Components
        pbLoad = view.findViewById(R.id.pb_fragment_notifs)

        // Recycler View
        initRecyclerView(view)

        return view
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun initRecyclerView(view: View) {
        // recycler view and adapter
        rvNotif = view.findViewById(R.id.rv_notifs)
        rvNotif.layoutManager = LinearLayoutManager(this.context)
    }

    private fun getData() {
        typeList.clear()
        msgList.clear()

        db.reference.child(Collections.Friends.name)
            .child(user.uid)
            .child("notifications")
            .addListenerForSingleValueEvent(object:
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(notif in snapshot.children) {
                        typeList.add("New Follower")
                        msgList.add(notif.value.toString())
                    }
                    rvNotif.adapter = NotifAdapter(typeList, msgList)
                    rvNotif.adapter?.notifyDataSetChanged()
                    pbLoad.visibility = View.GONE
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Error", error.toString())
                }
            })
    }
}