package ph.com.onlyfriends.models

class Post() {

    var uName: String = ""
    var uHandle: String = ""
    var pContent: String = ""
    var uid: String = ""

    constructor(_name: String, _handle: String, _pContent: String, _uid: String) : this() {
        uName = _name
        uHandle = _handle
        pContent = _pContent
        uid = _uid
    }

    constructor(_name: String, _handle: String, _pContent: String) : this() {
        uName = _name
        uHandle = _handle
        pContent = _pContent
    }
}