package ca.unb.mobiledev.myapplication

import android.content.Context
import android.content.ContextWrapper
import android.content.res.AssetManager
import android.os.Environment
import android.util.Log
import org.json.JSONObject
import java.io.File
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList

class JsonUtils(context: Context) {
    private lateinit var songList: ArrayList<Song>
    private lateinit var playlistList: ArrayList<Playlist>
    lateinit var playlist :Playlist
    init {
        songList = ArrayList<Song>()
        playlistList = ArrayList<Playlist>()
        //initializeSongList(context)
        //initializePlaylistList(context)
        //val contextWrapper = ContextWrapper(context)
        //val music = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        //val file: File  = File(music, "Perfect.")
        addPlaylist("0", "Trending yay!")
        addSongs("0", "7 years!", "Lukas Graham")

    }

    fun addPlaylist(playListID : String, playListName : String ){
        playlist = Playlist(playListID, playListName, "@tools:sample/avatars")
        playlistList.add(playlist)
    }

    fun addSongs (songID :String, songName: String, authorName : String, ){
        val song = Song(songID, songName, authorName, "@tools:sample/avatars",  "ABC")
        songList.add(song)
        playlist.addSong(song)


    }


    fun getPlaylist(context: Context, playlistId: String?): ArrayList<Song> {
        return playlistList[playlistId!!.toInt()].getSongList()
    }
    private fun getSong(context: Context, songId: String): Song {
        return songList[songId.toInt()]
    }

    private fun loadJSONFromAssets(context: Context): String? {
        Log.i("Hello", context.assets.list("").toString())

        var inputStream: InputStream = context.assets.open(DATA_JSON_FILE)
        var size: Int = inputStream.available()
        var buffer = ByteArray(size)
        inputStream.read(buffer)
        return String(buffer)
    }

    private fun initializeSongList(context: Context) {
        val jsonObject = JSONObject(Objects.requireNonNull(loadJSONFromAssets(context)))

        val jsonArray = jsonObject.getJSONArray(ALLSONGS)
        for (i in 0 until jsonArray.length()) {
            val item: JSONObject = jsonArray[i] as JSONObject
            val id  = item.getString("songId")
            val name = item.getString("songName")
            val authorName = item.getString("authorName")
            val avatar = item.getString("avatar")
            val source = item.getString("source")
            val song = Song(id, name, authorName, avatar, source)
            songList.add(song)
        }
    }
    private fun initializePlaylistList(context: Context) {
        val jsonObject = JSONObject(Objects.requireNonNull(loadJSONFromAssets(context)))

        val jsonArray = jsonObject.getJSONArray(PLAYLISTS)
        for (i in 0 until jsonArray.length()) {
            val item: JSONObject = jsonArray[i] as JSONObject
            val id  = item.getString("playlistId")
            val name = item.getString("playlistName")
            val avatar = item.getString("avatar")
            val songsId  = item.getJSONArray("songList")
            var playlist = Playlist(id, name, avatar)
            for (i in 0 until songsId.length()) {
                playlist.addSong(songList[(songsId[i] as Int)-1])
            }
            playlistList.add(playlist)
        }
    }

    fun getSongList(): ArrayList<Song> {
        return songList
    }
    fun getPlaylistList(): ArrayList<Playlist> {
        return playlistList
    }

    companion object {
        private const val DATA_JSON_FILE = "Data.json"
        private const val ALLSONGS = "songs"
        private const val PLAYLISTS = "playlists"
        private const val SONGLIST = "songList"
    }
}