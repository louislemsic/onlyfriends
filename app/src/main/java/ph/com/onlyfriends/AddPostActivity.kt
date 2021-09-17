package ph.com.onlyfriends

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import ph.com.onlyfriends.models.Post

class AddPostActivity : AppCompatActivity() {

    // views
    private lateinit var ivOnlyfriends : ImageView
    private lateinit var tvTitle : TextView
    private lateinit var etPostContent: EditText
    private lateinit var cancelBtn: Button
    private lateinit var uploadBtn: Button


    // firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: DatabaseReference

    // user information
    private lateinit var email: String
    private lateinit var name: String
    private lateinit var handle: String
    private lateinit var uid: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        // firebase authentication
        firebaseAuth = FirebaseAuth.getInstance()
        checkUserStatus()

        // get data from db
        db = FirebaseDatabase.getInstance().getReference("Friends")
        val query: Query = db.orderByChild("email").equalTo(email)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds: DataSnapshot in snapshot.children) {
                    name = "" + ds.child("name").value
                    handle = "" + ds.child("handle").value
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", error.message)
            }
        })

        // initialize views
        ivOnlyfriends = findViewById(R.id.iv_onlyfriends_logo2)
        tvTitle = findViewById(R.id.post_title)
        etPostContent = findViewById(R.id.et_post_content)
        cancelBtn = findViewById(R.id.btn_post_cancel)
        uploadBtn = findViewById(R.id.btn_post)

        // upload button on click listener
        uploadBtn.setOnClickListener {
            // get data from ET
            val content: String = etPostContent.text.toString().trim()

            if (TextUtils.isEmpty(content)) {
                Toast.makeText(this, "Post should not be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            uploadData(content)
            finish()
        }

        // cancel button on click listener
        cancelBtn.setOnClickListener {
            finish() //go back in post or closes the activity
        }
    }

    override fun onStart() {
        super.onStart()
        checkUserStatus()
    }

    override fun onResume() {
        super.onResume()
        checkUserStatus()
    }

    private fun uploadData(content: String) {

        val timestamp: String = String.format(System.currentTimeMillis().toString())

        val model = Post(name, handle, content, uid)

        // path to store data
        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference("Posts")

        ref.child(timestamp).setValue(model)
            .addOnSuccessListener {
            // reset views
                etPostContent.setText("")
            }
            .addOnFailureListener{
                Toast.makeText(this, ""+it.message, Toast.LENGTH_SHORT).show()
            }

    }

    private fun checkUserStatus() {
        val user: FirebaseUser? = firebaseAuth.currentUser
        if (user != null) {
            // user is signed in stay in this activity
            email = user.email.toString()
            uid = user.uid
        }
        else {
            // User not signed in, go to login activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}