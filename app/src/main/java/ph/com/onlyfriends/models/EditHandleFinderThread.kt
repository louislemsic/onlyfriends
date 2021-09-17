package ph.com.onlyfriends.models

import android.util.Log
import android.widget.EditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.properties.Delegates

class EditHandleFinderThread(_db: FirebaseDatabase, _et: EditText, myHandle: String): Thread(){

    private var ogHandle = myHandle
    private var db = _db
    private var et = _et
    private var toFind = _et.text.toString()
    private var isSearchDone = false
    private var flag by Delegates.notNull<Boolean>()

    private fun getResult() {
        if (!isSearchDone){
            if(flag) {
                this.et.error = "Handle is Taken"
                this.et.requestFocus()
            }
            isSearchDone = true
        }
    }

    override fun run() {
        db.reference.child(Collections.Friends.name).addValueEventListener(object:
            ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                flag = false
                for (friend in snapshot.children) {
                    val string = friend.child("handle").value.toString()
                    if (string == "@$toFind" && string != ogHandle) {
                        flag = true
                    }
                }
                getResult()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", error.toString())
            }
        })
    }
}