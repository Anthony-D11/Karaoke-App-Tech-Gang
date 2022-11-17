package ca.unb.mobiledev.myapplication

import android.Manifest
import android.app.Activity
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
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()

        var utils = JsonUtils(this)


        var playlistList = utils.getPlaylistList()
        var recyclerView = findViewById<RecyclerView>(R.id.playlistList)
        var adapter = PlaylistAdapter(playlistList, this)
        microphoneButton = findViewById(R.id.microphoneButton)
        recyclerView.adapter = adapter


        val microphoneButton = findViewById<FloatingActionButton>(R.id.microphoneButton)
        microphoneButton.setOnClickListener {
            if (checkRecordingPermission()) {
                if (!isRecording) startRecording()
                else stopRecording()
            }
            else {
                requestRecordingPermission()
            }
        }

        val addPlayListButton = findViewById<FloatingActionButton>(R.id.addPlaylistButton)
        addPlayListButton.setOnClickListener {
            Log.i("MainActivity", "addPlaylistButton Called")
            val intents = Intent(this@MainActivity, AddPlaylist::class.java)
            startActivity(intents)
        }

    }

    private fun stopRecording() {
        Executors.newSingleThreadExecutor().execute(object: Runnable{
            override fun run() {
                mediaRecorder!!.stop()
                mediaRecorder!! .release()
                mediaRecorder = null

                runOnUiThread(object: Runnable{
                    override fun run() {
                        microphoneButton.setImageResource(R.drawable.microphone)
                        handler.removeCallbacksAndMessages(null)
                    }
                })
            }
        })

    }

    private fun startRecording() {
        isRecording = true
        Executors.newSingleThreadExecutor().execute(object: Runnable {
            override fun run() {
                mediaRecorder = MediaRecorder()
                mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
                mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                mediaRecorder!!.setOutputFile(getRecordingPath())
                mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                val path = getRecordingPath()
                mediaRecorder!!.prepare()
                mediaRecorder!!.start()
                runOnUiThread(object: Runnable {
                    override fun run() {
                        microphoneButton.setImageResource(R.drawable.stop_button)
                    }

                })
            }

        })
    }
    private fun requestRecordingPermission() {
        ActivityCompat.requestPermissions(this@MainActivity, arrayOf<String>(Manifest.permission.RECORD_AUDIO), REQUEST_AUDIO_PERMISSION_CODE)
    }
    private fun checkRecordingPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            requestRecordingPermission()
            return false
        }
        return true
    }
    private fun getRecordingPath(): String {
        val contextWrapper = ContextWrapper(applicationContext)
        val music = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC + "/General")
        val musicFile = File(music, "ABC" + ".mp3")
        return musicFile.path
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_AUDIO_PERMISSION_CODE) {
            if (grantResults.size > 0) {
                var permissionToRecord: Boolean = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (permissionToRecord) {
                    Toast.makeText(applicationContext, "Permission Granted", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
    class PlaylistAdapter(private val playlistList: ArrayList<Playlist>, private val parentActivity: Activity):RecyclerView.Adapter<PlaylistAdapter.ViewHolder>(){
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
            holder.playlistName.text = playlistList[position].getName()
            holder.songNumbers.text = playlistList[position].getSongList()?.size.toString()
            holder.playlistAvatar.setBackgroundResource(R.drawable.ic_launcher_background)
            holder.itemView.setOnClickListener {
                val intent = Intent(parentActivity, PlaylistActivity::class.java).apply {
                    putExtra(Intent.EXTRA_INDEX, playlistList[position].getId())
                }
                parentActivity.startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return playlistList.size
        }
    }
}