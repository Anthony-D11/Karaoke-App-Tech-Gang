package ca.unb.mobiledev.myapplication

import android.content.Context
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.security.AccessController.getContext
import java.util.*

class JsonUtils(context: Context) {
    private var songList: ArrayList<Song> = ArrayList<Song>()
    private var playlistList: ArrayList<Playlist> = ArrayList<Playlist>()
    //lateinit var playlist :Playlist
    init {
       processJSON(context)
//        initializeSongList(context)
      // initializePlaylistList(context)
//       val contextWrapper = ContextWrapper(context)
//        val music = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
//        val file: File  = File(music, "Perfect.")
        //addPlaylist("0", "Trending yay!")
        //addSongs("0", "7 years!", "Lukas Graham")
        }

//    fun addPlaylist(playListID : String, playListName : String ){
//        playlist = Playlist(playListID, playListName, "@tools:sample/avatars")
//        playlistList.add(playlist)
//
//    }

    private fun processJSON(context: Context) {
        // Initialize the lateinit value
        songList = ArrayList<Song>()
        playlistList = ArrayList<Playlist>()

        try {
            // Create a JSON Object from file contents String
            val jsonObject = JSONObject(Objects.requireNonNull(loadJSONFromAssets(context)))

            // Create a JSON Array from the JSON Object
            // This array is the "courses" array mentioned in the lab write-up
            val jsonArray = jsonObject.getJSONArray(PLAYLISTS)
            for (i in 0 until jsonArray.length()) {
                var curr = jsonArray.getJSONObject(i)
                val playlist:Playlist = Playlist.Builder()
                    .id(curr.getString(KEY_ID)).name(curr.getString(NAME)).avatar(curr.getString(
                        KEY_AVATAR)).songList(songList)
                    .build()
                playlistList.add(playlist)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }



//    fun addSongs (songID :String, songName: String, authorName : String, ){
//        val song = Song(songID, songName, authorName, "@tools:sample/avatars",  "ABC")
//        songList.add(song)
//        playlist.addSong(song)
//
//
//    }


    fun getPlaylist(context: Context, playlistId: String?): ArrayList<Song>? {
        return playlistList[playlistId!!.toInt()].getSongList()
    }
    private fun getSong(context: Context, songId: String): Song {
        return songList[songId.toInt()]
    }

    private fun loadJSONFromAssets(context: Context): String? {
        Log.i("Hellos", context.assets.list("").toString())
        var string: String? = ""
        try{
            val inputStream: InputStream = context.assets.open("Data.json")
            var size: Int = inputStream.available()
         var buffer = ByteArray(size)
            inputStream.read(buffer)
            string= String(buffer)
            Log.i("JsonUtils", " file was opened")

       } catch (e: IOException) {
            Log.i("JsonUtils", "helpSS")
         e.printStackTrace()
      }
        return string
    }





  fun initializeSongList(context: Context) {
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

    // work here try and figure out why its not initalizing INT error
    fun initializePlaylistList(context: Context) {
        val jsonObject = JSONObject(Objects.requireNonNull(loadJSONFromAssets(context)))

        val jsonArray = jsonObject.getJSONArray(PLAYLISTS)
        for (i in 0 until jsonArray.length()) {
            val item: JSONObject = jsonArray[i] as JSONObject
            val id  = item.getString("playlistId")
            val name = item.getString("playlistName")
            val avatar = item.getString("avatar")
            val songsId  = item.getJSONArray("songList")
            var playlist = Playlist(id, name, avatar, songList)
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
        private const val NAME = "name"
        private const val KEY_ID = "id"
        private const val KEY_AVATAR = "avatar"


    }
}