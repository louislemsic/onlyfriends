package ph.com.onlyfriends

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import ph.com.onlyfriends.fragments.chat.ChatFragment
import ph.com.onlyfriends.fragments.notifications.NotificationsFragment
import ph.com.onlyfriends.fragments.search.SearchFragment
import ph.com.onlyfriends.fragments.settings.SettingsFragment

class HomeActivity : AppCompatActivity() {

    private val bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //Set ChatFragment as the Default Fragment
        loadFragment(ChatFragment())

        //OnItemSelectedListener for the Nav bar
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener {
            when(it.itemId) {
                R.id.nav_messenger -> {  //Forecast Fragment
                    loadFragment(ChatFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.nav_search -> {    //Search for other locations Fragment
                    loadFragment(SearchFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.nav_notifications -> {
                    loadFragment(NotificationsFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.nav_settings -> {  //Settings Fragment
                    loadFragment(SettingsFragment())
                    return@setOnItemSelectedListener true
                }
                else -> {   //Default is Forecast Fragment
                    loadFragment(ChatFragment())
                    return@setOnItemSelectedListener true
                }
            }
        }
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