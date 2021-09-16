package ph.com.onlyfriends

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ph.com.onlyfriends.models.Collections

class ProfileActivity : AppCompatActivity() {

    private var user: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
    private var db: FirebaseDatabase = FirebaseDatabase.getInstance()

    private lateinit var tvName: TextView
    private lateinit var tvHandle: TextView
    private lateinit var btnFollow: Button

    private lateinit var name: String
    private lateinit var handle: String
    private var isFollowed: Boolean = false

    private lateinit var followUid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        this.tvName = findViewById(R.id.tv_profile_name)
        this.tvHandle = findViewById(R.id.tv_profile_handle)
        this.btnFollow = findViewById(R.id.btn_profile_follow)

        getIntentData()
        getUserProfile()
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

        db.reference.child(Collections.Friends.name).addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (friend in snapshot.children) {
                    if (friend.child("name").value.toString() == name) {
                        followUid = friend.key.toString()
                        Log.d("following", followUid)
                        initListeners()
                        updateButton()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", error.toString())
            }
        })

    }

    private fun initListeners() {
        this.btnFollow.setOnClickListener(View.OnClickListener {
            if(btnFollow.text.toString() == "follow")
                pushFollowing()
            else
                popFollowing()
            updateButton()
        })
    }

    private fun updateButton() {
        db.reference.child(Collections.Friends.name)
            .child(user.uid).child("following").addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var isFollowing: Boolean = false

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


        ref.child("following").updateChildren(map as Map<String, Boolean>)
    }

    private fun popFollowing() {
        Log.d("pop", "working")

        val uid = user.uid

        db.getReference("Friends/$uid/following").child(followUid).setValue(null)
    }
}