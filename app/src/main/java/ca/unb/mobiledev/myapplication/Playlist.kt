package ca.unb.mobiledev.myapplication

class Playlist private constructor(builder: Builder) {
    var name: String
    val avatar: String
    val background: String
    val songList: ArrayList<String>
    // val playlistDetail: String?,
    val title: String
        get() = "$name - $avatar - ${songList.size}"
    fun addSong(song: Song): Boolean {
        for (songName in songList) {
            if (song.realName == songName) return false
        }
        songList.add(song.realName)
        return true
    }
    class Builder(
        var name: String,
        var avatar: String,
        var background: String,
        var songList: ArrayList<String>
    ) {
        fun build(): Playlist {
            return Playlist(this)
        }
    }
    init {
        name = builder.name
        avatar = builder.avatar
        background = builder.background
        songList = builder.songList
    }
}
