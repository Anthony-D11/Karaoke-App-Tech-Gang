
    package ca.unb.mobiledev.myapplication

    import android.os.Bundle
    import android.util.Log
    import android.view.View
    import android.widget.Button
    import androidx.appcompat.app.AppCompatActivity
    import android.widget.EditText
    import android.widget.TextView
    import java.io.File

    class AddPlaylist : AppCompatActivity() {

        lateinit var editText: EditText
        lateinit var nameOfPlaylist: String
        lateinit var addPlaylistButton : View
        //lateinit var artist: String
        lateinit var textView: TextView
        lateinit var jsonClass: JsonUtils
      // lateinit var songAdapterClass: PlaylistActivity.SongAdapter

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.add_playlist)
           // Log.i("addSong", "test")
            //cut off
            //addPlaylistButton = findViewById(R.id.addSongsButton)
            // code submit button and ok button
            //not sure why "playlist name" is in add_playlist.xml, I don't think it's necessary
            //Add a textfield saying the name of the file we just uploaded.
            //add a new playlist, add it to song list in json util
            //change xml files
            //file picker in PlaylistActivity
            val addPlaylist = findViewById<Button>(R.id.playlistSubmitBtn)//submitting
            addPlaylist.setOnClickListener {
                Log.i("addplaylist", "addPlaylist Called")
                //create new playlist and add it into Data.json file static playlist array
                //jsonClass.initializeSongList(this.applicationContext)
                //code below is copied from example code. Replace variables

                   // val playlistToAdd: List<Playlist> = listOf(
                    //    Playlist("", "bezkoder", )
                   // );

                   // val playlistToAddString: String = playlistToAdd.toString()
                    //val jsonTutsList: String = gson.toJson(tutsList)

                   // File("Data.json").writeText(playlistToAddString)
                //increment id

               // playlistI++
                //MAYBE IT WOULD BE EASIER TO CREATE 2 DIFFERENT DATA.JSON FILES FOR PLAYLISTS AND
                //SONGS

            }
            editText = findViewById(R.id.editText)
             textView = findViewById(R.id.playlistName)


            val uploadPlaylistCover = findViewById<Button>(R.id.playlistUploadBtn)
            uploadPlaylistCover.setOnClickListener {

                Log.i("addSong", "Ok button Called")
            nameOfPlaylist = editText.text.toString()
            textView.text = nameOfPlaylist


               //var tester = songAdapterClass.itemCount
                //come back to this this will be the ID,
                // grab the name of song and author from the text fields

            }
        // cut off

        }

        companion object {
            // String for LogCat documentation
            private const val TAG = "Lab 2 - Activity One"
        }

    }