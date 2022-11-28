
    package ca.unb.mobiledev.myapplication

    import android.content.Intent
    import android.os.Bundle
    import android.os.Environment
    import android.util.Log
    import android.view.View
    import android.widget.Button
    import android.widget.EditText
    import android.widget.ImageView
    import android.widget.TextView
    import androidx.activity.result.ActivityResult
    import androidx.activity.result.ActivityResultLauncher
    import androidx.activity.result.contract.ActivityResultContracts
    import androidx.appcompat.app.AppCompatActivity
    import java.io.File
    import java.util.ArrayList


    class AddPlaylist : AppCompatActivity() {
        private var filePicker: ActivityResultLauncher<Intent>? = null
        private var currentModify = ""
        lateinit var playlistAvatar: ImageView
        lateinit var editText: EditText
        lateinit var nameOfPlaylist: String
        lateinit var addPlaylistButton : View
        //lateinit var artist: String
        lateinit var textView: TextView
        lateinit var jsonClass: JsonUtils
        //private lateinit var imageUri: EditText
      // lateinit var songAdapterClass: PlaylistActivity.SongAdapter
        private lateinit var externalFile:File
        override fun onCreate(savedInstanceState: Bundle?) {

            super.onCreate(savedInstanceState)
            setContentView(R.layout.add_playlist)

            //playlistAvatar = findViewById(R.id.playlistAvatar)

            //imageUri = findViewById(R.id.playlistName)
           // Log.i("addSong", "test")
            //cut off
            //addPlaylistButton = findViewById(R.id.addSongsButton)
            // code submit button and ok button
            //not sure why "playlist name" is in add_playlist.xml, I don't think it's necessary
            //Add a textfield saying the name of the file we just uploaded.
            //add a new playlist, add it to song list in json util
            //change xml files
            //file picker in PlaylistActivity

            val submitPlaylist = findViewById<Button>(R.id.playlistSubmitBtn)//CLICK THIS BUTTON TO SUBMIT INFO
            submitPlaylist.setOnClickListener {
               //Log.i("submitplaylist", "submitPlaylist Called")
                //create new playlist and add it into Data.json file static playlist array
                //jsonClass.initializeSongList(this.applicationContext)

                editText = findViewById<EditText>(R.id.editText)
                var TestSongList = ArrayList<Song>()

//changging the playlistI  , editText.text.toString()
                var newPlaylist:Playlist = Playlist(playlistI.toString(), "tester","@tools:sample/avatars", TestSongList )
                //code does not like editText field above
                jsonClass = JsonUtils(applicationContext)
                if (!isExternalStorageAvailable() || isExternalStorageReadOnly() ) {
                    submitPlaylist.isEnabled = false
                    Log.i("JsonUtils", "nope")
                } else {
                    // need access to read function here OR initialize externalFile in JsonUtils
                    Log.i("JsonUtils", "does it work")
                       jsonClass.addPlaylistToJSONFile(newPlaylist, applicationContext)

                }
                editText.setText("")
            //put this data in the Json file
                //increment id
               playlistI++
                val intent = Intent(this@AddPlaylist, MainActivity::class.java)
                startActivity(intent)
                //MAYBE IT WOULD BE EASIER TO CREATE 2 DIFFERENT DATA.JSON FILES FOR PLAYLISTS AND
                //SONGS

            }

          // editText = findViewById(R.id.editText)
            // textView = findViewById(R.id.playlistName)
            Log.i("addPlaylist", "bambi")

            val uploadPlaylistCover = findViewById<Button>(R.id.playlistUploadBtn)
            uploadPlaylistCover.setOnClickListener {

               // Log.i("addSong", "Ok button Called")
            //nameOfPlaylist = editText.text.toString()
            //textView.text = nameOfPlaylist

              //var pA:PlaylistActivity = PlaylistActivity()
              // pA.setupFilePicker()
               //var tester = songAdapterClass.itemCount
                //come back to this this will be the ID,
                // grab the name of song and author from the text fields
                //adds avatar id (file name? something.mp3)
            }

        // cut off
            setupFilePicker(playlistI)

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

        private fun openFilePicker() {
            var intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            intent = Intent.createChooser(intent, "Choose a image file")
            filePicker!!.launch(intent)
            //write uri (maybe) to Data.json


        }
        private fun setupFilePicker(curPlaylistId:Int) {//use Id to find playlist in .json file and set "avatar" to uri
            filePicker = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    result: ActivityResult ->
                if (result.resultCode == RESULT_OK) {
                   val data = result.data
                    val uri = data!!.data
                   playlistAvatar.setImageURI(uri)//how to find unique playlistAvatar
                   // val inputStream: InputStream = applicationContext.contentResolver.openInputStream(data)!!
                    //var info: String = uri.toString()
                   // File("playlistData.json").bufferedWriter().use { out ->
                       // out.write(info)
                   // }
                }
            }
        }


    companion object {
            // String for LogCat documentation
            private const val TAG = "Lab 2 - Activity One"
        }

    }