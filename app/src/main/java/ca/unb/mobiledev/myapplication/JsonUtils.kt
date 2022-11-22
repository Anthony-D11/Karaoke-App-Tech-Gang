package ca.unb.mobiledev.myapplication

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.util.*

var playlistI = 0 //auto-increment playlist ID
var songI = 0

class JsonUtils(context: Context) {
    private lateinit var songList: ArrayList<Song>
    private lateinit var playlistList: ArrayList<Playlist> //each playlist in this list has a songList

    lateinit var playlist :Playlist
    init {
       processJSON(context)
        loadPlaylist(context)
       // displayPlaylist(context)
      //  getPlaylist(context, playlistList)
//     initializeSongList(context)
       //initializePlaylistList(context)
//        val contextWrapper = ContextWrapper(context)
//       val music = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
//        val file: File  = File(music, "Perfect.")

        }


    private fun processJSON(context: Context) {
        // Initializes the lateinit values
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
    }



    fun getPlaylist(context: Context, playlistId: String?): ArrayList<Song>? {
        return playlistList[playlistId!!.toInt()].getSongList()
    }
//    private fun getSong(context: Context, songId: String): Song {
//        return songList[songId.toInt()]
//    }

     fun loadJSONFromAssets(context: Context): String? {//
        Log.i("Hellos", context.assets.list("").toString())
        var string: String? = ""
        try{
            val inputStream: InputStream = context.assets.open("Data.json")
            var size: Int = inputStream.available()
         var buffer = ByteArray(size)
            inputStream.read(buffer)
            string= String(buffer)
            Log.i("JsonUtils", "file was opened")

       } catch (e: IOException) {
            Log.i("JsonUtils", "file did NOT open")
         e.printStackTrace()
      }
        return string
    }

   fun loadPlaylist(context: Context){//making objects from the info in the Data.json file

//<<<<<<< Updated upstream
       var playlistID: ArrayList<String?> = ArrayList()
       var playlistNameShow: ArrayList<String?> = ArrayList()
       var avatarShow: ArrayList<String?> = ArrayList()
       var songPlaylistShow: ArrayList<String?> = ArrayList()
       try {
           val obj = JSONObject(loadJSONFromAssets(context))
           val userArray = obj.getJSONArray("playlists")

           for (i in 0 until userArray.length()) {
//=======
    fun addSongToPlaylist(songToAdd:Song){

    }

    fun addPlaylistToList(playlistToAdd:Playlist){//adds playlist to playlistList

    }

//>>>>>>> Stashed changes

               val playlistDetail = userArray.getJSONObject(i)
               val songListsDetail = userArray.getJSONObject(i)
               Log.i("JsonUtils", "playlistID $playlistDetail")

               playlistID.add(playlistDetail.getString("playlistId"))
               playlistNameShow.add(playlistDetail.getString("playlistName"))
               avatarShow.add(playlistDetail.getString("avatar"))
              songPlaylistShow.add(songListsDetail.getString("songList"))


              // this might not work because I need to fix the song playlist
                  //songList.add(songPlaylistShow[i])
                //songsId  = playlistDetail.getJSONArray("songList")
               val  playlist = Playlist(playlistID[i], playlistNameShow[i], avatarShow[i], null)
                  // for (i in 0 until songsId.length()) {
                     //  Log.i("JsonUtils", " helping is 1")
            // playlist.addSong(songList[(songsId[i] as Int)])
                   //    Log.i("JsonUtils", " helping is 2")

            //}


               Log.i("JsonUtils", " loadplaylist is 44")


               playlistList.add(playlist)
           }
       }
       catch (e: JSONException) {
           Log.i("JsonUtils", " loadplaylist errors")
           e.printStackTrace()
       }

       Log.i("JsonUtils", "ID " + playlistID[1])

   }

    fun getDataInPlaylistData(context: Context) {

        var jsonString: String = context.assets.open("playlistData.json")
            .bufferedReader()
            .use{ it.readText() }

        val listPlaylistType = object : TypeToken<List<Playlist>>() {}.type
        Log.i("Yoyo", Gson().fromJson(jsonString, listPlaylistType))
        //return Gson().fromJson(jsonString, listPlaylistType)

    }

//            fun initializeSongList(context: Context) {
//                val jsonObject = JSONObject(Objects.requireNonNull(loadJSONFromAssets(context)))
//
//                val jsonArray = jsonObject.getJSONArray(ALLSONGS)
//                for (i in 0 until jsonArray.length()) {
//                    val item: JSONObject = jsonArray[i] as JSONObject
//            val id  = item.getString("songId")
//            val name = item.getString("songName")
//            val authorName = item.getString("authorName")
//            val avatar = item.getString("avatar")
//            val source = item.getString("source")
//            val song = Song(id, name, authorName, avatar, source)
//            songList.add(song)
//        }
//    }

//<<<<<<< Updated upstream
    // work here try and figure out why its not initalizing INT error
//    fun initializePlaylistList(context: Context) {
//        val jsonObject = JSONObject(Objects.requireNonNull(loadJSONFromAssets(context)))
//
//        val jsonArray = jsonObject.getJSONArray(PLAYLISTS)
//        for (i in 0 until jsonArray.length()) {
//            val item: JSONObject = jsonArray[i] as JSONObject
//            val id  = item.getString("playlistId")
//            val name = item.getString("playlistName")
//            val avatar = item.getString("avatar")
//            val songsId  = item.getJSONArray("songList")
//            var playlist = Playlist(id, name, avatar, songList)
//            for (i in 0 until songsId.length()) {
//                playlist.addSong(songList[(songsId[i] as Int)-1])
//            }
//            playlistList.add(playlist)
//        }
//    }
//=======
    // work here try and figure out why its not initializing INT error
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
//>>>>>>> Stashed changes

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