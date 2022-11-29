package ca.unb.mobiledev.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Environment
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



        val done = findViewById<Button>(R.id.submitBnt)
        done.setOnClickListener {
            Log.i("AddSong", "addSongButton Called")
            jsonClass = JsonUtils(applicationContext)

            var songID = jsonClass.getSongSize()?.toInt()?.plus(1)
            Log.i("JsonUtils", "songId" + songID)
            var newSongs:Song = Song(songID.toString(),"testing one two three" ,"tester","@tools:sample/avatars",  "ABC","00:03:40" )
            //code does not like editText field above

            if (!isExternalStorageAvailable() || isExternalStorageReadOnly() ) {
                addSong.isEnabled = false
                Log.i("JsonUtils", "nope")
            } else {

                Log.i("JsonUtils", "does it work")
                jsonClass.addSongToJSONFile(newSongs, applicationContext)

            }



            val intent = Intent(this@AddSong, MainActivity::class.java)
            startActivity(intent)




        }


    }

  private fun isExternalStorageReadOnly(): Boolean {
      val extStorageState = Environment.getExternalStorageState()
      return if (Environment.MEDIA_MOUNTED_READ_ONLY == extStorageState) {
          true
      } else false
  }

    private fun isExternalStorageAvailable(): Boolean {
        val extStorageState = Environment.getExternalStorageState()
        return if (Environment.MEDIA_MOUNTED == extStorageState) {
            true
        } else false
    }

    companion object {
        // String for LogCat documentation
        const val TAG = "Lab 2 - Activity One"
    }
}
