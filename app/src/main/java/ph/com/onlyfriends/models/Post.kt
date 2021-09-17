package ph.com.onlyfriends.models

class Post(_email: String = "", _name: String = "", _handle: String = "", _uid: String = "", _pId: String = "", _pTime: String = "", _pContent: String = "") {

    var uEmail: String = _email
    var uName: String = _name
    var uHandle: String = "@$_handle"
    var uid: String = _uid
    var pContent: String = _pContent
}