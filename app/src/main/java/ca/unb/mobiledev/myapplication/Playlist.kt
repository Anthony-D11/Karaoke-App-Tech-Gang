package ca.unb.mobiledev.myapplication

class Playlist(
    private var id: String?,
    private var name: String?,
    private var avatar: String?,
    private var songList: ArrayList<Song>?,
   // val playlistDetail: String?,

    ) {
    val title: String
        get() = "$name - $avatar - ${songList?.size}"

    fun addSong(song: Song) {
        songList?.add(song)
    }

    fun getName(): String? {
        return name
    }

    fun getId(): String? {
        return id
    }

    fun getAvatar(): String? {
        return avatar
    }

    fun getSongList(): ArrayList<Song>? {
        return songList
    }

    fun addPlaylist(playlistList: ArrayList<Playlist>) {
       // playlistList?.add(playList)
    }


    data class Builder(
        var id: String? = null,
        var name: String? = null,
        var avatar: String? = null,
        var songList: ArrayList<Song>? = null,
    ) {

        fun id(id: String) = apply { this.id = id }
        fun name(name: String) = apply { this.name = name }
        fun avatar(avatar: String?) = apply { this.avatar = avatar }
        fun songList(songList: ArrayList<Song>) = apply { this.songList = songList }

        fun build() = Playlist(id, name, avatar, songList)


    }
}
