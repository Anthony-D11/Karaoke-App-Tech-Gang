
    package ca.unb.mobiledev.myapplication

    import android.os.Bundle
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
    import java.io.File

    class AddPlaylist : AppCompatActivity() {

        lateinit var editText: EditText
        lateinit var nameOfPlaylist: String
        lateinit var addPlaylistButton : View
        //lateinit var artist: String
        lateinit var textView: TextView
        lateinit var jsonClass: JsonUtils
      //  lateinit var songAdapterClass: PlaylistActivity.SongAdapter

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.add_playlist)
            Log.i("addSong", "test")
            //addPlaylistButton = findViewById(R.id.addSongsButton)

           // val addPlaylist = findViewById<Button>(R.id.deployBnt)
           // addPlaylist.setOnClickListener {
               // Log.i("addplaylist", "addPlaylist Called")

            //}
            //editText = findViewById(R.id.editText)
            // textView = findViewById(R.id.nameOfPlaylist)


            //val done = findViewById<Button>(R.id.submitBnt)
            //done.setOnClickListener {
//
//                Log.i("addSong", "Ok button Called")
//            nameOfPlaylist = editText.text.toString()
//            textView.text = nameOfPlaylist

               // var tester = songAdapterClass.itemCount
                //come back to this this will be the ID,
                // grab the name of song and author from the text fields

         //   }


        }

        companion object {
            // String for LogCat documentation
            private const val TAG = "Lab 2 - Activity One"
        }

    }