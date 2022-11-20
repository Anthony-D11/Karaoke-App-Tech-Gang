package ca.unb.mobiledev.myapplication

class Song(
    var id: String?,
    var name: String?,
    var authorName: String?,
    var avatar: String?,
    var source: String?,
    val duration: String,
    //val songDetail: String?,

){
    val title: String
        get() = "$name - $authorName - $avatar - $source"

    data class Builder(
        var id: String? = null,
        var name: String? = null,
        var authorName: String? = null,
        var avatar: String? = null,
        var source: String? = null,
        var duration: String? = "00:00",
        var songList: ArrayList<Song>? = null,
    ) {

        fun id(id: String) = apply { this.id = id }
        fun name(name: String) = apply { this.name = name }
        fun authorName(authorName: String) = apply { this.authorName = authorName }
        fun avatar(avatar: String?) = apply { this.avatar = avatar }
        fun duration(duration: String?) = apply { this.duration = duration }
        fun source(source: String?) = apply { this.source = source }
        fun songList(songList: ArrayList<Song>) = apply { this.songList = songList }

        fun build() = Playlist(id, name,authorName, avatar, source,duration,  songList)

    }

}

