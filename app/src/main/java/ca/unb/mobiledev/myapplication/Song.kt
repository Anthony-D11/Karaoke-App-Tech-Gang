package ca.unb.mobiledev.myapplication

class Song(id: String, name: String, authorName: String, avatar: String, source: String) {
    var id: String
    var name: String
    var authorName: String
    var avatar: String
    var source: String
    val duration: String = "00:00"
    val songDetail: String
        get() = "$name - $authorName - $avatar - $source"
    init {
        this.id = id
        this.name = name
        this.authorName = authorName
        this.avatar = avatar
        this.source = source
    }

}