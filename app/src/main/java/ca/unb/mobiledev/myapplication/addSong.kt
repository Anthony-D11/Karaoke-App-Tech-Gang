package ca.unb.mobiledev.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import org.json.JSONException
import org.json.JSONObject
import java.io.FileWriter
import java.io.PrintWriter
import java.nio.charset.Charset
import android.widget.TextView
import java.io.File

class AddSong : AppCompatActivity() {

    lateinit var editText: EditText
    lateinit var nameOfSong: String

    //lateinit var artist: String
    lateinit var textView: TextView
    lateinit var jsonClass: JsonUtils
    lateinit var songAdapterClass: PlaylistActivity.SongAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "test")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_songs)


        val addSong = findViewById<Button>(R.id.songUploadBtn)
        addSong.setOnClickListener {
            Log.i("AddSong", "addSongButton Called")

        }
        editText = findViewById(R.id.editText)
        textView = findViewById(R.id.nameOfSongtxt)


        val done = findViewById<Button>(R.id.submitBnt)
        done.setOnClickListener {

            Log.i("addSong", "Ok button Called")
            nameOfSong = editText.text.toString()
            textView.text = nameOfSong

//            var tester = songAdapterClass.itemCount
            //come back to this this will be the ID,
            // grab the name of song and author from the text fields

        }


    }

//    private fun addToJson() {
//        val path = "/json/Data.json"
//        val json = JSONObject()
//
//        try {
//            json.put("songId", "4")
//            json.put("songName", "test")
//            json.put("authorName", "00:03:40")
//            json.put("duration", "@tools:sample/avatars")
//            json.put("avatar", "ABC")
//
//
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//
//        try {
//            PrintWriter(FileWriter(path, Charset.defaultCharset()))
//                .use { it.write(json.toString()) }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }


  //  }

    companion object {
        // String for LogCat documentation
        const val TAG = "Lab 2 - Activity One"
    }
}
