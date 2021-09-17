package ph.com.onlyfriends

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ph.com.onlyfriends.models.Collections
import ph.com.onlyfriends.models.EditHandleFinderThread

class EditProfileActivity : AppCompatActivity() {

    private lateinit var btnCancel: Button
    private lateinit var btnUpload: Button
    private lateinit var btnSave: Button
    private lateinit var pb: ProgressBar

    private lateinit var editDisplayName: EditText
    private lateinit var editHandle: EditText
    private lateinit var editBio: EditText
    private lateinit var originalHandle: String

    private lateinit var user: FirebaseUser
    private lateinit var db: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        initializeFirebase()
        initializeComponents()
        initializeProfile()
    }

    private fun initializeFirebase() {
        user = FirebaseAuth.getInstance().currentUser!!
        db = FirebaseDatabase.getInstance()
    }

    private fun initializeProfile() {

        db.reference.child(Collections.Friends.name).addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (friend in snapshot.children) {
                    if (friend.key == user.uid) {
                        editDisplayName.setText(friend.child("name").value.toString())
                        editBio.setText(friend.child("bio").value.toString())
                        originalHandle = friend.child("handle").value.toString()
                        originalHandle.replace("@", "")

                        editHandle.setText(originalHandle)
                        isEditable(true)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", error.toString())
            }
        })
    }

    private fun initializeComponents() {

        pb = findViewById(R.id.pb_edit_profile)
        editDisplayName = findViewById(R.id.et_displayName)
        editBio = findViewById(R.id.et_edit_bio)
        editHandle = findViewById(R.id.et_edit_handle)

        btnCancel = findViewById(R.id.btn_edit_cancel)
        btnCancel.setOnClickListener {
            finish()
        }

        btnUpload = findViewById(R.id.btn_upload_photo)
        btnUpload.isClickable = false

        btnSave = findViewById(R.id.btn_edit_save)
        btnSave.setOnClickListener {

            if (editHandle.error == null) {
                isEditable(false)
                val newName = editDisplayName.text.toString()
                val newHandle = editHandle.text.toString()
                val newBio = editBio.text.toString()

                updateDatabase(newName, newHandle, newBio)
            }
            else {
                Toast.makeText(this, "Fix all errors first", Toast.LENGTH_SHORT).show()
            }
        }

        editHandle.doAfterTextChanged {
            EditHandleFinderThread(db, editHandle, originalHandle).run()
        }

        isEditable(false)
    }

    private fun updateDatabase(newName: String, newHandle: String, newBio: String) {

        db.reference.child(Collections.Friends.name).child(user.uid).child("name").setValue(newName)
        db.reference.child(Collections.Friends.name).child(user.uid).child("handle").setValue("@$newHandle")
        db.reference.child(Collections.Friends.name).child(user.uid).child("bio").setValue(newBio)

        finish()
    }

    private fun isEditable(flag: Boolean){
        if (flag) {
            pb.visibility = View.GONE
            editDisplayName.isEnabled = true
            editHandle.isEnabled = true
            editBio.isEnabled = true
            btnSave.isClickable = true
        }
        else {
            pb.visibility = View.VISIBLE
            editDisplayName.isEnabled = false
            editHandle.isEnabled = false
            editBio.isEnabled = false
            btnSave.isClickable = false
        }
    }
}