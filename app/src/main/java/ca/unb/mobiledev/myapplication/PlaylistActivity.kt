package ca.unb.mobiledev.myapplication

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.media.Image
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File

class PlaylistActivity: AppCompatActivity() {
    lateinit var mediaPlayer: MediaPlayer
    lateinit var addSongsButton: FloatingActionButton
    lateinit var playButton: FloatingActionButton
    lateinit var nextButton: FloatingActionButton
    lateinit var playlistAvatar: ImageView
    lateinit var playlistBackground: ImageView
    lateinit var songName_bb: TextView
    lateinit var authorName_bb: TextView
    lateinit var songAvatar_bb: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.playlist)
        addSongsButton = findViewById(R.id.addSongsButton)
        playButton = findViewById(R.id.playButton)
        nextButton = findViewById(R.id.nextButton)
        playlistAvatar = findViewById(R.id.playlistAvatar)
        playlistBackground = findViewById(R.id.playlistBackground)
        songName_bb = findViewById(R.id.songName_bb)
        authorName_bb = findViewById(R.id.authorName_bb)
        songAvatar_bb = findViewById(R.id.songAvatar_bb)

        var intent: Intent = getIntent()
        var playlistId: String? = intent.getStringExtra(Intent.EXTRA_INDEX)
        mediaPlayer = MediaPlayer()
        addSongsButton.setOnClickListener{
            startActivity(intent)
            Log.i("PlaylistActivity", "addSongsButton Called")
        }
        var utils = JsonUtils(this)
        var songList: ArrayList<Song> = utils.getPlaylist(this, playlistId)
        playlistAvatar.setBackgroundResource(R.drawable.ic_launcher_background)
        playlistBackground.setBackgroundResource(R.drawable.default_playlist_background)
        var recyclerView = findViewById<RecyclerView>(R.id.songList)
        var adapter = SongAdapter(songList, mediaPlayer, this@PlaylistActivity)
        recyclerView.adapter = adapter
    }
    class SongAdapter(private val songList: ArrayList<Song>, private val mediaPlayer: MediaPlayer, private val parentActivity: PlaylistActivity)
        :RecyclerView.Adapter<SongAdapter.ViewHolder>(){
        private var isPlaying: Boolean = false
        private var songPlaying: String = ""
        class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
            val songName_list: TextView
            val authorName_list: TextView
            val songDuration_list: TextView
            val songAvatar_list: ImageView
            init {
                songName_list = view.findViewById(R.id.songName_list)
                authorName_list = view.findViewById(R.id.authorName_list)
                songDuration_list = view.findViewById(R.id.songDuration_list)
                songAvatar_list = view.findViewById(R.id.songAvatar_list)

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.song_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.songName_list.text = songList[position].name
            holder.authorName_list.text = songList[position].authorName
            holder.songDuration_list.text = songList[position].duration
            holder.songAvatar_list.setBackgroundResource(R.drawable.ic_launcher_background)
            holder.itemView.setOnClickListener {
                if (!isPlaying) playSong(holder, position,"General", "ABC.mp3")
            }
            parentActivity.playButton.setOnClickListener {
                if (isPlaying) {
                    parentActivity.playButton.setImageResource(R.drawable.play_button)
                    mediaPlayer.pause()
                    isPlaying = false
                }
                else {
                    if (songPlaying != "") {
                        parentActivity.playButton.setImageResource(R.drawable.stop_button)
                        mediaPlayer.start()
                    }
                    else playSong(holder, position,"General", "ABC.mp3")
                    isPlaying = true
                }
            }
        }

        private fun playSong(holder: ViewHolder, position: Int, dirSource: String, fileName: String) {
            isPlaying = true
            songPlaying = fileName
            parentActivity.songAvatar_bb.setBackgroundResource(R.drawable.ic_launcher_background)
            parentActivity.songName_bb.text = songList[position].name
            parentActivity.authorName_bb.text = songList[position].authorName
            parentActivity.playButton.setImageResource(R.drawable.stop_button)
            mediaPlayer.setDataSource(getMusicPath(dirSource, fileName))
            mediaPlayer.prepare()
            mediaPlayer.start()

        }
        private fun getMusicPath(dirSource: String, fileName: String): String {
            val contextWrapper = ContextWrapper(parentActivity.applicationContext)
            val music = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC + "/$dirSource")
            val musicFile = File(music, fileName)
            return musicFile.path
        }
        override fun getItemCount(): Int {
            return songList.size
        }
    }
}