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
import kotlin.collections.ArrayList


class JsonUtils(context: Context) {
    private lateinit var songList: ArrayList<Song>
    private lateinit var playlistList: ArrayList<Playlist> //each playlist in this list has a songList
    private var fileName: String = "Data.json"
    private lateinit var filePath: String//??? Possibly delete
    private lateinit var externalFile: File
    lateinit var playlist: Playlist

    init {
        processJSON(context)
        //loadPlaylist(context)
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
        val jsonObject = JSONObject(Objects.requireNonNull(loadJSONFromAssets(context)))
        try {
            val jsonArray = jsonObject.getJSONArray(ALLSONGS)
            for (i in 0 until jsonArray.length()) {
                val curr = jsonArray.getJSONObject(i)
                val songlists: Song = Song.Builder()
                    .id(curr.getString(KEY_ID_SONG)).name(curr.getString(NAME)).avatar(
                        curr.getString(KEY_AVATAR_SONG)
                    ).authorName(curr.getString(AUTHOR_NAME_SONG)).source(
                        curr.getString(SOURCE_SONG)
                    ).authorName(curr.getString(DURATION))
                    .build()
                songList.add(songlists)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        try {
            val jsonArray = jsonObject.getJSONArray(PLAYLISTS)

            for (i in 0 until jsonArray.length()) {
                val curr = jsonArray.getJSONObject(i)
                val songList = ArrayList<String>()
                for (j in 0 until curr.getJSONArray(SONGLIST).length()) {
                    val songId = curr.getJSONArray(SONGLIST)[j]
                    songList.add(getSong(songId as String).id!!)
                }
                val playlist: Playlist = Playlist.Builder(
                    curr.getString(NAME), curr.getString(KEY_AVATAR),
                    curr.getString(KEY_BACKGROUND), songList
                ).build()

                playlistList.add(playlist)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }


    }


    fun getPlaylist(playlistName: String?): Playlist {
        var index = -1;
        for (i in 0 until playlistList.size) {
            if (playlistList[i].name == playlistName) {
                index = i
                break
            }
        }
        return playlistList[index]
    }

    fun getSongList(playlistName: String?): ArrayList<Song> {
        val res = ArrayList<Song>()
        var index = -1;
        for (i in 0 until playlistList.size) {
            if (playlistList[i].name == playlistName) {
                index = i
                break
            }
        }

        for (i in 0 until playlistList[index].songList.size) {
            val songId = playlistList[index].songList[i]
            res.add(getSong(songId))
        }
        return res
    }

    fun getPlaylistSize(): Int? {

        return playlistList.size
    }

    fun getSongSize(): Int? {

        return songList.size
    }


    fun getSong(songId: String): Song {
        return songList[songId.toInt()]
    }

    private fun loadJSONFromAssets(context: Context): String {//
        val contextWrapper = ContextWrapper(context)
        val assetsPath = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val file = File(assetsPath, "Data.json")
        if (!file.exists()) {
            file.createNewFile()
            val emptyJson = JSONObject()
            val emptyArray = JSONArray()
            emptyJson.put("songs", emptyArray)
            emptyJson.put("playlists", emptyArray)
            val bufferedWriter = file.bufferedWriter()
            bufferedWriter.use { it.write(emptyJson.toString()) }
        }
        val content = file.inputStream().readBytes().toString(Charsets.UTF_8)
        Log.i("JsonUtils", content)
        return content
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



    /*
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
    */

    fun addPlaylistToJSONFile(playlistToAdd: Playlist, context: Context) {
        val contextWrapper = ContextWrapper(context)
        val assetsPath = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val file = File(assetsPath, DATA_JSON_FILE)
        val newPlaylistJsonContent = JSONObject()
        newPlaylistJsonContent.put(NAME, playlistToAdd.name)
        newPlaylistJsonContent.put(KEY_AVATAR, playlistToAdd.avatar)
        newPlaylistJsonContent.put(KEY_BACKGROUND, playlistToAdd.background)
        newPlaylistJsonContent.put(SONGLIST, JSONArray(playlistToAdd.songList))

        val bufferedReader = file.bufferedReader()
        val result = bufferedReader.use { it.readText() }
        val oldJsonContent = JSONObject(result)
        val oldPlaylist = oldJsonContent.getJSONArray(PLAYLISTS)
        oldPlaylist.put(newPlaylistJsonContent)
        oldJsonContent.put(PLAYLISTS, oldPlaylist)

        val bufferedWriter = file.bufferedWriter()
        bufferedWriter.use { it.write(oldJsonContent.toString()) }

        playlistList.add(playlistToAdd)
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
        val assetsPath = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val file = File(assetsPath, DATA_JSON_FILE)
        val newSongJsonContent = JSONObject()
        newSongJsonContent.put(KEY_ID, songAdd.id)
        newSongJsonContent.put(NAME, songAdd.name)
        newSongJsonContent.put(AUTHOR_NAME_SONG, songAdd.authorName)
        newSongJsonContent.put(DURATION, songAdd.duration)
        newSongJsonContent.put(KEY_AVATAR, songAdd.avatar)
        newSongJsonContent.put(SOURCE_SONG, songAdd.source)

        val bufferedReader = file.bufferedReader()
        val result = bufferedReader.use { it.readText() }
        val oldJsonContent = JSONObject(result)
        val oldSongList = oldJsonContent.getJSONArray(ALLSONGS)
        oldSongList.put(newSongJsonContent)
        oldJsonContent.put(ALLSONGS, oldSongList)

        val bufferedWriter = file.bufferedWriter()
        bufferedWriter.use { it.write(oldJsonContent.toString()) }
        songList.add(songAdd)
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
        private const val KEY_BACKGROUND = "background"



        private const val KEY_ID_SONG = "id"
        private const val KEY_AVATAR_SONG = "avatar"
        private const val AUTHOR_NAME_SONG = "authorName"
        private const val SOURCE_SONG = "source"
        private const val DURATION = "duration"


    }
}