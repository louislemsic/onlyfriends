package ph.com.onlyfriends

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AddPostActivity : AppCompatActivity() {

    private lateinit var btnCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        btnCancel = findViewById(R.id.btn_post_cancel)
        btnCancel.setOnClickListener {
            finish() //go back in post or closes the activity
        }
    }
}