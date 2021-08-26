package ph.com.onlyfriends

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Matcher
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        initializeComponents()
    }

    private fun initializeComponents() {

        etEmail = findViewById(R.id.et_login_email)
        etPassword = findViewById(R.id.et_login_password)

        val register = findViewById<TextView>(R.id.tv_login_register)
        register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val login = findViewById<Button>(R.id.btn_login_login)
        login.setOnClickListener {
            val email: String = etEmail.text.toString().trim()
            val password: String = etPassword.text.toString().trim()

            if (areInputsValid(email, password)) {
                signIn(email, password)
            }
        }
    }

    private fun signIn(email: String, password: String) {
        findViewById<ProgressBar>(R.id.pb_login).visibility = View.VISIBLE
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    val intent = Intent(this, HomeActivity::class.java)
                    Toast.makeText(applicationContext,"Success", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                }
                else {
                    Toast.makeText(applicationContext,"Invalid Credentials", Toast.LENGTH_SHORT).show()
                }
                findViewById<ProgressBar>(R.id.pb_login).visibility = View.GONE
            }
    }

    private fun areInputsValid(email: String, password: String): Boolean {
        var isValid = true

        if (password.isEmpty()) {
            this.etPassword.error = "Required Field"
            this.etPassword.requestFocus()
            isValid = false
        }
        if (email.isEmpty()) {
            this.etEmail.error = "Required Field"
            this.etEmail.requestFocus()
            isValid = false
        }
        else if (!isEmailValid(email)) {
            this.etEmail.error = "Wrong Email Format"
            this.etEmail.requestFocus()
            isValid = false
        }

        return isValid
    }

    private fun isEmailValid(email: String?): Boolean {
        val expression = "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email.toString())
        return matcher.matches()
    }
}