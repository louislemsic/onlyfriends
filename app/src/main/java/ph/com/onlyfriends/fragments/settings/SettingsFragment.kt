package ph.com.onlyfriends.fragments.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ph.com.onlyfriends.R

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        initializeComponents(view)

        return view
    }

    private fun initializeComponents(view: View) {

        //Sign Out Button
        view.findViewById<Button>(R.id.btn_sign_out).setOnClickListener {
            Firebase.auth.signOut()
            activity?.finish()!!
        }
    }
}