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
  //  lateinit var playlist: Playlist
    private  val SonglistTwo = ArrayList<String>()
    private lateinit var arrayOne: ArrayList<Song>
    private lateinit var arrayTwo: ArrayList<Song>
    private lateinit var arrayThree: ArrayList<Song>
    private lateinit var arrayFour: ArrayList<Song>
    private lateinit var    arrayFive: ArrayList<Song>
//    private lateinit var arrayOne: ArrayList<Song>
//    private lateinit var arrayOne: ArrayList<Song>
//    private lateinit var arrayOne: ArrayList<Song>
//    private lateinit var arrayOne: ArrayList<Song>
//    private lateinit var arrayOne: ArrayList<Song>



    init {
        processJSON(context)
    }


    private fun processJSON(context: Context) {
        // Initializes the lateinit values
        songList = ArrayList<Song>()
        playlistList = ArrayList<Playlist>()
        arrayOne = ArrayList<Song>()
        arrayTwo = ArrayList<Song>()
        arrayThree = ArrayList<Song>()
        arrayFour = ArrayList<Song>()
        arrayFive = ArrayList<Song>()
        val jsonObject = JSONObject(Objects.requireNonNull(loadJSONFromAssets(context)))
        try {
            val jsonArray = jsonObject.getJSONArray(ALLSONGS)
            for (i in 0 until jsonArray.length()) {
                val curr = jsonArray.getJSONObject(i)
                val songlists: Song? = Song.Builder()
                    .id(curr.getString(KEY_ID_SONG)).name(curr.getString(NAME)).avatar(
                        curr.getString(KEY_AVATAR_SONG)
                    ).authorName(curr.getString(AUTHOR_NAME_SONG)).source(
                        curr.getString(SOURCE_SONG)
                    ).authorName(curr.getString(DURATION))
                    .playlistSong(curr.getString(PLAYLISTSONG)).build()
                if (songlists != null) {
                    songList.add(songlists)
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        try {
            val jsonArray = jsonObject.getJSONArray(PLAYLISTS)

            for (i in 0 until jsonArray.length()) {
                val curr = jsonArray.getJSONObject(i)
                val songList = ArrayList<String>()
                for (j in 0 until curr.getJSONArray(SONGLIST).length()-1) {
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
//        var index = -1;
//
//        for (i in 0 until playlistList.size) {
//            if (playlistList[i].name == playlistName) {
//                Log.i("JsonUtils", "ughh no " + playlistList[i].name)
//                Log.i("JsonUtirls", "ughhh " + songList[i].name)
//                    res.add(songList[i])
//                index = i
//
//            }
//        }


//        for (i in 0 until songList.size) {
//           if(playlistList[i].name==playlistName && playlistList[i].songList !=null){
//               Log.i("JsonUtils", "pleaseee " + playlistList[i].songList + " "+ playlistList[i].name)
//              for(j in 0 until songList.size){
//                  Log.i("JsonUtils", "pleaseee for loop " + playlistList[i].songList + " "+ playlistList[i].name)
//                  if(playlistList[j].songList[0] == songList[j].id.toString()){
//                      Log.i("JsonUtils", "pleaseee if " + playlistList[i].songList + " "+ playlistList[i].name)
//                      res.add(songList[j])
//                  }
//              }


          // }
            //}
//        }
//        for (i in 0 until playlistList[index].songList.size )  {
//            val songId = playlistList[index].songList[i]
//            res.add(getSong(songId))
//        }
      //  Log.i("JsonUtils", "pugger array one " +arrayOne[0].name)

      //somehow connect the arrays

//        var arrayOneString: String? = null
//        var arrayTwoString: String? = null
//        for (i in 0 until playlistList.size) {
//            if(i==0){
//                 arrayOneString = playlistList[i].name
//            }else if(i==1){
//                arrayTwoString = playlistList[i].name
//            }
//
//        }
        Log.i("JsonUtils", "songlist error " + songList.size)
        for (i in 0 until songList.size) {
            Log.i("JsonUtils", "songlist error for loop " + songList.size)
            if (playlistName == songList[i].playlistSong) {

                Log.i("JsonUtils", "bruh one")
                arrayOne.add(songList[i])
                Log.i("JsonUtils", "pugger if too " + arrayOne[0].name)
            }




        }

        return arrayOne
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
        Log.i("JsonUtils", "need " + content)
        return content
    }

    //need to add the playlist name with the song id
    //somehow connect it idk hopefully will figure that out inthe morning

        //fun addsongToPlaylist(playlistName : String, songId :String, newSong : Song ) {
       //     val newSongList = ArrayList<String>()
//
//            Log.i("JsonUtils", "does this work?" + songList )
//            //this is creating the songlist<String> that is supposed to be in playlist
//           // this is looking for the playlistName in the json fileplaylistList[i].name == playlistName
//
//            var index = -1;
//            for (i in 0 until playlistList.size) {
//                Log.i("JsonUtils", "playlist first for loop" + playlistList[i].songList )
//                if (playlistList[i].name == playlistName) {
//                 //   this is the going to be the songList<String> in the playlist
//                newSongList.add(songId)
//                  index = i
//                    break
//                }
//            }
//            //adding the songID to the songList
//            songList.add(newSong)
//
//           playlistList[index]= Playlist.Builder(playlistList[index].name, playlistList[index].avatar ,"", newSongList).build()
//            Log.i(" JsonUtils", "playlistBuilder " + playlistList[index].songList )
//            for (i in 0 until playlistList.size) {
//                Log.i("JsonUtils", "playlist second for loop" + playlistList[i].songList )
//            }
////
////            Log.i("JsonUtils", "songlist added " + songList[1].name)

      //  }


    fun addsongToPlaylist(playlistName : String, songId :String, newSong : Song, context: Context){

        addSongToJSONFile(newSong, context)


    }



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


    fun addSongToJSONFile(songAdd: Song, context: Context) {
        for (i in 0 until playlistList.size) {
            Log.i("JsonUtils", "playlist second for loop json file" + playlistList[i].songList )
            Log.i("JsonUtils", "playlist second for loop json file" + songList.size )
        }

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
        newSongJsonContent.put(PLAYLISTSONG, songAdd.playlistSong)

        val bufferedReader = file.bufferedReader()
        val result = bufferedReader.use { it.readText() }
        val oldJsonContent = JSONObject(result)
        val oldSongList = oldJsonContent.getJSONArray(ALLSONGS)
        oldSongList.put(newSongJsonContent)
        oldJsonContent.put(ALLSONGS, oldSongList)

        val bufferedWriter = file.bufferedWriter()
        bufferedWriter.use { it.write(oldJsonContent.toString()) }
        Log.i("JsonUtils", "YES "  )
        songList.add(songAdd)
        Log.i("JsonUtils", "YES PLEASE"  + songList.size)
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
        private const val PLAYLISTSONG = "playlistSong"


        private const val KEY_ID_SONG = "id"
        private const val KEY_AVATAR_SONG = "avatar"
        private const val AUTHOR_NAME_SONG = "authorName"
        private const val SOURCE_SONG = "source"
        private const val DURATION = "duration"


    }
}