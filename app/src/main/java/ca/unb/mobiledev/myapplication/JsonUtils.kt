package ca.unb.mobiledev.myapplication

import android.content.Context
import android.content.ContextWrapper
import android.os.Environment
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.util.*


var playlistI = 0 //auto-increment playlist ID
var songI = 0

class JsonUtils(context: Context) {
    private lateinit var songList: ArrayList<Song>
    private lateinit var playlistList: ArrayList<Playlist> //each playlist in this list has a songList
    private var fileName:String = "Data.json"
    private lateinit var filePath: String//??? Possibly delete
    private lateinit var externalFile: File
    lateinit var playlist: Playlist

    init {
        processJSON(context)
        loadPlaylist(context)
       //var newPlaylist:Playlist = Playlist("9",  "testing","@tools:sample/avatars", null)
    //    addPlaylistToJSONFile(newPlaylist,context)
        //  loadSongList(context)
        //  getPlaylist(context, playlistList)
             // initializeSongList(context)
       // initializePlaylistList(context)
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
                val playlist: Playlist = Playlist.Builder()
                    .id(curr.getString(KEY_ID)).name(curr.getString(NAME)).avatar(
                        curr.getString(
                            KEY_AVATAR
                        )
                    ).songList(songList)
                    .build()
                playlistList.add(playlist)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        try {

            val jsonObject = JSONObject(Objects.requireNonNull(loadJSONFromAssetsSongs(context)))
            val jsonArray = jsonObject.getJSONArray(SONGLIST)
            for (i in 0 until jsonArray.length()) {
                var curr = jsonArray.getJSONObject(i)
                val songlists: Song = Song.Builder()
                    .id(curr.getString(KEY_ID_SONG)).name(curr.getString(NAME_SONG)).avatar(
                        curr.getString(
                            KEY_AVATAR_SONG
                        )
                    ).authorName(curr.getString(AUTHOR_NAME_SONG)).source(
                        curr.getString(
                            SOURCE_SONG
                        )
                    ).authorName(curr.getString(DURATION))
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

    fun getPlaylistSize(): Int? {

        return playlistList.size
    }

    fun getSongSize(): Int? {

        return songList.size
    }


    fun getSong(context: Context, songId: String): Song {
        return songList[songId.toInt()]
    }

    fun loadJSONFromAssets(context: Context): String? {//
       // Log.i("Hellos", context.assets.list("").toString())

        val contextWrapper = ContextWrapper(context)
        val assetsPath = contextWrapper.filesDir
        val file = File(assetsPath, "playlistData.json")

        var string = file.inputStream().readBytes().toString(Charsets.UTF_8)
        Log.i("JsonUtils", "string " + string)
     return string

    }

    fun loadJSONFromAssetsSongs(context: Context): String? {//
        // Log.i("Hellos", context.assets.list("").toString())

        val contextWrapper = ContextWrapper(context)
        val assetsPath = contextWrapper.filesDir
        val file = File(assetsPath, "Data.json")

        var string = file.inputStream().readBytes().toString(Charsets.UTF_8)
        Log.i("JsonUtils", "string " + string)

        return string
    }




    fun loadPlaylist(context: Context) {//making objects from the info in the Data.json file

        var playlistID: ArrayList<String?> = ArrayList()
        var playlistNameShow: ArrayList<String?> = ArrayList()
        var avatarShow: ArrayList<String?> = ArrayList()
        var songPlaylistShow: ArrayList<String?> = ArrayList()
        var testing: ArrayList<String?> = ArrayList()

        loadSongList(context) //initilizes songlist
        var songListOnly: ArrayList<Song> = ArrayList<Song>()

        var newSongs:Song = Song("1","testing one two " ,"tester","@tools:sample/avatars",  "ABC","00:03:40" )
        songList.add(newSongs)
        Log.i("JsonUtils", "songList message " + songList.toString())
        try {


            val obj = JSONObject(loadJSONFromAssets(context))
           // val objTwo = JSONObject(loadJSONFromAssetsSongs(context))
            val userArray = obj.getJSONArray("playlists")
           // val userArraySong = obj.getJSONArray("songs")

            Log.i("JsonUtils", "songList " + userArray.length())

            for (i in 0 until userArray.length() -2) {

                val playlistDetail = userArray.getJSONObject(i)
               // val songListsDetail = userArraySong.getJSONObject(i)
                Log.i("JsonUtils", "playlistID $playlistDetail")

                playlistID.add(playlistDetail.getString("id"))
                playlistNameShow.add(playlistDetail.getString("name"))
                avatarShow.add(playlistDetail.getString("avatar"))
                testing.add(playlistDetail.getString("songList"))
                //songPlaylistShow.add(songListsDetail.getString("songId"))


             //   Log.i("JsonUtils", "songList adding with melissa " + songList[i].authorName)

                val playlist = Playlist(playlistID[i], playlistNameShow[i], avatarShow[i], songList)



                playlistList.add(playlist)
                Log.i("JsonUtils", "songList adding with melissa test " + playlistList[i].name)

            }
        } catch (e: JSONException) {
            Log.i("JsonUtils", " loadplaylist error")
            e.printStackTrace()
        }


    }

    fun addPlaylistToJSONFile(playlistToAdd: Playlist, context: Context) {
        val contextWrapper = ContextWrapper(context)
        val assetsPath = contextWrapper.filesDir
        val file = File(assetsPath, "playlistData.json")
        val json = JSONObject()

        val gson = Gson()
        var oldContent = " "
        var counter = 0

        var mixed = " "
        Log.i("JsonUtils", " playlistList sizess outsidesss" + playlistList.size)

        for (i in 0 until playlistList.size ) {
            Log.i("JsonUtils", " helpss playlistList sizess in i " + i)
            if(i==0){
                oldContent = oldContent + " " + gson.toJson(playlistList[i])
                Log.i("JsonUtils", " helps playlistList sizess in " + oldContent)

            }else{

                oldContent = oldContent + " , " + gson.toJson(playlistList[i])
                Log.i("JsonUtils", " helpss playlistList size second out " + oldContent)

            }

        }


        try {
            PrintWriter(FileWriter(file)).use {
                Log.i("JsonUtils", " GOOD")
                val jsonString = gson.toJson(playlistToAdd)
                Log.i("JsonUtils", " sizeee " + playlistList.size)
      if(playlistList.size>0){
          mixed = oldContent + ", " + jsonString
          Log.i("JsonUtils", " helpss mixed feelings " + mixed)

      }else{
         mixed = oldContent  + jsonString
      Log.i("JsonUtils", "helpss  mixed feelings two " + mixed)
       }


                Log.i("JsonUtils", " mixed " + mixed)
                Log.i("JsonUtils", " GOOD " + jsonString.toString())

                it.write("{\"playlists\": [ $mixed ]}")
                counter =1
            }
        } catch (e: Exception) {
            Log.i("JsonUtils", " ERROR CATCH")
            e.printStackTrace()
        }


    }






//    @Throws(Exception::class)
//    fun convertStreamToString(`is`: InputStream?): String {
//        val reader = BufferedReader(InputStreamReader(`is`))
//        val sb = StringBuilder()
//        var line: String? = null
//        while (reader.readLine().also { line = it } != null) {
//            sb.append(line).append("\n")
//        }
//        return sb.toString()
//    }

    fun addSongToJSONFile(songAdd: Song, context: Context) {
        val contextWrapper = ContextWrapper(context)
        val assetsPath = contextWrapper.filesDir
        val file = File(assetsPath, "Data.json")


        val gson = Gson()
        var oldContent = " "
        var counter = songList.size

        var mixed = " "

        for (i in 0 until songList.size) {
            if(songList[i].id?.toInt()==1){
                oldContent = oldContent + " " + gson.toJson(songList[i])
            }else{
                oldContent = oldContent + " , " + gson.toJson(songList[i])

            }
        }
        try {
            PrintWriter(FileWriter(file)).use {
                Log.i("JsonUtils", " GOOD")
                val jsonString = gson.toJson(songAdd)
                if(songList.size>0){
                    mixed = oldContent + ", " + jsonString
                }else{
                    mixed = oldContent  + jsonString
                }
                Log.i("JsonUtils", " mixed " + mixed)
                it.write("{\"songs\": [ $mixed ]}")
                counter =1
            }
        } catch (e: Exception) {
            Log.i("JsonUtils", " ERROR CATCH")
            e.printStackTrace()
        }
       songList.add(songAdd)
        Log.i("JsonUtils", " load songlist + " + songList[0].name )

    }






    fun loadSongList(context: Context) {

        var songListIDShow: ArrayList<String?> = ArrayList()
        var songlistNameShow: ArrayList<String?> = ArrayList()
        var avatarSongShow: ArrayList<String?> = ArrayList()
        var songAuthorShow: ArrayList<String?> = ArrayList()
        var source: ArrayList<String?> = ArrayList()
        var duration: ArrayList<String?> = ArrayList()
        //var songListSongsShow: ArrayList<String?> = ArrayList()


        try {
            val obj = JSONObject(loadJSONFromAssetsSongs(context))
            val userArray = obj.getJSONArray("songs")

            for (i in 0 until userArray.length()-1) {

                val songDetailShow = userArray.getJSONObject(i)
                //val songListsDetail = userArray.getJSONObject(i)
                Log.i("JsonUtils", "playlistID F  $songDetailShow")

                songListIDShow.add(songDetailShow.getString("id"))
                songlistNameShow.add(songDetailShow.getString("name"))
                avatarSongShow.add(songDetailShow.getString("avatar"))
                songAuthorShow.add(songDetailShow.getString("authorName"))
                source.add(songDetailShow.getString("source"))
                duration.add(songDetailShow.getString("duration"))
                //songListSongsShow.add(songDetailShow.getString("songList"))


                val songs = duration[i]?.let {
                    Song(
                        songListIDShow[i],
                        songlistNameShow[i],
                        songAuthorShow[i],
                        avatarSongShow[i],
                        source[i],
                        it
                    )
                }

                Log.i("JsonUtils", " loadSongs is 44")


                if (songs != null) {
                    songList.add(songs)
                }
                Log.i("JsonUtils", "ID song " + songList[i].name)
            }
        } catch (e: JSONException) {
            Log.i("JsonUtils", " loadplaylist errors")
            e.printStackTrace()
        }


    }

    fun createDirectory(){
       externalFile = File(Environment.getExternalStorageDirectory().absolutePath + "/Download/Data.json")
        var dir: File = File(Environment.getExternalStorageDirectory().absolutePath + "/Download")
        dir.mkdirs()
        externalFile = File(dir, "Data.json")
    }

//    fun readDataFromJson(file:File) : String{
//        var data:String = ""
//
//        return data
//    }

//    fun writeStringToUri(playlistToAdd: Playlist) {//we have get (in RecordActivity) but not write. This will be write
//
//        val gson = Gson()
//        Log.i("cho", externalFile.path)
//        var previousJson: String? = readDataFromJson(externalFile)
//
//        val playlists: MutableList<Playlist> = gson.fromJson<MutableList<Playlist>>(
//            previousJson,
//            object : TypeToken<List<Playlist?>?>() {}.type
//        )//get the information from .json file as a list of Playlists
//        Log.i("heybo", previousJson.toString())
//
//// set properties
//        playlists.add(playlistToAdd)// add new info to original list
//
//        var jsonString = gson.toJson(playlists)//convert to json
//
//        try {
//            PrintWriter(FileWriter(externalFile)).use {//add to .json file
//
//                it.write(jsonString.toString())//MAYBE FIND A METHOD WHERE YOU DON'T WRITE TO IT IN STRING FORM?
//                Log.i("JsonUtils", " GOOD " + jsonString.toString())
//            }
//        } catch (e: Exception) {
//            Log.i("JsonUtils", " ERROR CATCH WEE")
//            e.printStackTrace()
//       }



   // }

//    private fun initializeSongList(context: Context) {
//        val jsonObject = JSONObject(Objects.requireNonNull(loadJSONFromAssetsSongs(context)))
//
//        val jsonArray = jsonObject.getJSONArray(ALLSONGS)
//        for (i in 0 until jsonArray.length()) {
//            val item: JSONObject = jsonArray[i] as JSONObject
//            val id  = item.getString("id")
//            val name = item.getString("name")
//            val authorName = item.getString("authorName")
//            val avatar = item.getString("avatar")
//            val source = item.getString("source")
//            val duration = item.getString("duration")
//            val song = Song(id, name, authorName, avatar, source,duration )
//            songList.add(song)
//        }
//    }

//    private fun initializePlaylistList(context: Context) {
//        val jsonObject = JSONObject(Objects.requireNonNull(loadJSONFromAssets(context)))
//
//        val jsonArray = jsonObject.getJSONArray(PLAYLISTS)
//        for (i in 0 until jsonArray.length()) {
//            val item: JSONObject = jsonArray[i] as JSONObject
//            val id  = item.getString("id")
//            val name = item.getString("name")
//            val avatar = item.getString("avatar")
//            val songsId  = item.getJSONArray("songList")
//            var playlist = Playlist(id, name, avatar, songList)
////            for (i in 0 until songsId.length()) {
////                playlist.addSong(songList[(songsId[i] as Int)-1])
////            }
//            playlistList.add(playlist)
//        }
//    }


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


        private const val NAME_SONG = "name"
        private const val KEY_ID_SONG = "id"
        private const val KEY_AVATAR_SONG = "avatar"
        private const val AUTHOR_NAME_SONG = "authorName"
        private const val SOURCE_SONG = "source"
        private const val DURATION = "duration"


    }
}