package ph.com.onlyfriends.models

class Post() {

    var uName: String = ""
    var uHandle: String = ""
    var pContent: String = ""
    var uid: String = ""

    constructor(type: Int, _pContent: String, _uid: String) : this() {
        when (type) {
            DATABASE -> {
                pContent = _pContent
                uid = _uid
            }
        }
    }

    constructor(type: Int, s1: String, s2: String, s3: String) : this() {
        when (type) {
            POST -> {
                uName = s1
                uHandle = s2
                pContent = s3
            }
            WHITELIST -> {
                uName = s1
                uHandle = s2
                uid = s3
            }
        }
    }

    companion object {
        const val POST = 1
        const val DATABASE = 2
        const val WHITELIST = 3
    }
}