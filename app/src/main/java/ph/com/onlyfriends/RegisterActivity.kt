package ph.com.onlyfriends

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ph.com.onlyfriends.models.Collections
import ph.com.onlyfriends.models.Friend
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etName: EditText
    private lateinit var etHandle: EditText
    private val emails = ArrayList<String>()
    private val handles = ArrayList<String>()

    // Firebase Components
    private lateinit var mAuth: FirebaseAuth // Firebase Authentication
    private lateinit var db: FirebaseDatabase // Firebase Realtime Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initializeFirebase()
        initializeComponents()
    }

    private fun initializeFirebase() {
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
    }

    private fun initializeComponents() {

        etEmail = findViewById(R.id.et_add_email)
        etPassword = findViewById(R.id.et_add_password)
        etName = findViewById(R.id.et_add_name)
        etHandle = findViewById(R.id.et_add_handle)

        // Login Text Button-ish
        val backToLogin = findViewById<TextView>(R.id.tv_add_login)
        backToLogin.setOnClickListener {
            finish()
        }

        // Register Button
        val register = findViewById<Button>(R.id.btn_add_register)
        register.setOnClickListener {

            // get the data of the user
            val email: String = etEmail.text.toString().trim()
            val password: String = etPassword.text.toString().trim()
            val name: String = etName.text.toString().trim()
            val handle: String = etHandle.text.toString().trim()

            if(areInputsValid(email, password, name, handle)) {
                // add a new user to the db
                val friend = Friend(email, name, handle)
                registerAndStore(friend, password)
            }
        }

        // Check if Email is taken
        etEmail.doAfterTextChanged {
            val email: String = it.toString().trim()
            if(isEmailTaken(email)) {
                this.etEmail.error = "Email is Taken"
                this.etEmail.requestFocus()
            }
        }

        // Check if Handle is taken
        etHandle.doAfterTextChanged {
            val handle: String = it.toString().trim()
            if(isHandleTaken("@$handle")) {
                this.etHandle.error = "Handle is Taken"
                this.etHandle.requestFocus()
            }
        }
    }



    private fun registerAndStore(friend: Friend, password: String) {
        findViewById<ProgressBar>(R.id.pb_register).visibility = View.VISIBLE

        // Register the Friend to the Firebase Authentication
        mAuth.createUserWithEmailAndPassword(friend.email, password).addOnCompleteListener { task ->
            if(task.isSuccessful) {

                // If successful in registering the email, place the information to Firebase Database
                db.getReference(Collections.Friends.name).child(mAuth.currentUser!!.uid).setValue(friend).addOnCompleteListener {
                    if(it.isSuccessful) {
                        successfulRegistration()
                    }
                    else {
                        failedRegistration()
                    }
                }
            }
            else {
                failedRegistration()
            }
        }
    }

    private fun failedRegistration() {
        findViewById<ProgressBar>(R.id.pb_register).visibility = View.GONE
        Toast.makeText(this, "Friend Registration Failed", Toast.LENGTH_SHORT).show()
    }

    private fun successfulRegistration() {
        findViewById<ProgressBar>(R.id.pb_register).visibility = View.GONE
        Toast.makeText(this, "Friend Registration Success", Toast.LENGTH_SHORT).show()

        // go back to LoginActivity
        finish()
    }

    private fun areInputsValid(email: String, password: String, name: String, handle: String): Boolean {
        var isValid = true

        if (name.isEmpty()) {                         // Checks the Name Field if Empty
            this.etName.error = "Required Field"
            this.etName.requestFocus()
            isValid = false
        }

        if (handle.isEmpty()) {                       // Checks the Handle Field if Empty
            this.etHandle.error = "Required Field"
            this.etHandle.requestFocus()
            isValid = false
        }
        else if (!isHandleValid(handle)){              // Checks the Format of the Handle
            this.etHandle.error = "Numbers, Letters, and underscores only"
            this.etHandle.requestFocus()
            isValid = false
        }

        if (password.isEmpty()) {                      // Checks the Password Field if Empty
            this.etPassword.error = "Required Field"
            this.etPassword.requestFocus()
            isValid = false
        }
        else if (password.length <= 8){                // Checks if the PW is at least 8 characters long
            this.etPassword.error = "At least 8 characters"
            this.etPassword.requestFocus()
            isValid = false
        }

        if (email.isEmpty()) {                         // Checks the Email Field if Empty
            this.etEmail.error = "Required Field"
            this.etEmail.requestFocus()
            isValid = false
        }
        else if (!isEmailValid(email)) {               // Checks the Format of the Email
            this.etEmail.error = "Wrong Email Format"
            this.etEmail.requestFocus()
            isValid = false
        }

        return isValid
    }

    private fun isEmailValid(email: String?): Boolean {
        val expression = "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email.toString())
        return matcher.matches()
    }

    private fun isHandleValid(handle: String): Boolean {
        val expression = "(?=.*\\w)[\\w]{1,15}\$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(handle)
        return matcher.matches()
    }

    private fun isHandleTaken(handle: String): Boolean {
        var isTaken = false

        db.reference.child(Collections.Friends.name).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (friend in snapshot.children) {
                    val string = friend.child("handle").value.toString()
                    handles.add(string)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", error.toString())
            }
        })

        for (retrievedHandles in handles)
            if (handle == retrievedHandles)
                isTaken = true

        return isTaken
    }

    private fun isEmailTaken(email: String): Boolean {
        var isTaken = false

        db.reference.child(Collections.Friends.name).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (friend in snapshot.children) {
                    val string = friend.child("email").value.toString()
                    emails.add(string)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", error.toString())
            }
        })

        for (retrievedEmail in emails)
            if (email == retrievedEmail)
                isTaken = true

        return isTaken
    }
}