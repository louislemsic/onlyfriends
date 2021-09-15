package ph.com.onlyfriends.fragments.chat

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ph.com.onlyfriends.AddPostActivity
import ph.com.onlyfriends.R

class PostFragment : Fragment() {

    private lateinit var fab: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_post, container, false)

        initializeComponents(view)

        return view
    }

    private fun initializeComponents(view: View) {
        fab = view.findViewById(R.id.fab_add_post)
        fab.setOnClickListener {
            val intent = Intent(activity, AddPostActivity::class.java)
            activity?.startActivity(intent)
        }
    }

}