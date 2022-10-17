package ca.unb.mobiledev.myapplication

class Song(name: String, authorName: String, avatar: String, source: String) {
    var name: String
    var authorName: String
    var avatar: String
    var source: String
    val duration: String
    val songDetail: String
        get() = "$name - $authorName - $avatar - $source"
    init {
        this.name = name
        this.authorName = authorName
        this.avatar = avatar
        this.source = source
    }

}