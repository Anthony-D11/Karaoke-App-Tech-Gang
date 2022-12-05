package ca.unb.mobiledev.myapplication

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import org.json.JSONException
import org.json.JSONObject
import java.io.FileWriter
import java.io.PrintWriter
import java.nio.charset.Charset
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import java.io.File

class AddSong : AppCompatActivity() {
    private var filePicker: ActivityResultLauncher<Intent>? = null
    lateinit var editText: EditText
    lateinit var nameOfSong: String

    //lateinit var artist: String
    lateinit var textView: TextView
    lateinit var jsonClass: JsonUtils
    lateinit var songAdapterClass: PlaylistActivity.SongAdapter

    lateinit var dialog: Dialog
    lateinit var songNameEditText: EditText
    lateinit var authorNameEditText: EditText
    lateinit var submitButtonSong: Button
    lateinit var cancelButtonSong: Button


    private var newSongName = ""
    private var newPlaylistAvatar = ""
    private var newAuthorName = ""

    private var utils: JsonUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "test")
        super.onCreate(savedInstanceState)
      setContentView(R.layout.playlist)
        onCreateDialogaddSongs()
//        val done = findViewById<Button>(R.id.playlistSubmitBtn3)
//        done.setOnClickListener {
//            Log.i("AddSong", "addSongButton Called")
       jsonClass = JsonUtils(applicationContext)
//            var songID = jsonClass.getSongSize()?.toInt()?.plus(1)
//            Log.i("JsonUtils", "songId" + songID)
//            var newSongs:Song = Song( songID.toString(),"sjdff" ,"mel ","@tools:sample/avatars",  "ABC","00:03:40" )

//          jsonClass.addSongToJSONFile(newSongs, applicationContext)
//            val intent = Intent(this@AddSong, MainActivity::class.java)
//            startActivity(intent)
        setupFilePicker()

     }

        fun onCreateDialogaddSongs() {
            val dialogBuilder = AlertDialog.Builder(this)
            val popupView: View = layoutInflater.inflate(R.layout.add_songs, null)



            songNameEditText= popupView.findViewById(R.id.playlistEditText3)
            authorNameEditText= popupView.findViewById(R.id.songAuthorTxt)
            submitButtonSong = popupView.findViewById(R.id.playlistSubmitBtn3)
            cancelButtonSong = popupView.findViewById(R.id.playlistCancelBtn3)
            // choosePictureButton = popupView.findViewById(R.id.playlistUploadBtn)

            cancelButtonSong.setOnClickListener { dialog.dismiss() }

            submitButtonSong.setOnClickListener {
                newSongName = songNameEditText.text.toString()
                newAuthorName = authorNameEditText.text.toString()

                val newSongs = Song(jsonClass.getSongSize().toString(),newSongName, newAuthorName, newPlaylistAvatar,"ABC",  "00:03:40")
                jsonClass.addSongToJSONFile(newSongs, applicationContext)
                newPlaylistAvatar = ""
                dialog.dismiss()
            }

            dialogBuilder.setView(popupView)
            dialog = dialogBuilder.create()
            dialog.show()

//            val intent = Intent(this@AddSong, MainActivity::class.java)
//           startActivity(intent)

        }





    companion object {
        // String for LogCat documentation
        const val TAG = "Lab 2 - Activity One"
    }
}
