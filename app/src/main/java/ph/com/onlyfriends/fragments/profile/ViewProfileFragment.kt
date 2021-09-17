package ph.com.onlyfriends.fragments.profile

import android.content.Intent
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
import ph.com.onlyfriends.EditProfileActivity
import ph.com.onlyfriends.R
import ph.com.onlyfriends.models.Collections

class ViewProfileFragment : Fragment() {

    private lateinit var myName: TextView
    private lateinit var myHandle: TextView
    private lateinit var myFollowersCount: TextView
    private lateinit var myBio: TextView

    private lateinit var myPFP: ImageView
    private lateinit var editButton: Button

    private lateinit var user: FirebaseUser
    private lateinit var db: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_profile, container, false)

        initializeComponents(view)
        initializeProfile(view)

        return view
    }

    private fun initializeProfile(view: View) {

        myPFP.setImageResource(R.drawable.ic_default_user)

        db.reference.child(Collections.Friends.name).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (friend in snapshot.children) {
                    if (friend.key == user.uid) {
                        myName.text = friend.child("name").value.toString()
                        myHandle.text = friend.child("handle").value.toString()
                        myFollowersCount.text = friend.child("numFollowing").value.toString()

                        val bio: String = friend.child("bio").value.toString()
                        if(bio.isNotEmpty()) {
                            myBio.text = bio
                        }
                        else
                            myBio.text = view.resources.getString(R.string.bio_empty)
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
        myFollowersCount = view.findViewById(R.id.tv_followers)
        myBio = view.findViewById(R.id.tv_settings_bio)

        myPFP = view.findViewById(R.id.iv_profile_pfp)
        editButton = view.findViewById(R.id.btn_edit_profile)

        //Sign Out Button
        view.findViewById<Button>(R.id.btn_sign_out).setOnClickListener {
            Firebase.auth.signOut()
            activity?.finish()!!
        }

        editButton.setOnClickListener {
            val intent = Intent(activity, EditProfileActivity::class.java)
            activity?.startActivity(intent)
        }
    }
}