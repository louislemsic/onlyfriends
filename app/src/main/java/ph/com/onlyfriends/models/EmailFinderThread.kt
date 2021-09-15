package ph.com.onlyfriends.models

import android.util.Log
import android.widget.EditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.properties.Delegates

class EmailFinderThread(_db: FirebaseDatabase, _et: EditText): Thread() {

    private var db = _db
    private var et = _et
    private var toFind = _et.text.toString()
    private var isSearchDone = false
    private var flag by Delegates.notNull<Boolean>()

    private fun getResult() {
        if (!isSearchDone){
            if(flag) {
                this.et.error = "Email is Taken"
                this.et.requestFocus()
            }
            isSearchDone = true
        }
    }

    override fun run() {
        db.reference.child(Collections.Friends.name).addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                flag = false
                for (friend in snapshot.children) {
                    val string = friend.child("email").value.toString()
                    if (string == toFind)
                        flag = true
                }
                getResult()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", error.toString())
            }
        })
    }
}