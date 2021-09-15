package ph.com.onlyfriends.models

import android.util.Log
import android.widget.EditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.properties.Delegates

open class FinderThread(_db: FirebaseDatabase, _et: EditText, _type: Int): Runnable {

    private var db = _db
    private var et = _et
    private var toFind = _et.text.toString()
    private var type = _type
    private var isSearchDone by Delegates.notNull<Boolean>()
    private var flag by Delegates.notNull<Boolean>()

    companion object {
        const val EMAIL = 1
        const val HANDLE = 2
    }

    private fun getResult() {
        if (isSearchDone){
            if(flag) {
                when (type) {
                    EMAIL -> {
                        this.et.error = "Email is Taken"
                        this.et.requestFocus()
                    }
                    HANDLE -> {
                        this.et.error = "Handle is Taken"
                        this.et.requestFocus()
                    }
                }
            }
        }
    }

    override fun run() {
        isSearchDone = false
        flag = false

        when (type) {
            EMAIL -> {
                db.reference.child(Collections.Friends.name).addValueEventListener(object: ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (friend in snapshot.children) {
                            val string = friend.child("email").value.toString()
                            if (string == toFind)
                                flag = true
                        }
                        isSearchDone = true
                        getResult()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("Error", error.toString())
                    }
                })
            }
            HANDLE -> {
                db.reference.child(Collections.Friends.name).addValueEventListener(object: ValueEventListener{

                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (friend in snapshot.children) {
                            val string = friend.child("handle").value.toString()
                            if (string == "@$toFind")
                                flag = true
                        }
                        isSearchDone = true
                        getResult()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("Error", error.toString())
                    }
                })
            }
        }
    }
}