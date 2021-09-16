package ph.com.onlyfriends.models

open class Friend(_email: String = "", _name: String = "", _handle: String = "")  {

    var email: String = _email
    var name: String = _name
    var handle: String = "@$_handle"
    var bio: String = ""
    var following: MutableMap<String, Boolean> = HashMap()
    var numFollowing: Int = 1

    open fun setUid(uid: String) {
        following[uid] = true
    }
}