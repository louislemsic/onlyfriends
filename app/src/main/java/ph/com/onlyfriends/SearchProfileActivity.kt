package ph.com.onlyfriends

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import ph.com.onlyfriends.fragments.chat.AdapterPosts
import ph.com.onlyfriends.models.Collections
import ph.com.onlyfriends.models.Post
import ph.com.onlyfriends.models.notifs.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchProfileActivity : AppCompatActivity() {

    private var user: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
    private var db: FirebaseDatabase = FirebaseDatabase.getInstance()

    private lateinit var tvName: TextView
    private lateinit var tvHandle: TextView
    private lateinit var tvFollowerCount: TextView
    private lateinit var btnFollow: Button

    private lateinit var rvPosts: RecyclerView
    private lateinit var postList: ArrayList<Post>

    private lateinit var name: String
    private lateinit var handle: String
    private lateinit var userHandle: String
    private var isFollowed: Boolean = false

    private lateinit var followUid: String

    private var apiService = RetroFitClient
        .getClient("https://fcm.googleapis.com/")
        .create(APIService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_profile)

        this.tvFollowerCount = findViewById(R.id.tv_search_followers)
        this.tvName = findViewById(R.id.tv_profile_name)
        this.tvHandle = findViewById(R.id.tv_profile_handle)
        this.btnFollow = findViewById(R.id.btn_profile_follow)

        getIntentData()
        getUserProfile()
        initRecyclerView()
        loadPosts(postList)
    }

    private fun loadPosts(postList: ArrayList<Post>) {
        // path of posts
        val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Posts")

        postList.clear()

        dbRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (ds: DataSnapshot in snapshot.children) {

                    if (ds.child("uid").value.toString() ==  followUid) {
                        val content = ds.child("pcontent").value.toString()
                        postList.add(Post(name, handle, content))
                    }

                }
                rvPosts.adapter = AdapterPosts(postList)

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }

    private fun initRecyclerView() {
        // recycler view and adapter
        rvPosts = findViewById(R.id.rv_search_profile)
        rvPosts.layoutManager = LinearLayoutManager(this)
        postList = arrayListOf()
    }

    private fun getIntentData() {
        //Getting Data from Intent
        this.name = intent.getStringExtra(Keys.NAME.name).toString()
        this.handle = intent.getStringExtra(Keys.HANDLE.name).toString()
        this.isFollowed = intent.getStringExtra(Keys.ISFOLLOWED.name).toString() == "true"

        //Setting Intent Data
        this.tvName.text = name
        this.tvHandle.text = handle
        this.btnFollow.text = if(isFollowed) "unfollow" else "follow"
    }

    private fun getUserProfile() {

        db.reference.child(Collections.Friends.name).addListenerForSingleValueEvent(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (friend in snapshot.children) {
                    if (friend.child("name").value.toString() == name) {
                        if(friend.key.toString() == user.uid)
                            userHandle = friend.child("handle").value.toString()
                        followUid = friend.key.toString()
                        tvFollowerCount.text = friend.child("numFollowers").value.toString()
                        initListeners()
                        updateButton()
                    }

                    else if(friend.key.toString() == user.uid)
                        userHandle = friend.child("handle").value.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", error.toString())
            }
        })

    }

    private fun updateFollowersCount() {
        db.reference.child(Collections.Friends.name).child(followUid).addListenerForSingleValueEvent(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tvFollowerCount.text = snapshot.child("numFollowers").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", error.toString())
            }
        })
    }

    private fun initListeners() {
        this.btnFollow.setOnClickListener {
            if(btnFollow.text.toString() == "follow")
                pushFollowing()
            else
                popFollowing()
        }
    }

    private fun updateButton() {
        db.reference.child(Collections.Friends.name)
            .child(user.uid).child("following").addListenerForSingleValueEvent(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var isFollowing = false

                for (following in snapshot.children)
                    if(following.key.toString() == followUid)
                        isFollowing = true

                btnFollow.text = if(isFollowing) "unfollow" else "follow"
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", error.toString())
            }
        })
    }

    private fun pushFollowing() {
        val uid = user.uid
        val ref = db.getReference("Friends/$uid")

        val map: MutableMap<String, Boolean> = HashMap()
        map[followUid] = true

        ref.child("following").updateChildren(map as Map<String, Boolean>).addOnCompleteListener {
            if(it.isSuccessful) {
                buildNotification()
                updateFollowingCount(1)
            }
            else {
                Log.d("increment", "failed")
            }
        }


    }

    private fun popFollowing() {
        val uid = user.uid

        db.getReference("Friends/$uid/following").child(followUid).setValue(null).addOnCompleteListener {
            if(it.isSuccessful) {
                updateFollowingCount(-1)
            }
            else {
                Log.d("increment", "failed")
            }
        }
    }

    private fun updateFollowingCount(inc: Int) {
        db.reference.child(Collections.Friends.name)
            .child(user.uid).child("following").addValueEventListener(object:
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val uid = user.uid
                    db.getReference("Friends/$uid/numFollowing").setValue(snapshot.childrenCount).addOnCompleteListener {
                        if(it.isSuccessful)
                            updateFollowerCount(inc)
                        else
                            Log.d("updateFollowingCount", "error")
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Error", error.toString())
                }
            })
    }

    private fun updateFollowerCount(inc: Int) {
        db.reference.child(Collections.Friends.name).child(followUid).addListenerForSingleValueEvent(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var numFollowers = snapshot.child("numFollowers").value as Long
                numFollowers += inc
                db.reference.child(Collections.Friends.name).child(followUid).child("numFollowers").setValue(numFollowers)
                updateButton()
                updateFollowersCount()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", error.toString())
            }
        })
    }

    private fun buildNotification() {
        FirebaseDatabase.getInstance().getReference()
            .child("Tokens")
            .child(followUid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val usertoken = dataSnapshot.child("token").value.toString()
                sendNotification(usertoken, "New Follower", "$userHandle followed you!")
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun sendNotification(token: String, type: String, msg: String) {
        val data = Data(type, msg)
        val sender = NotificationSender(data, token)

        apiService.sendNotifcation(sender)!!.enqueue(object : Callback<MyResponse?> {

            override fun onResponse(call: Call<MyResponse?>, response: Response<MyResponse?>) {
                if (response.code() === 200) {
                    if (response.body()!!.success !== 1) {

                    }
                }
            }

            override fun onFailure(call: Call<MyResponse?>, t: Throwable?) {

            }
        })
    }
}