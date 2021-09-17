package ph.com.onlyfriends.fragments.chat

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import ph.com.onlyfriends.AddPostActivity
import ph.com.onlyfriends.R
import ph.com.onlyfriends.models.Post

class PostFragment : Fragment() {

    private lateinit var fab: FloatingActionButton
    private lateinit var rvPosts: RecyclerView
    private lateinit var postList: ArrayList<Post>

    // Firebase
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_post, container, false)

        initializeComponents(view)

        return view
    }

    @SuppressLint("ResourceType")
    private fun initializeComponents(view: View) {

        firebaseAuth = FirebaseAuth.getInstance()
        fab = view.findViewById(R.id.fab_add_post)
        fab.setOnClickListener {
            val intent = Intent(activity, AddPostActivity::class.java)
            activity?.startActivity(intent)
        }

        // recycler view and adapter
        rvPosts = view.findViewById(R.id.rv_posts)
        rvPosts.layoutManager = LinearLayoutManager(this.context)
        postList = arrayListOf()

        // show new posts first
        (rvPosts.layoutManager as LinearLayoutManager).stackFromEnd = true
        (rvPosts.layoutManager as LinearLayoutManager).reverseLayout = true


        loadPosts(postList)
    }

    private fun loadPosts(postList: ArrayList<Post>) {
        // path of posts
        val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Posts")

        // get all data from the ref
        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                postList.clear()

                for (ds: DataSnapshot in snapshot.children) {
                    val modelPost: Post? = ds.getValue(Post::class.java)

                    if (modelPost != null) {
                        postList.add(modelPost)
                    }
                    rvPosts.adapter = AdapterPosts(postList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // in case there is an error
                Toast.makeText(activity, ""+error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

}