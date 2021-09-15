package ph.com.onlyfriends.fragments.settings

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import ph.com.onlyfriends.R
import ph.com.onlyfriends.models.Collections

class SettingsFragment : Fragment() {

    private lateinit var myName: TextView
    private lateinit var myHandle: TextView
    private lateinit var myPFP: ImageView
    private lateinit var user: FirebaseUser
    private lateinit var db: FirebaseDatabase

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

        db.reference.child(Collections.Friends.name).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (friend in snapshot.children) {
                    if (friend.key == user.uid) {
                        myName.text = friend.child("name").value.toString()
                        myHandle.text = friend.child("handle").value.toString()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", error.toString())
            }
        })
    }

    private fun initializeComponents(view: View) {

        user = FirebaseAuth.getInstance().currentUser!!
        db = FirebaseDatabase.getInstance()
        myName = view.findViewById(R.id.tv_my_name)
        myHandle = view.findViewById(R.id.tv_profile_handle)
        myPFP = view.findViewById(R.id.iv_profile_pfp)

        //Sign Out Button
        view.findViewById<Button>(R.id.btn_sign_out).setOnClickListener {
            Firebase.auth.signOut()
            activity?.finish()!!
        }
    }
}