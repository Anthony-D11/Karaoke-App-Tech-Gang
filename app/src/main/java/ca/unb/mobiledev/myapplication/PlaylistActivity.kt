package ca.unb.mobiledev.karaokeapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import ca.unb.mobiledev.myapplication.MainActivity
import ca.unb.mobiledev.myapplication.R
import ca.unb.mobiledev.myapplication.Song
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PlaylistActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val backButton: FloatingActionButton = findViewById<FloatingActionButton>(R.id.backButton)
        val addSongsButton: FloatingActionButton = findViewById<FloatingActionButton>(R.id.addSongsButton)
        val playButton: FloatingActionButton = findViewById<FloatingActionButton>(R.id.playButton)
        val nextButton: FloatingActionButton = findViewById<FloatingActionButton>(R.id.nextButton)
        backButton.setOnClickListener{
            val intent: Intent = Intent(this@PlaylistActivity, MainActivity::class.java)
            startActivity(intent)
            Log.i("PlaylistActivity", "backButton Called")
        }
        addSongsButton.setOnClickListener{
            startActivity(intent)
            Log.i("PlaylistActivity", "addSongsButton Called")
        }
        playButton.setOnClickListener{
            startActivity(intent)
            Log.i("PlaylistActivity", "playButton Called")
        }
        nextButton.setOnClickListener{
            startActivity(intent)
            Log.i("PlaylistActivity", "nextButton Called")
        }
    }
    class CustomAdapter(private val songList: ArrayList<Song>):RecyclerView.Adapter<CustomAdapter.ViewHolder>(){
        class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
            val songName: TextView
            val authorName: TextView
            val songDuration: TextView
            val songAvatar: ImageView
            init {
                songName = view.findViewById(R.id.songName)
                authorName = view.findViewById(R.id.authorName)
                songDuration = view.findViewById(R.id.songDuration)
                songAvatar = view.findViewById(R.id.songAvatar)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.song_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.songName.text = songList[position].name
            holder.authorName.text = songList[position].authorName
            holder.songDuration.text = songList[position].duration
            holder.songAvatar.setBackgroundResource(R.drawable.ic_launcher_background)
        }

        override fun getItemCount(): Int {
            return songList.size
        }
    }
}