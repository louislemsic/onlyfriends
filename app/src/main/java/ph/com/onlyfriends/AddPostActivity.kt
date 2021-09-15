package ph.com.onlyfriends

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AddPostActivity : AppCompatActivity() {

    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        fab = findViewById(R.id.btn_post_cancel)
        fab.setOnClickListener {
            finish() //go back in post or closes the activity
        }
    }
}