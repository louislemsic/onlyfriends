package ph.com.onlyfriends.models.notifs

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ph.com.onlyfriends.R
import ph.com.onlyfriends.models.Collections
import kotlin.random.Random


class FirebaseNotifService: FirebaseMessagingService(){

    private val CHANNEL_ID = "abs-cbn"
    private var currentUser = FirebaseAuth.getInstance().currentUser
    private val pushedNotification = mutableListOf<Int>()
    private val notifLines = mutableListOf<String>()
    private var pushedNotifsInc = 0

    override fun onNewToken(token :String){
        super.onNewToken(token)
        updateToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage){
        super.onMessageReceived(remoteMessage)

        notifLines.add("Really? <handle> decided to follow you?")
        notifLines.add("Wow. Someone named <handle> is following you now?")
        notifLines.add("Oh nothing, <handle> just followed you.")

        val type = remoteMessage.data["Title"].toString()
        val handle = remoteMessage.data["Message"].toString()
        val recipient = remoteMessage.data["RecipientID"].toString()
        val sender = remoteMessage.data["SenderID"].toString()

        var message = notifLines[Random.nextInt(0, 3)]
        message = message.replace("<handle>", handle)

        createNotificationChannel()

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(type)
            .setContentText(message)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            pushedNotification.add(pushedNotifsInc)
            notify(pushedNotification[pushedNotifsInc], builder.build())
            pushedNotifsInc++
        }

        updateNotification(handle, "newFollower_$sender", recipient)
    }

    private fun updateToken(refreshToken:String){
        val token = Token(refreshToken)

        if (currentUser != null)
            FirebaseDatabase.getInstance().getReference("Tokens").child(currentUser!!.uid).setValue(token)
    }

    private fun updateNotification(type: String, key: String, recipient: String) {
        currentUser = FirebaseAuth.getInstance().currentUser
        val map: MutableMap<String, Any> = HashMap()
        map[key] = type

        if(currentUser != null)
            FirebaseDatabase.getInstance().getReference(Collections.Friends.name)
                .child(recipient).child("notifications")
                .updateChildren(map)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}