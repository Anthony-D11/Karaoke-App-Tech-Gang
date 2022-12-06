package ca.unb.mobiledev.myapplication

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private val REQUEST_AUDIO_PERMISSION_CODE = 101
    private var isRecording = false
    private var output: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private lateinit var microphoneButton: FloatingActionButton
    private var handler: Handler = Handler()
    lateinit var dialog: Dialog
    lateinit var playlistNameEditText: EditText
    lateinit var submitButton: Button
    lateinit var cancelButton: Button
    lateinit var textView: TextView
    lateinit var jsonClass: JsonUtils
    lateinit var songAdapterClass: PlaylistActivity.SongAdapter




    private var filePicker: ActivityResultLauncher<Intent>? = null
    private var newPlaylistName = ""
    private var newPlaylistAvatar = ""
    private var utils: JsonUtils? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()

        utils = JsonUtils(applicationContext)

        val playlistList = utils!!.getPlaylistList()
        val recyclerView = findViewById<RecyclerView>(R.id.playlistList)
        val adapter = PlaylistAdapter(playlistList, this)
        microphoneButton = findViewById(R.id.microphoneButton)
        recyclerView.adapter = adapter


        val microphoneButton = findViewById<FloatingActionButton>(R.id.microphoneButton)
        microphoneButton.setOnClickListener {
            val intents = Intent(this, RecordActivity::class.java)
            startActivity(intents)
        }
        val addPlayListButton = findViewById<FloatingActionButton>(R.id.addPlaylistButton)
        addPlayListButton.setOnClickListener {
            onCreateDialog()
        }
        //setupFilePicker()
    }
    private fun onCreateDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val popupView: View = layoutInflater.inflate(R.layout.add_playlist, null)
        playlistNameEditText = popupView.findViewById(R.id.playlistEditText)
        submitButton = popupView.findViewById(R.id.playlistSubmitBtn)
        cancelButton = popupView.findViewById(R.id.playlistCancelBtn)
        cancelButton.setOnClickListener { dialog.dismiss() }

        submitButton.setOnClickListener {
            newPlaylistName = playlistNameEditText.text.toString()
            val newPlaylist = Playlist.Builder(newPlaylistName, "", "", ArrayList<String>()).build()
            utils!!.addPlaylistToJSONFile(newPlaylist, applicationContext)
            utils!!.addPlaylistToPlaylistListObject(newPlaylist)
            dialog.dismiss()
        }


        dialogBuilder.setView(popupView)
        dialog = dialogBuilder.create()
        dialog.show()
    }
    class PlaylistAdapter(private val playlistList: ArrayList<Playlist>, private val parentActivity: Activity)
        :RecyclerView.Adapter<PlaylistAdapter.ViewHolder>(){
        class ViewHolder: RecyclerView.ViewHolder {
            var playlistName: TextView
            var songNumbers: TextView
            var playlistAvatar: ImageView
            constructor(view: View): super(view){
                playlistName = view.findViewById(R.id.playlistName)
                playlistAvatar = view.findViewById(R.id.playlistAvatar)
                songNumbers = view.findViewById(R.id.songNumbers)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.playlist_object, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            Log.i("JsonUtils",  "onbint "  )
            holder.playlistName.text = playlistList[position].name
            holder.songNumbers.text = playlistList[position].songList.size.toString()
            if (playlistList[position].avatar != "")
                holder.playlistAvatar.setImageURI(playlistList[position].avatar.toUri())
            else holder.playlistAvatar.setBackgroundResource(R.drawable.default_playlist_avatar)
            holder.itemView.setOnClickListener {
                val intent = Intent(parentActivity, PlaylistActivity::class.java).apply {
                    putExtra("playlistName", playlistList[position].name)
                }
                parentActivity.startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return playlistList.size
        }
    }


}