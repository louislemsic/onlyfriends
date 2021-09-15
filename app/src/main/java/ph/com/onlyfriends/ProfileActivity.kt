package ph.com.onlyfriends

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvHandle: TextView
    private lateinit var btnFollow: Button

    private lateinit var name: String
    private lateinit var handle: String
    private var isFollowed: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        this.tvName = findViewById(R.id.tv_profile_name)
        this.tvHandle = findViewById(R.id.tv_profile_handle)
        this.btnFollow = findViewById(R.id.btn_profile_follow)

        getIntentData()
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
}