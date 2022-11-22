package ca.unb.mobiledev.myapplication

import android.content.Context
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.util.*

class JsonUtils(context: Context) {
    private var songList: ArrayList<Song> = ArrayList<Song>()
    private var playlistList: ArrayList<Playlist> = ArrayList<Playlist>()
    //lateinit var playlist :Playlist
    init {
       processJSON(context)
        loadPlaylist(context)
      //  loadSongList(context)
      //  getPlaylist(context, playlistList)
//     initializeSongList(context)
       //initializePlaylistList(context)
//        val contextWrapper = ContextWrapper(context)
//       val music = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
//        val file: File  = File(music, "Perfect.")

        }


    private fun processJSON(context: Context) {
        // Initialize the lateinit value
        songList = ArrayList<Song>()
        playlistList = ArrayList<Playlist>()

        try {

            val jsonObject = JSONObject(Objects.requireNonNull(loadJSONFromAssets(context)))
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

        try {

            val jsonObject = JSONObject(Objects.requireNonNull(loadJSONFromAssets(context)))
            val jsonArray = jsonObject.getJSONArray(SONGLIST)
            for (i in 0 until jsonArray.length()) {
                var curr = jsonArray.getJSONObject(i)
                val songlists :Song = Song.Builder()
                    .id(curr.getString(KEY_ID_SONG)).name(curr.getString(NAME_SONG)).avatar(curr.getString(
                        KEY_AVATAR_SONG)).authorName(curr.getString(AUTHOR_NAME_SONG)).source(curr.getString(
                        SOURCE_SONG)).authorName(curr.getString(DURATION))
                    .build()
                songList.add(songlists)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }




    }



    fun getPlaylist(context: Context, playlistId: String?): ArrayList<Song>? {
        return playlistList[playlistId!!.toInt()].songList
    }
    fun getSong(context: Context, songId: String): Song {
        return songList[songId.toInt()]
    }

     fun loadJSONFromAssets(context: Context): String? {
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
            Log.i("JsonUtils", "file did NOT open")
         e.printStackTrace()
      }
        return string
    }

   fun loadPlaylist(context: Context){

       var playlistID: ArrayList<String?> = ArrayList()
       var playlistNameShow: ArrayList<String?> = ArrayList()
       var avatarShow: ArrayList<String?> = ArrayList()
       var songPlaylistShow: ArrayList<String?> = ArrayList()
       var testing : ArrayList<String?> = ArrayList()

       loadSongList(context) //initilizes songlist
       var songListOnly: ArrayList<Song> = ArrayList<Song>()


       try {
           val obj = JSONObject(loadJSONFromAssets(context))
           val userArray = obj.getJSONArray("playlists")
           val userArraySong = obj.getJSONArray("songs")

           for (i in 0 until userArray.length()) {

               val playlistDetail = userArray.getJSONObject(i)
               val songListsDetail = userArraySong.getJSONObject(i)
               Log.i("JsonUtils", "playlistID $playlistDetail")

               playlistID.add(playlistDetail.getString("playlistId"))
               playlistNameShow.add(playlistDetail.getString("playlistName"))
               avatarShow.add(playlistDetail.getString("avatar"))
               testing.add(playlistDetail.getString("songList"))
              songPlaylistShow.add(songListsDetail.getString("songId"))

               Log.i("JsonUtils", "songList adding with melissa " + songList[i].authorName)

               val  playlist = Playlist(playlistID[i], playlistNameShow[i], avatarShow[i], songList)



               playlistList.add(playlist)
               Log.i("JsonUtils", "songList adding with melissa test "    + playlistList[i].name)

           }
       }
       catch (e: JSONException) {
           Log.i("JsonUtils", " loadplaylist errors")
           e.printStackTrace()
       }



   }


    // where do I call this from like the page.
    fun loadSongList(context: Context){

        var songListIDShow: ArrayList<String?> = ArrayList()
        var songlistNameShow: ArrayList<String?> = ArrayList()
        var avatarSongShow: ArrayList<String?> = ArrayList()
        var songAuthorShow: ArrayList<String?> = ArrayList()
        var source: ArrayList<String?> = ArrayList()
        var duration: ArrayList<String?> = ArrayList()
        //var songListSongsShow: ArrayList<String?> = ArrayList()


        try {
            val obj = JSONObject(loadJSONFromAssets(context))
            val userArray = obj.getJSONArray("songs")

            for (i in 0 until userArray.length()) {

                val songDetailShow = userArray.getJSONObject(i)
                //val songListsDetail = userArray.getJSONObject(i)
                Log.i("JsonUtils", "playlistID F  $songDetailShow")

                songListIDShow.add(songDetailShow.getString("songId"))
                songlistNameShow.add(songDetailShow.getString("songName"))
                avatarSongShow.add(songDetailShow.getString("avatar"))
                songAuthorShow.add(songDetailShow.getString("authorName"))
                source.add(songDetailShow.getString("source"))
                duration.add(songDetailShow.getString("duration"))
                //songListSongsShow.add(songDetailShow.getString("songList"))


                val  songs = duration[i]?.let {
                    Song(songListIDShow[i], songlistNameShow[i],songAuthorShow[i],avatarSongShow[i],source[i],
                        it
                    )
                }

                Log.i("JsonUtils", " loadSongs is 44")


                if (songs != null) {
                    songList.add(songs)
                }
                Log.i("JsonUtils", "ID song " + songList[i].name)
            }
        }
        catch (e: JSONException) {
            Log.i("JsonUtils", " loadplaylist errors")
            e.printStackTrace()
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


        private const val NAME_SONG = "songName"
        private const val KEY_ID_SONG = "songId"
        private const val KEY_AVATAR_SONG = "avatar"
        private const val AUTHOR_NAME_SONG = "authorName"
        private const val SOURCE_SONG = "source"
        private const val DURATION = "duration"



    }
}