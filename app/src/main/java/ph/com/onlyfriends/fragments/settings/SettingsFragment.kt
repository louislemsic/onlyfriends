package ph.com.onlyfriends.fragments.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ph.com.onlyfriends.R

class SettingsFragment : Fragment() {

    private lateinit var myName: TextView
    private lateinit var myHandle: TextView
    private lateinit var myPFP: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        initializeComponents(view)
        initializeProfile()

        return view
    }

    private fun initializeProfile() {

        myPFP.setImageResource(R.drawable.ic_default_user)


    }

    private fun initializeComponents(view: View) {

        myName = view.findViewById(R.id.tv_my_name)
        myHandle = view.findViewById(R.id.tv_my_handle)
        myPFP = view.findViewById(R.id.iv_my_pfp)

        //Sign Out Button
        view.findViewById<Button>(R.id.btn_sign_out).setOnClickListener {
            Firebase.auth.signOut()
            activity?.finish()!!
        }
    }
}