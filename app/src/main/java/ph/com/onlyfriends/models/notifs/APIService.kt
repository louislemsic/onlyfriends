package ph.com.onlyfriends.models.notifs

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
interface APIService {
    @Headers(
        "Content-Type:application/json",
        "Authorization:key=AAAArzBscaM:APA91bGZdillenSSn8ZLdPRFJ8Z1_hpGffWS81Uuiux1GsfSXVGzOg5OF1iHmStCxE6W2D3hMPeIxIFDD83oSyomgCerzP35SL65mu0YZBvgSGbJNLPIQeVZmUHtI2_SQ7hbvyVqwa3I" // Your server key refer to video for finding your server key
    )
    @POST("fcm/send")
    fun sendNotifcation(@Body body: NotificationSender?): Call<MyResponse?>?
}