package ph.com.onlyfriends.fragments.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import ph.com.onlyfriends.AddPostActivity
import ph.com.onlyfriends.R
import ph.com.onlyfriends.models.Collections
import ph.com.onlyfriends.models.Post

class PostFragment : Fragment() {

    private lateinit var fab: FloatingActionButton
    private lateinit var pb: ProgressBar
    private lateinit var rvPosts: RecyclerView
    private lateinit var postList: ArrayList<Post>

    // Firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseDatabase // Firebase Realtime Database
    private lateinit var user: FirebaseUser

    private var flag: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_post, container, false)

        initializeComponents(view)

        return view
    }

    override fun onResume() {
        super.onResume()
        if(flag) {
            db.reference.child(Collections.Friends.name).child(user.uid).addListenerForSingleValueEvent(object:
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val whitelisted: ArrayList<String> = ArrayList()
                    whitelisted.add(user.uid)

                    if (Integer.parseInt(snapshot.child("numFollowing").value.toString()) > 0) {
                        val followList: Map<*, *> = snapshot.child("following").value as Map<*, *>

                        for (following in followList) {
                            whitelisted.add(following.key.toString())
                        }
                    }

                    loadPosts(postList, whitelisted)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Error", error.toString())
                }
            })
        }
    }

    private fun initializeComponents(view: View) {

        pb = view.findViewById(R.id.pb_fragment_post)
        db = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        user = FirebaseAuth.getInstance().currentUser!!

        fab = view.findViewById(R.id.fab_add_post)
        fab.setOnClickListener {
            val intent = Intent(activity, AddPostActivity::class.java)
            activity?.startActivity(intent)
            this.flag = true
        }

        // recycler view and adapter
        rvPosts = view.findViewById(R.id.rv_posts)
        rvPosts.layoutManager = LinearLayoutManager(this.context)
        postList = arrayListOf()

        // show new posts first
        (rvPosts.layoutManager as LinearLayoutManager).stackFromEnd = true
        (rvPosts.layoutManager as LinearLayoutManager).reverseLayout = true

        db.reference.child(Collections.Friends.name).child(user.uid).addListenerForSingleValueEvent(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val whitelisted: ArrayList<String> = ArrayList()
                whitelisted.add(user.uid)

                if (Integer.parseInt(snapshot.child("numFollowing").value.toString()) > 0) {
                    val followList: Map<*, *> = snapshot.child("following").value as Map<*, *>

                    for (following in followList) {
                        whitelisted.add(following.key.toString())
                    }
                }

                loadPosts(postList, whitelisted)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", error.toString())
            }
        })
    }

    private fun loadPosts(postList: ArrayList<Post>, whitelisted: ArrayList<String>) {
        // path of posts
        val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Posts")

        postList.clear()

        dbRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds: DataSnapshot in snapshot.children) {
                    for (whitelist in whitelisted) {
                        if (ds.child("uid").value.toString() == whitelist) {
                            val name = ds.child("uname").value.toString()
                            val handle = ds.child("uhandle").value.toString()
                            val content = ds.child("pcontent").value.toString()
                            postList.add(Post(name, handle, content))
                        }
                    }
                }
                rvPosts.adapter = AdapterPosts(postList)
                rvPosts.adapter?.notifyDataSetChanged()
                pb.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                // in case there is an error
                Toast.makeText(activity, ""+error.message, Toast.LENGTH_SHORT).show()
            }
        })

    }
}