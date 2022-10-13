package ca.unb.mobiledev.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

    class RecordActivity: AppCompatActivity() {
        @SuppressLint("WrongViewCast")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            setContentView(R.layout.record);

            val pauseButton = findViewById<Button>(R.id.pauseButton)
            pauseButton.setOnClickListener {
                Log.i(TAG, "pause button works!")


            }

            val stopButtons = findViewById<Button>(R.id.stopButton)
            stopButtons.setOnClickListener {

                Log.i(TAG, "stop button works!")
            }

            val recordButtons = findViewById<Button>(R.id.recordButton)
            recordButtons.setOnClickListener {
                Log.i(TAG, "record button works!")

            }


        }



        companion object {
            // String for LogCat documentation
            private const val TAG = "testing buttons"
        }

    }