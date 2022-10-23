package ca.unb.mobiledev.myapplication

class Playlist(id: String, name: String, avatar: String) {
    private var id: String
    private var name: String
    private var avatar: String
    private var songList: ArrayList<Song>
    val playlistDetail: String
        get() = "$name - $avatar - ${songList.size}"
    init {
        this.id = id
        this.name = name
        this.avatar = avatar
        this.songList = ArrayList()
    }
    fun addSong(song: Song) {
        songList.add(song)
    }
    fun getName(): String {
        return name
    }
    fun getId(): String {
        return id
    }
    fun getAvatar(): String {
        return avatar
    }
    fun getSongList(): ArrayList<Song> {
        return songList
    }

}