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
            val intents = Intent(this@MainActivity, RecordActivity::class.java)
            startActivity(intents)
        }
        val addPlayListButton = findViewById<FloatingActionButton>(R.id.addPlaylistButton)
        addPlayListButton.setOnClickListener {
            val intents = Intent(this@MainActivity, AddPlaylist::class.java)
            startActivity(intents)
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
            holder.songNumbers.text = playlistList[position].getSongList().size.toString()
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