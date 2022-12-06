package ca.unb.mobiledev.myapplication

class Song private constructor(builder: Builder) {
    var realName: String
    var nickName: String
    var authorName: String
    var avatar: String
    var duration: String
    var source: String


    class Builder(
        var realName: String,
        var nickName: String,
        var authorName: String,
        var avatar: String,
        var duration: String,
        var source: String
    ) {
        fun build(): Song {
            return Song(this)
        }
    }
    init {
        realName = builder.realName
        nickName = builder.nickName
        avatar = builder.avatar
        authorName = builder.authorName
        duration = builder.duration
        source = builder.source
    }
}
