package ca.unb.mobiledev.myapplication

import android.Manifest
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.sql.Time
import java.util.*
import java.util.concurrent.Executors

class RecordActivity: AppCompatActivity() {

    private val REQUEST_AUDIO_PERMISSION_CODE = 101
    lateinit var recordButton: ImageButton
    lateinit var displayTimer: TextView
    lateinit var timer: CountDownTimer
    lateinit var chooseSongButton: Button

    private var maximumSeconds: Long = 10000
    private var intervalSeconds: Long = 1

    private var isRecording = false
    private var mediaRecorder: MediaRecorder? = null
    private var handler: Handler = Handler()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.record);

        displayTimer = findViewById(R.id.lblTimer)
        chooseSongButton = findViewById(R.id.chooseSongButton)
        recordButton = findViewById(R.id.recordButton)

        setupTimer()
        recordButton.setBackgroundResource(R.drawable.start_recording)
        recordButton.setOnClickListener {
            if (!isRecording) {
                if (checkRecordingPermission()) {
                    startRecording()
                } else {
                    requestRecordingPermission()
                }
            }
            else {
                stopRecording()
            }
        }
        chooseSongButton.setOnClickListener {

        }
    }

    private fun stopRecording() {
        isRecording = false
        timer.cancel()
        displayTimer.text = "00:00:00"
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
        Toast.makeText(this, "Recording is saved", Toast.LENGTH_SHORT).show()
    }
    private fun startRecording() {
        isRecording = true
        timer.start()
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
                        recordButton.setBackgroundResource(R.drawable.stop_recording)
                    }

                })
            }

        })
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


    companion object {
        // String for LogCat documentation
        private const val TAG = "testing buttons"
    }

}