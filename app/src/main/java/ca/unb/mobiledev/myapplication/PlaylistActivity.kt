package ca.unb.mobiledev.myapplication

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class PlaylistActivity: AppCompatActivity() {
    lateinit var addSongsButton: FloatingActionButton
    lateinit var playButton: FloatingActionButton
    lateinit var playlistAvatar: ImageView
    lateinit var playlistBackground: ImageView
    lateinit var songName_bb: TextView
    lateinit var authorName_bb: TextView
    lateinit var songAvatar_bb: ImageView
    private var mediaPlayer: MediaPlayer? = null
    lateinit var utils: JsonUtils
    lateinit var playlistName: String
    lateinit var currentPlaylist: Playlist
    lateinit var recyclerView: RecyclerView
    lateinit var bottomBar: LinearLayout

    private var isPlaying: Boolean = false
    private var songPlaying: String = ""
    lateinit var dialog: Dialog


    lateinit var songNameEditText: EditText
    lateinit var authorNameEditText: EditText
    lateinit var submitButtonSong: Button
    lateinit var cancelButtonSong: Button

    private var newSongName = ""
    private var newSongAvatar= ""
    private var newAuthorName = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.playlist)
        val intent: Intent = getIntent()
        playlistName= intent.getStringExtra("playlistName").toString()
        utils = JsonUtils(this)

        val songList = utils.getSongList(playlistName)

        currentPlaylist = utils.getPlaylist(playlistName)!!


        addSongsButton = findViewById(R.id.addSongsButton)
        playButton = findViewById(R.id.playButton)
        playlistAvatar = findViewById(R.id.playlistAvatar)
        playlistBackground = findViewById(R.id.playlistBackground)
        songName_bb = findViewById(R.id.songName_bb)
        authorName_bb = findViewById(R.id.authorName_bb)
        songAvatar_bb = findViewById(R.id.songAvatar_bb)
        val playlistTitle = findViewById<TextView>(R.id.playlistNameTitle)
        playlistTitle.text = playlistName

        bottomBar = findViewById(R.id.bottomBar)

        addSongsButton.setOnClickListener{
          onCreateDialogAddSongs()
        }
        playButton.setOnClickListener {
            if (mediaPlayer!!.isPlaying) {
                playButton.setImageResource(R.drawable.play_button)
                mediaPlayer!!.pause()
            }
            else {
                mediaPlayer!!.start()
                playButton.setImageResource(R.drawable.stop_button)

                val handler = Handler()
                handler.postDelayed(object : Runnable {
                    override fun run() {
                        playButton.setImageResource(R.drawable.play_button)
                    }
                }, mediaPlayer!!.duration.toLong())
            }
        }

        playlistAvatar.setBackgroundResource(R.drawable.default_playlist_avatar)
        playlistBackground.setBackgroundResource(R.drawable.default_playlist_background)

        recyclerView = findViewById<RecyclerView>(R.id.songList)
        val adapter= SongAdapter(songList,this, "playing")


        recyclerView.adapter = adapter

    }

    private fun changeUI(song: Song) {
        songAvatar_bb.setBackgroundResource(R.drawable.default_song_cover)
        songName_bb.text = song.nickName
        authorName_bb.text = song.authorName
        playButton.setImageResource(R.drawable.stop_button)
    }

    fun playSong(song: Song, changeUI: Boolean) {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setDataSource(song.source)
        mediaPlayer!!.prepare()
        mediaPlayer!!.start()
        if (changeUI) changeUI(song)
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                playButton.setImageResource(R.drawable.play_button)
            }
        }, mediaPlayer!!.duration.toLong())

    }

    class SongAdapter(private val songList: ArrayList<Song>?, private val parentActivity: AppCompatActivity, private val mode: String)

        :RecyclerView.Adapter<SongAdapter.ViewHolder>(){
        class ViewHolder(view: View, mode: String): RecyclerView.ViewHolder(view) {
            val songName_list: TextView
            val authorName_list: TextView
            val songDuration_list: TextView
            var songAvatar_list: ImageView? = null
            init {
                songName_list = view.findViewById(R.id.songName_list)
                authorName_list = view.findViewById(R.id.authorName_list)
                songDuration_list = view.findViewById(R.id.songDuration_list)
                if (mode == "playing")
                songAvatar_list = view.findViewById(R.id.songAvatar_list)
            }

        }
        private var previousChoose: ViewHolder? = null
        var choosedSong: Song? = null
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            var view = LayoutInflater.from(parent.context)
                .inflate(R.layout.song_item, parent, false)
            if (mode == "choosing")
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.song_choose, parent, false)
            return ViewHolder(view, mode)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            Log.i("JsonUtils",  "onbint "  )
            holder.songName_list.text = songList?.get(position)!!.nickName
            if (songList?.get(position)!!.nickName == "") holder.songName_list.text = songList?.get(position)!!.realName
            holder.authorName_list.text = songList?.get(position).authorName
            if (songList.get(position).authorName == "" && mode == "playing") holder.authorName_list.text = "Unknown author"
            if (mode == "playing") holder.songAvatar_list!!.setBackgroundResource(R.drawable.default_song_cover)

            holder.itemView.setOnClickListener {
                if (mode == "playing") {
                    (parentActivity as PlaylistActivity).bottomBar.visibility = View.VISIBLE
                    (parentActivity as PlaylistActivity).playSong(songList[position], true)
                }
                else {
                    if (previousChoose != null) previousChoose!!.songDuration_list.text = ""
                    holder.songDuration_list.text = "Choosed"
                    previousChoose = holder
                    choosedSong = songList[position]
                }
            }
        }
        override fun getItemCount(): Int {
            return songList!!.size
        }
    }



    private fun onCreateDialogAddSongs() {
        val dialogBuilders = AlertDialog.Builder(this)
        val popupView: View = layoutInflater.inflate(R.layout.add_songs, null)

        val allSongs = utils.getAllSongs()
        songNameEditText= popupView.findViewById(R.id.addSongNameEditText)
        authorNameEditText= popupView.findViewById(R.id.addSongAuthorEditText)
        submitButtonSong = popupView.findViewById(R.id.addSongSubmitBtn)
        cancelButtonSong = popupView.findViewById(R.id.addSongCancelBtn)
        val recycler = popupView.findViewById<RecyclerView>(R.id.chooseSongRecyclerView)
        val customAdapter = SongAdapter(allSongs, this, "choosing")
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        recycler.setLayoutManager(mLayoutManager)
        recycler.adapter = customAdapter
        cancelButtonSong.setOnClickListener { dialog.dismiss() }

        submitButtonSong.setOnClickListener {
            if (songNameEditText.text.toString() == "" || authorNameEditText.text.toString() == "" || customAdapter.choosedSong == null)
                Toast.makeText(applicationContext, "Unsuccessful, please enter both text field!", Toast.LENGTH_SHORT).show()
            else {
                val song = Song.Builder(customAdapter.choosedSong!!.realName, songNameEditText.text.toString(), authorNameEditText.text.toString(), "", "", customAdapter.choosedSong!!.source).build()
                utils.addSongToPlaylistObject(song, playlistName)
                utils.addSongToPlaylistJson(song, playlistName)
                utils.addNickNameForSongJson(customAdapter.choosedSong!!.realName, songNameEditText.text.toString())
                utils.addNickNameForSongListObject(customAdapter.choosedSong!!.realName, songNameEditText.text.toString())
                utils.addAuthorForSongJson(customAdapter.choosedSong!!.realName, authorNameEditText.text.toString())
                utils.addAuthorForSongListObject(customAdapter.choosedSong!!.realName, authorNameEditText.text.toString())

                val a = SongAdapter(utils.getSongList(playlistName), this, "playing")
                recyclerView.adapter = a
                dialog.dismiss()
            }
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