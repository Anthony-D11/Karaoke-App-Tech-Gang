package ca.unb.mobiledev.myapplication

import android.app.Dialog
import android.content.ContextWrapper
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File

class PlaylistActivity: AppCompatActivity() {
    lateinit var addSongsButton: FloatingActionButton
    lateinit var playButton: FloatingActionButton
    lateinit var nextButton: FloatingActionButton
    lateinit var avatar_edit_button: ImageButton
    lateinit var background_edit_button: ImageButton
    lateinit var playlistAvatar: ImageView
    lateinit var playlistBackground: ImageView
    lateinit var songName_bb: TextView
    lateinit var authorName_bb: TextView
    lateinit var songAvatar_bb: ImageView
    lateinit var mediaPlayer: MediaPlayer
    lateinit var currentPlaylist: Playlist

    private var filePicker: ActivityResultLauncher<Intent>? = null
    private var currentModify = ""

    private var isPlaying: Boolean = false
    private var songPlaying: Song? = null
    lateinit var dialog: Dialog


    lateinit var songNameEditText: EditText
    lateinit var authorNameEditText: EditText
    lateinit var submitButtonSong: Button
    lateinit var cancelButtonSong: Button
    lateinit var jsonClass: JsonUtils

    private var newSongName = ""
    private var newSongAvatar= ""
    private var newAuthorName = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.playlist)
        var intent: Intent = getIntent()
        var playlistName: String? = intent.getStringExtra("playlistName")

        val utils = JsonUtils(this)

        val songList = utils.getSongList(playlistName)

        currentPlaylist = utils.getPlaylist(playlistName)

        if (songList.size > 0)
            songPlaying = songList.get(0)



        addSongsButton = findViewById(R.id.addSongsButton)
        playButton = findViewById(R.id.playButton)
        nextButton = findViewById(R.id.nextButton)
        playlistAvatar = findViewById(R.id.playlistAvatar)
        playlistBackground = findViewById(R.id.playlistBackground)
        songName_bb = findViewById(R.id.songName_bb)
        authorName_bb = findViewById(R.id.authorName_bb)
        songAvatar_bb = findViewById(R.id.songAvatar_bb)
        avatar_edit_button = findViewById(R.id.avatar_edit_button)
        background_edit_button = findViewById(R.id.background_edit_button)


        avatar_edit_button.setOnClickListener {
            editAvatar()
        }
        background_edit_button.setOnClickListener {
            editBackground()
        }

       addSongsButton.setOnClickListener{

          onCreateDialogaddSongs()
        }
        playButton.setOnClickListener {
            if (isPlaying) {
                playButton.setImageResource(R.drawable.play_button)
                mediaPlayer.pause()
                isPlaying = false
            }
            else {
                playSong(songPlaying!!,"General", "ABC.mp3")
                isPlaying = true
            }
        }


        playlistAvatar.setBackgroundResource(R.drawable.ic_launcher_background)
        playlistBackground.setBackgroundResource(R.drawable.default_playlist_background)
        val recyclerView = findViewById<RecyclerView>(R.id.songList)
        var adapter = songList?.let { SongAdapter(it, this, "playing") }
        recyclerView.adapter = adapter
        mediaPlayer = MediaPlayer()
        setupFilePicker()

    }

    private fun editAvatar() {
        currentModify = "avatar"
        openFilePicker()
    }

    private fun editBackground() {
        currentModify = "background"
        openFilePicker()
    }

    private fun openFilePicker() {
        var intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        intent = Intent.createChooser(intent, "Choose a image file")
        filePicker!!.launch(intent)
        //write uri to Data.json. Create a JSON object and add it to Data.json file using

    }
    private fun setupFilePicker() {
        filePicker = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val uri = data!!.data
                if (currentModify == "avatar") {
                    playlistAvatar.setImageURI(uri)
                }
                if (currentModify == "background") {
                    playlistBackground.setImageURI(uri)
                }
            }
        }
    }

    fun playSong(song: Song, dirSource: String, fileName: String) {
        isPlaying = true
        songAvatar_bb.setBackgroundResource(R.drawable.ic_launcher_background)
        songName_bb.text = song.name
        authorName_bb.text = song.authorName
        playButton.setImageResource(R.drawable.stop_button)
        mediaPlayer.setDataSource(getMusicPath(dirSource, fileName))
        mediaPlayer.prepare()
        mediaPlayer.start()
    }

    private fun getMusicPath(dirSource: String, fileName: String): String {
        val contextWrapper = ContextWrapper(applicationContext)
        val music = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC + "/$dirSource")
        val musicFile = File(music, fileName)
        return musicFile.path
    }
    class SongAdapter(private val songList: ArrayList<Song>?, private val parentActivity: AppCompatActivity, private val mode: String)
        :RecyclerView.Adapter<SongAdapter.ViewHolder>(){
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
            Log.i("playlistActivity", " load songlist working? " )
            holder.songName_list.text = songList?.get(position)!!.name
            holder.authorName_list.text = songList?.get(position).authorName
            holder.songDuration_list.text = songList?.get(position).duration
            holder.songAvatar_list.setBackgroundResource(R.drawable.ic_launcher_background)
            holder.itemView.setOnClickListener {
                (parentActivity as PlaylistActivity).playSong(songList[position],"General", "ABC.mp3")
            }
        }
        override fun getItemCount(): Int {
            return songList!!.size
        }
    }

    fun onCreateDialogaddSongs() {
        val dialogBuilders = AlertDialog.Builder(this)
        val popupView: View = layoutInflater.inflate(R.layout.add_songs, null)
        jsonClass = JsonUtils(applicationContext)


        songNameEditText= popupView.findViewById(R.id.playlistEditText3)
        authorNameEditText= popupView.findViewById(R.id.songAuthorTxt)
        submitButtonSong = popupView.findViewById(R.id.playlistSubmitBtn3)
        cancelButtonSong = popupView.findViewById(R.id.playlistCancelBtn3)

        cancelButtonSong.setOnClickListener { dialog.dismiss() }

        submitButtonSong.setOnClickListener {
            newSongName = songNameEditText.text.toString()
            newAuthorName = authorNameEditText.text.toString()

            val newSongs = Song(jsonClass.getSongSize().toString(),newSongName, newAuthorName, newSongAvatar,"ABC",  "00:03:40")
            jsonClass.addSongToJSONFile(newSongs, applicationContext)
            newSongAvatar = ""
            dialog.dismiss()
        }

        dialogBuilders.setView(popupView)
        dialog = dialogBuilders.create()
        dialog.show()

    }



    companion object {
        // String for LogCat documentation
        const val TAG = "Lab 2 - Activity One"
    }
}