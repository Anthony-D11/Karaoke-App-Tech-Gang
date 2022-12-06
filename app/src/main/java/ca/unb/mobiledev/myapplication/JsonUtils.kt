package ca.unb.mobiledev.myapplication

import android.content.Context
import android.content.ContextWrapper
import android.os.Environment
import android.util.Log
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

    private var jsonFile: File? = null


    init {
        songList = ArrayList()
        playlistList = ArrayList()
        val contextWrapper = ContextWrapper(context)
        val assetsPath = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        jsonFile = File(assetsPath, "Data.json")
        if (jsonFile!!.exists()) {
            processJSON(context)
        }
        else initializeJson(context)
    }

    private fun initializeJson(context: Context) {

        jsonFile!!.createNewFile()
        val emptyJson = JSONObject()
        val emptyArray = JSONArray()
        emptyJson.put(ALLSONGS, emptyArray)
        emptyJson.put(PLAYLISTS, emptyArray)
        val bufferedWriter = jsonFile!!.bufferedWriter()
        bufferedWriter.use { it.write(emptyJson.toString()) }
        Log.i("Hello", "here")

        val mp3List = getAllMp3(Environment.getExternalStorageDirectory().absolutePath)

        for (i in 0 until mp3List!!.size) {
            addSongToJSONFile(mp3List[i], context)
            addSongtoSongListObject(mp3List[i])
        }
    }

    private fun addSongtoSongListObject(songToAdd: Song): Boolean {
        for (song in songList) {
            if (song.source == songToAdd.source) return false
        }
        songList.add(songToAdd)
        return true
    }

    private fun processJSON(context: Context) {
        val jsonObject = JSONObject(Objects.requireNonNull(loadJSONFromAssets(context)))
        try {
            val jsonArray = jsonObject.getJSONArray(ALLSONGS)
            for (i in 0 until jsonArray.length()) {
                val curr = jsonArray.getJSONObject(i)
                val song = Song.Builder(curr.getString(NAME), curr.getString(NICKNAME), curr.getString(AUTHOR), "", "", curr.getString(SOURCE)).build()
                addSongtoSongListObject(song)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        try {
            val jsonArray = jsonObject.getJSONArray(PLAYLISTS)

            for (i in 0 until jsonArray.length()) {
                val curr = jsonArray.getJSONObject(i)
                val playlist = Playlist.Builder(
                    curr.getString(NAME), curr.getString(AVATAR),
                    curr.getString(BACKGROUND), ArrayList()
                ).build()

                for (j in 0 until curr.getJSONArray(SONGLIST).length()) {
                    val songName = curr.getJSONArray(SONGLIST)[j]
                    playlist.addSong(getSong(songName as String)!!)
                }
                playlistList.add(playlist)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun getAllMp3(rootPath: String): ArrayList<Song>? {
        val res = ArrayList<Song>()
        try {
            val rootFolder = File(rootPath)
            val files: Array<File> = rootFolder.listFiles()
            for (file in files) {
                if (file.isDirectory) {
                    val temp = getAllMp3(file.absolutePath)
                    if (temp != null) {
                        res.addAll(temp)
                    } else {
                        break
                    }
                }
                else if (filterMp3(file)) {
                    val song = Song.Builder(file.name, "", "", "", "", file.absolutePath).build()
                    Log.i("Hello", file.name)
                    res.add(song)
                }
            }
            return res
        } catch (e: Exception) {
            return null
        }
    }

    private fun filterMp3(file: File): Boolean {
        val size = file.length() / 1024 / 1024
        if (!file.name.endsWith(".mp3")) return false
        if (size < 1) return false
        return true
    }

    fun getPlaylist(playlistName: String?): Playlist? {
        for (i in 0 until playlistList.size) {
            if (playlistList[i].name == playlistName) {
                return playlistList[i]
            }
        }
        return null
    }

    fun getSongList(playlistName: String?): ArrayList<Song> {
        val res = ArrayList<Song>()
        Log.i("DAO", playlistName!!)
        for (i in 0 until playlistList.size) {
            if (playlistName == playlistList[i].name) {
                Log.i("DAO", playlistName)
                for (j in 0 until playlistList[i].songList.size) {

                    val song = getSong(playlistList[i].songList[j])

                    res.add(song!!)
                }
                break
            }
        }
        return res
    }

    fun getPlaylistSize(): Int? {

        return playlistList.size
    }

    fun getSongSize(): Int? {

        return songList.size
    }

    fun getAllSongs(): ArrayList<Song> {
        return songList
    }

    fun getSong(songName: String): Song? {
        for (i in 0 until songList.size) {
            if (songList[i].realName == songName) {
                Log.i("DAO", songList[i].source)
                return songList[i]
            }
        }
        return null
    }

    private fun loadJSONFromAssets(context: Context): String {
        val content = jsonFile!!.inputStream().readBytes().toString(Charsets.UTF_8)
        return content
    }


    fun addSongToPlaylistObject(songToAdd: Song, playlistName: String?): Boolean{
        for (i in 0 until playlistList.size) {
            if (playlistName == playlistList[i].name) {
                return playlistList[i].addSong(songToAdd)
            }
        }
        return false
    }

    fun addNickNameForSongListObject(realName: String, nickName: String) {
        for (i in 0 until songList.size) {
            if (songList[i].realName == realName) {
                songList[i].nickName = nickName
                break
            }
        }
    }

    fun addNickNameForSongJson(realName: String, nickName: String) {
        val bufferedReader = jsonFile!!.bufferedReader()
        val result = bufferedReader.use { it.readText() }
        val oldJsonContent = JSONObject(result)
        val oldSongList = oldJsonContent.getJSONArray(ALLSONGS)

        for (i in 0 until oldSongList.length()) {
            val song = oldSongList.getJSONObject(i)
            if (song.getString(NAME) == realName) {
                oldJsonContent.getJSONArray(ALLSONGS).getJSONObject(i).put(NICKNAME, nickName)
            }
        }

        val bufferedWriter = jsonFile!!.bufferedWriter()
        bufferedWriter.use { it.write(oldJsonContent.toString()) }
    }

    fun addAuthorForSongListObject(realName: String, author: String) {
        for (i in 0 until songList.size) {
            if (songList[i].realName == realName) {
                songList[i].authorName = author
                break
            }
        }
    }

    fun addAuthorForSongJson(realName: String, author: String) {
        val bufferedReader = jsonFile!!.bufferedReader()
        val result = bufferedReader.use { it.readText() }
        val oldJsonContent = JSONObject(result)
        val oldSongList = oldJsonContent.getJSONArray(ALLSONGS)

        for (i in 0 until oldSongList.length()) {
            val song = oldSongList.getJSONObject(i)
            if (song.getString(NAME) == realName) {
                oldJsonContent.getJSONArray(ALLSONGS).getJSONObject(i).put(AUTHOR, author)
            }
        }

        val bufferedWriter = jsonFile!!.bufferedWriter()
        bufferedWriter.use { it.write(oldJsonContent.toString()) }
    }

    fun addPlaylistToJSONFile(playlistToAdd: Playlist, context: Context): Boolean {
        val newPlaylistJsonContent = JSONObject()
        newPlaylistJsonContent.put(NAME, playlistToAdd.name)
        newPlaylistJsonContent.put(AVATAR, playlistToAdd.avatar)
        newPlaylistJsonContent.put(BACKGROUND, playlistToAdd.background)
        newPlaylistJsonContent.put(SONGLIST, JSONArray(playlistToAdd.songList))

        val bufferedReader = jsonFile!!.bufferedReader()
        val result = bufferedReader.use { it.readText() }
        val oldJsonContent = JSONObject(result)
        val oldPlaylist = oldJsonContent.getJSONArray(PLAYLISTS)
        for (i in 0 until oldPlaylist.length()) {
            if (oldPlaylist.getJSONObject(i).getString(NAME) == playlistToAdd.name) return false
        }
        oldPlaylist.put(newPlaylistJsonContent)
        oldJsonContent.put(PLAYLISTS, oldPlaylist)

        val bufferedWriter = jsonFile!!.bufferedWriter()
        bufferedWriter.use { it.write(oldJsonContent.toString()) }
        return true
    }

    fun addPlaylistToPlaylistListObject(playlistToAdd: Playlist): Boolean {
        for (playlist in playlistList) {
            if (playlist.name == playlistToAdd.name) return false
        }
        playlistList.add(playlistToAdd)
        return true
    }

    fun addSongToJSONFile(songAdd: Song, context: Context): Boolean {
        val newSongJsonContent = JSONObject()
        newSongJsonContent.put(NAME, songAdd.realName)
        newSongJsonContent.put(NICKNAME, songAdd.nickName)
        newSongJsonContent.put(AUTHOR, songAdd.authorName)
        newSongJsonContent.put(DURATION, songAdd.duration)
        newSongJsonContent.put(AVATAR, songAdd.avatar)
        newSongJsonContent.put(SOURCE, songAdd.source)

        val bufferedReader = jsonFile!!.bufferedReader()
        val result = bufferedReader.use { it.readText() }
        val oldJsonContent = JSONObject(result)
        val oldSongList = oldJsonContent.getJSONArray(ALLSONGS)
        for (i in 0 until oldSongList.length()) {
            if (oldSongList.getJSONObject(i).getString(SOURCE) == songAdd.source) return false
        }
        oldSongList.put(newSongJsonContent)
        oldJsonContent.put(ALLSONGS, oldSongList)

        val bufferedWriter = jsonFile!!.bufferedWriter()
        bufferedWriter.use { it.write(oldJsonContent.toString()) }
        return true
    }

    fun addSongToPlaylistJson(songAdd: Song, playlistName: String?): Boolean {
        val bufferedReader = jsonFile!!.bufferedReader()
        val result = bufferedReader.use { it.readText() }
        val oldJsonContent = JSONObject(result)
        val oldPlaylist = oldJsonContent.getJSONArray(PLAYLISTS)
        for (i in 0 until oldPlaylist.length()) {
            val currPlaylist = oldPlaylist.getJSONObject(i)
            if (currPlaylist.getString(NAME) == playlistName) {
                val newSongName = songAdd.realName
                for (i in 0 until currPlaylist.getJSONArray(SONGLIST).length()) {
                    if (currPlaylist.getJSONArray(SONGLIST).getString(i) == songAdd.realName) return false
                }
                oldJsonContent.getJSONArray(PLAYLISTS).getJSONObject(i).getJSONArray(SONGLIST).put(oldJsonContent.getJSONArray(PLAYLISTS).getJSONObject(i).getJSONArray(SONGLIST).length(), newSongName)
            }
        }
        val bufferedWriter = jsonFile!!.bufferedWriter()
        bufferedWriter.use { it.write(oldJsonContent.toString()) }
        return true
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
        private const val NICKNAME = "nickName"
        private const val AVATAR = "avatar"
        private const val BACKGROUND = "background"


        private const val AUTHOR = "authorName"
        private const val SOURCE = "source"
        private const val DURATION = "duration"


    }
}