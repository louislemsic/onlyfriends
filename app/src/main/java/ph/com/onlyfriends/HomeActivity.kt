package ph.com.onlyfriends

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import ph.com.onlyfriends.fragments.chat.PostFragment
import ph.com.onlyfriends.fragments.notifications.NotificationFragment
import ph.com.onlyfriends.fragments.search.SearchFragment
import ph.com.onlyfriends.fragments.profile.ViewProfileFragment
import ph.com.onlyfriends.models.notifs.FirebaseNotifService

class HomeActivity : AppCompatActivity() {

    private val bundle = Bundle()

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    // Before 2.0
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Generate Token
        generateToken()

        //Set ChatFragment as the Default Fragment
        loadFragment(PostFragment())

        //OnItemSelectedListener for the Nav bar
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener {
            when(it.itemId) {
                R.id.nav_messenger -> {  //Forecast Fragment
                    loadFragment(PostFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.nav_search -> {    //Search for other locations Fragment
                    loadFragment(SearchFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.nav_notifications -> {
                    loadFragment(NotificationFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.nav_settings -> {  //Settings Fragment
                    loadFragment(ViewProfileFragment())
                    return@setOnItemSelectedListener true
                }
                else -> {   //Default is Forecast Fragment
                    loadFragment(PostFragment())
                    return@setOnItemSelectedListener true
                }
            }
        }
    }

    /**
     * generates token for logged in user
     */
    private fun generateToken() {
        val notifHandler = FirebaseNotifService()

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            if (token != null) {
                Log.d("token", token)
                notifHandler.onNewToken(token)
            }
        })
    }

    /**
     * This method loads the fragment being passed into the parameter.
     */
    private fun loadFragment(fragment : Fragment) {
        fragment.arguments = bundle
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_fragment_holder, fragment)
        fragmentTransaction.commit()
    }
}