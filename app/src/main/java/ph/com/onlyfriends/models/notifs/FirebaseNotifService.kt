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


class FirebaseNotifService: FirebaseMessagingService(){

    private val CHANNEL_ID = "abs-cbn"
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val pushedNotification = mutableListOf<Int>()
    private var pushedNotifsInc = 0
    override fun onNewToken(token :String){
        super.onNewToken(token)

        updateToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage){
        super.onMessageReceived(remoteMessage)

        val type = remoteMessage.data["Title"].toString()
        val message = remoteMessage.data["Message"].toString()

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

        updateNotification(type, message)
    }

    private fun updateToken(refreshToken:String){
        val token = Token(refreshToken)

        if (currentUser != null)
            FirebaseDatabase.getInstance().getReference("Tokens").child(currentUser.uid).setValue(token)
    }

    private fun updateNotification(type: String, msg: String) {
        val notif = Notification(type, msg)

        val map: MutableMap<String, Any> = HashMap()
        map[notif.type] = notif.message

        if(currentUser != null)
            FirebaseDatabase.getInstance().getReference(Collections.Friends.name)
                .child(currentUser.uid).child("notifications")
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