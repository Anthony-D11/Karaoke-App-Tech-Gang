package ca.unb.mobiledev.myapplication

import android.Manifest
import android.app.Dialog
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.net.URI
import java.util.concurrent.Executors

class RecordActivity: AppCompatActivity() {

    private val REQUEST_AUDIO_PERMISSION_CODE = 101
    lateinit var recordButton: ImageButton
    lateinit var displayTimer: TextView
    lateinit var timer: CountDownTimer
    lateinit var chooseSongButton: Button
    lateinit var chooseLyricsButton: Button
    lateinit var lyricsView: TextView
    lateinit var dialog: Dialog
    lateinit var dirSource: String
    lateinit var fileName: String
    lateinit var utils: JsonUtils
    private var maximumSeconds: Long = 10000
    private var intervalSeconds: Long = 1

    private var isRecording = false
    private var mediaRecorder: MediaRecorder? = null
    private var handler: Handler = Handler()
    private var mediaPlayer: MediaPlayer? = null
    private var songPlaying: String = ""
    private var filePicker: ActivityResultLauncher<Intent>? = null

    private var fileChoosing = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.record);

        val extras = intent
        utils = JsonUtils(applicationContext)
        createRecordingPlaylist()
        if (extras.getStringExtra("songName") != null) {
            songPlaying = extras.getStringExtra("songName").toString()
        }
        if (extras.getStringExtra("fileName") != null) {
            fileName = extras.getStringExtra("fileName").toString()
        }
        if (extras.getStringExtra("dirSource") != null) {
            dirSource = extras.getStringExtra("dirSource").toString()
        }

        displayTimer = findViewById(R.id.lblTimer)
        chooseSongButton = findViewById(R.id.chooseSongButton)
        chooseLyricsButton = findViewById(R.id.chooseLyricsButton)

        lyricsView = findViewById(R.id.lyricsTextView)

        recordButton = findViewById(R.id.recordButton)

        setupTimer()
        recordButton.setBackgroundResource(R.drawable.start_recording)
        recordButton.setOnClickListener {
            if (!isRecording) {
                if (checkRecordingPermission()) {
                    if (songPlaying != "") {
                        startPlayingSong()
                    }
                    startRecording()
                } else {
                    requestRecordingPermission()
                }
            }
            else {
                stopRecording()
                onCreateDialog()
                if (songPlaying != "") {
                    stopPlayingSong()
                    songPlaying = ""
                }
            }
        }
        chooseSongButton.setOnClickListener {
            fileChoosing = "song"
            chooseSong()
        }
        chooseLyricsButton.setOnClickListener {
            fileChoosing = "lyrics"
            chooseLyrics()
        }
        if (songPlaying != "") {
            chooseSongButton.text = songPlaying
            prepareSong(getMusicPath(dirSource, fileName))
        }
        setupFilePicker()

    }

    private fun createRecordingPlaylist() {
        val playlist = Playlist.Builder("Recording", "", "", ArrayList()).build()
        utils.addPlaylistToPlaylistListObject(playlist)
        utils.addPlaylistToJSONFile(playlist, applicationContext)
    }

    private fun startRecording() {
        isRecording = true
        timer.start()
        Executors.newSingleThreadExecutor().execute(object: Runnable {
            override fun run() {
                mediaRecorder = MediaRecorder()
                mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
                mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                mediaRecorder!!.setOutputFile(getRecordingPath("Test"))
                mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                mediaRecorder!!.prepare()
                mediaRecorder!!.start()
                runOnUiThread(object: Runnable {
                    override fun run() {
                        recordButton.setBackgroundResource(R.drawable.stop_recording)
                    }

                })
            }

        })
    }
    private fun stopRecording() {
        isRecording = false
        timer.cancel()
        displayTimer.text = "00:00:00"
        chooseSongButton.text = "Choose Song"
        chooseLyricsButton.text = "Choose Lyrics"
        lyricsView.text = ""
        Executors.newSingleThreadExecutor().execute(object: Runnable{
            override fun run() {
                mediaRecorder!!.stop()
                mediaRecorder!!.release()
                mediaRecorder = null

                runOnUiThread(object: Runnable{
                    override fun run() {
                        handler.removeCallbacksAndMessages(null)
                        recordButton.setBackgroundResource(R.drawable.start_recording)
                    }
                })
            }
        })
    }
    private fun getRecordingPath(songName: String): String {
        val contextWrapper = ContextWrapper(applicationContext)
        val music = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC + "/General")
        val musicFile = File(music, songName + ".mp3")
        return musicFile.path
    }

    private fun startPlayingSong() {
        Executors.newSingleThreadExecutor().execute(object: Runnable {
            override fun run() {
                mediaPlayer!!.start()
            }

        })
    }
    private fun stopPlayingSong() {
        Executors.newSingleThreadExecutor().execute(object: Runnable{
            override fun run() {
                mediaPlayer!!.stop()
                mediaPlayer!!.release()
                mediaPlayer = null

                runOnUiThread(object: Runnable{
                    override fun run() {
                        handler.removeCallbacksAndMessages(null)
                    }
                })
            }
        })
    }

    private fun prepareSong(path: String) {
        if (mediaPlayer == null) mediaPlayer = MediaPlayer()
        mediaPlayer!!.setDataSource(path)
        mediaPlayer!!.prepare()
    }
    private fun getMusicPath(dirSource: String, fileName: String): String {
        val contextWrapper = ContextWrapper(applicationContext)
        val music = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC + "/$dirSource")
        val musicFile = File(music, fileName)
        return musicFile.path
    }

    private fun setupTimer() {
        timer = object: CountDownTimer(maximumSeconds * 1000, intervalSeconds * 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var seconds = (maximumSeconds * 1000 - millisUntilFinished) / 1000
                var minutes = (seconds / 60).toInt()
                var hours = minutes / 60
                seconds -= minutes * 60
                minutes -= hours * 60
                var result = ""
                if (hours < 10) {
                    result += "0"
                }
                result += hours.toString() + ":"
                if (minutes < 10) {
                    result += "0"
                }
                result += minutes.toString() + ":"
                if (seconds < 10) {
                    result += "0"
                }
                result += seconds.toString()
                displayTimer.text = result
            }
            override fun onFinish() {
            }

        }
    }

    private fun chooseSong() {
        var intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("audio/*")
        intent = Intent.createChooser(intent, "Choose a music file")
        filePicker!!.launch(intent)
    }

    private fun chooseLyrics() {
        var intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("text/*")
        intent = Intent.createChooser(intent, "Choose a text file")
        filePicker!!.launch(intent)
    }

    private fun setupFilePicker() {
        filePicker = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val add = data!!.data
                if (fileChoosing == "song") {
                    if (mediaPlayer == null) mediaPlayer = MediaPlayer()
                    mediaPlayer!!.setDataSource(this, data!!.data!!)
                    mediaPlayer!!.prepare()
                    songPlaying = add.toString()
                    chooseSongButton.text = "Choosed"
                }
                else if (fileChoosing == "lyrics") {
                    lyricsView.text = getStringFromUri(add!!)
                    chooseLyricsButton.text = "Choosed"
                }
            }
        }
    }
    private fun getStringFromUri(uri: Uri): String {
        var path = uri.path
        path = path!!.substring(path.indexOf(':') + 1)
        path = Environment.getExternalStorageDirectory().path + "/" + path
        val bufferedReader = File(path).bufferedReader()
        val result = bufferedReader.use { it.readText() }
        return result
    }

    private fun onCreateDialog() {
        var playing = false
        val dialogBuilder = AlertDialog.Builder(this)
        val popupView: View = layoutInflater.inflate(R.layout.save_song, null)
        val songNameEditText: EditText = popupView.findViewById(R.id.songNameEditText)
        val playMusicButton: Button = popupView.findViewById(R.id.playMusicButton)
        val submitButton: Button = popupView.findViewById(R.id.submitButton)
        val cancelButton: Button = popupView.findViewById(R.id.cancelButton)

        cancelButton.setOnClickListener {
            if (playing) {
                stopPlayingSong()
                playing = false
            }
            val file = File(getRecordingPath("Test"))
            file.delete()
            dialog.dismiss()
        }
        submitButton.setOnClickListener {
            val file = File(getRecordingPath("Test"))
            if (songNameEditText.text.toString() != "") {
                file.renameTo(File(getRecordingPath(songNameEditText.text.toString())))
                val song = Song.Builder(
                    songNameEditText.text.toString(),
                    songNameEditText.text.toString(),
                    "",
                    "",
                    "",
                    getRecordingPath(songNameEditText.text.toString())
                ).build()
                Log.i("Record", song.realName + song.source)
                if (utils.addSongToJSONFile(song, applicationContext) &&
                    utils.addSongToPlaylistJson(song, PLAYLISTNAME)
                ) {
                    Toast.makeText(this, "Recording is saved", Toast.LENGTH_SHORT).show()
                    file.delete()
                    if (playing) {
                        stopPlayingSong()
                        playing = false
                    }
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "Song's name is duplicated", Toast.LENGTH_SHORT).show()
                }
            }
            else Toast.makeText(this, "Song's name can't be blank", Toast.LENGTH_SHORT).show()

        }
        playMusicButton.setOnClickListener {
            if (!playing) {
                prepareSong(getRecordingPath("Test"))
                startPlayingSong()
                playing = true
            }
            else {
                stopPlayingSong()
                playing = false
            }

        }
        dialogBuilder.setView(popupView)
        dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun requestRecordingPermission() {
        ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.RECORD_AUDIO), REQUEST_AUDIO_PERMISSION_CODE)
    }
    private fun checkRecordingPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            requestRecordingPermission()
            return false
        }
        return true
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


    companion object {
        // String for LogCat documentation
        private const val TAG = "testing buttons"
        private const val PLAYLISTNAME = "Recording"
    }

}