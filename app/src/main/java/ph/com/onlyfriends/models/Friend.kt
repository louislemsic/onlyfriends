package ph.com.onlyfriends.models

open class Friend(_email: String = "", _name: String = "", _handle: String = "")  {

    var email: String = _email
    var name: String = _name
    var handle: String = "@$_handle"

    var followers: HashMap<String, String> = HashMap()
    var numFollowers: Int = 0

    init {
        followers["Activated"] = "true"
    }
}