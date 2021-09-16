package ph.com.onlyfriends

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import ph.com.onlyfriends.fragments.chat.PostFragment
import ph.com.onlyfriends.fragments.search.SearchFragment
import ph.com.onlyfriends.fragments.profile.ViewProfileFragment

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
     * This method loads the fragment being passed into the parameter.
     */
    private fun loadFragment(fragment : Fragment) {
        fragment.arguments = bundle
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_fragment_holder, fragment)
        fragmentTransaction.commit()
    }
}