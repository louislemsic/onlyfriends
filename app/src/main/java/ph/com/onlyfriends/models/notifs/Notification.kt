package ph.com.onlyfriends.models.notifs

class Notification(_type:String, _message: String){

    var type: String = _type
    var message: String = _message

    constructor():this("", ""){}
}