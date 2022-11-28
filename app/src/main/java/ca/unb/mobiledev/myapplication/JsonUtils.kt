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

            val jsonObject = JSONObject(Objects.requireNonNull(loadJSONFromAssets(context)))
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
    //   }
//        var string: String? = ""
//        try {
//            val inputStream: InputStream =
//            var size: Int = inputStream.available()
//            var buffer = ByteArray(size)
//            inputStream.read(buffer)
//            string = String(buffer)
//            Log.i("JsonUtils", "file was opened")
//
//        } catch (e: IOException) {
//            Log.i("JsonUtils", "file did NOT open")
//            e.printStackTrace()
//        }
//        return string
    }

    fun loadPlaylist(context: Context) {//making objects from the info in the Data.json file

        var playlistID: ArrayList<String?> = ArrayList()
        var playlistNameShow: ArrayList<String?> = ArrayList()
        var avatarShow: ArrayList<String?> = ArrayList()
        var songPlaylistShow: ArrayList<String?> = ArrayList()
        var testing: ArrayList<String?> = ArrayList()

        loadSongList(context) //initilizes songlist
        var songListOnly: ArrayList<Song> = ArrayList<Song>()


        try {
            //
//            val contextWrapper = ContextWrapper(context)
//            val assetsPath = contextWrapper.filesDir
//            val file = File(assetsPath, "playlistData.json")

            val obj = JSONObject(loadJSONFromAssets(context))

            val userArray = obj.getJSONArray("playlists")
            //val userArraySong = obj.getJSONArray("songs")

            for (i in 0 until userArray.length()) {


                val playlistDetail = userArray.getJSONObject(i)
               // val songListsDetail = userArraySong.getJSONObject(i)
                Log.i("JsonUtils", "playlistID $playlistDetail")

                playlistID.add(playlistDetail.getString("playlistId"))
                playlistNameShow.add(playlistDetail.getString("playlistName"))
                avatarShow.add(playlistDetail.getString("avatar"))
                testing.add(playlistDetail.getString("songList"))
                //songPlaylistShow.add(songListsDetail.getString("songId"))

                Log.i("JsonUtils", "songList adding with melissa " + songList[i].authorName)

                val playlist = Playlist(playlistID[i], playlistNameShow[i], avatarShow[i], songList)



                playlistList.add(playlist)
                Log.i("JsonUtils", "songList adding with melissa test " + playlistList[i].name)

            }
        } catch (e: JSONException) {
            Log.i("JsonUtils", " loadplaylist errors")
            e.printStackTrace()
        }


    }

    fun addPlaylistToJSONFile(playlistToAdd: Playlist, context: Context) {
        val contextWrapper = ContextWrapper(context)
        val assetsPath = contextWrapper.filesDir
        val file = File(assetsPath, "playlistData.json")
        val json = JSONObject()

//try and add more than one playlist here
       // Log.i("JsonUtils", " playlist help " + playlistList[1].name.toString())
        val gson = Gson()
        var oldContent = " "
        var counter = playlistList.size

        var mixed = " "

        for (i in 0 until playlistList.size) {
            if(playlistList[i].id?.toInt()==0){
                oldContent = oldContent + " " + gson.toJson(playlistList[i])

            }else{
                oldContent = oldContent + " , " + gson.toJson(playlistList[i])

            }

        }


        try {
            PrintWriter(FileWriter(file)).use {
                Log.i("JsonUtils", " GOOD")
                val jsonString = gson.toJson(playlistToAdd)

       if(playlistList.size>0){
           mixed = oldContent + ", " + jsonString
       }else{
           mixed = oldContent  + jsonString
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

       // val contextWrapper = ContextWrapper(context)
     //  val assetsPath = contextWrapper.filesDir
       // val file = File(assetsPath, "Data.json")
      // Log.i("user.dir path", file.path)

       // val gson = Gson()

        //var jsonString = gson.toJson(playlistToAdd)

        //val fl = File(file.path)
      //  val fin = FileInputStream(fl)
       // val ret: String = convertStreamToString(fin)
        //Make sure you close all streams.
       // fin.close()

//        file.writeText(jsonString)
//
//       var output: Writer? = null
//       try {
//            var temp: FileWriter = FileWriter(file)
//           Log.i("jambala2", "wee")
//            output = BufferedWriter(temp)
//
//            output.write(jsonString.toString())
//            output.close()
//            Log.i("Donda1", "hey")
//        } catch (e:IOException) {
//           Log.i("Donda", e.message.toString())
//        }
    }

    @Throws(Exception::class)
    fun convertStreamToString(`is`: InputStream?): String {
        val reader = BufferedReader(InputStreamReader(`is`))
        val sb = StringBuilder()
        var line: String? = null
        while (reader.readLine().also { line = it } != null) {
            sb.append(line).append("\n")
        }
        return sb.toString()
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
    // work here try and figure out why its not initializing INT error


    // where do I call this from like the page.
    fun loadSongList(context: Context) {

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

    fun readDataFromJson(file:File) : String{
        var data:String = ""

        return data
    }

    fun writeStringToUri(playlistToAdd: Playlist) {//we have get (in RecordActivity) but not write. This will be write

        val gson = Gson()
        Log.i("cho", externalFile.path)
        var previousJson: String? = readDataFromJson(externalFile)

        val playlists: MutableList<Playlist> = gson.fromJson<MutableList<Playlist>>(
            previousJson,
            object : TypeToken<List<Playlist?>?>() {}.type
        )//get the information from .json file as a list of Playlists
        Log.i("heybo", previousJson.toString())

// set properties
        playlists.add(playlistToAdd)// add new info to original list

        var jsonString = gson.toJson(playlists)//convert to json

        try {
            PrintWriter(FileWriter(externalFile)).use {//add to .json file

                it.write(jsonString.toString())//MAYBE FIND A METHOD WHERE YOU DON'T WRITE TO IT IN STRING FORM?
                Log.i("JsonUtils", " GOOD " + jsonString.toString())
            }
        } catch (e: Exception) {
            Log.i("JsonUtils", " ERROR CATCH WEE")
            e.printStackTrace()
       }


//        externalFile = File(Environment.getExternalStorageDirectory().absolutePath + "/Download/Data.json")
//        var dir: File = File(Environment.getExternalStorageDirectory().absolutePath + "/Download")
//        dir.mkdirs()
//        externalFile = File(dir, "Data.json")
//
//        Log.i("JsonUtils", " GOOD")
//        val json = JSONObject()
//
//        try {
//            PrintWriter(FileWriter(externalFile)).use {
//
//                val gson = Gson()
//                val jsonString = gson.toJson(playlistToAdd)
//
//                it.write(jsonString.toString())
//                Log.i("JsonUtils", " GOOD " + jsonString.toString())
//            }
//        } catch (e: Exception) {
//            Log.i("JsonUtils", " ERROR CATCH WEE")
//            e.printStackTrace()
//       }
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