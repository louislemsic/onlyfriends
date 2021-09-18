package ph.com.onlyfriends.models.notifs

class Data(val Title:String, val Message:String, val RecipientID: String, val SenderID: String){
    constructor():this("","", "", ""){}
}