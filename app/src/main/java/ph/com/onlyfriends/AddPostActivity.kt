package ph.com.onlyfriends

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import ph.com.onlyfriends.models.PostModel

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
    private lateinit var user: FirebaseUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        // firebase authentication
        firebaseAuth = FirebaseAuth.getInstance()
        user = firebaseAuth.currentUser!!

        // get data from db
        db = FirebaseDatabase.getInstance().getReference("Friends")

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

    private fun uploadData(content: String) {

        val timestamp: String = String.format(System.currentTimeMillis().toString())

        val model = PostModel(content, user.uid)

        // path to store data
        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference("Posts")

        ref.child(timestamp).setValue(model)
            .addOnSuccessListener {
                // reset views
                etPostContent.setText("")
            }
            .addOnFailureListener {
                Toast.makeText(this, ""+it.message, Toast.LENGTH_SHORT).show()
            }
    }
}