package ph.com.onlyfriends.models.notifs

class NotificationSender(val data: Data?, val to:String){
    constructor():this(null,""){}
}