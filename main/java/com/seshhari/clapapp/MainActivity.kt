package com.seshhari.clapapp

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var seekbar : SeekBar
    private lateinit var runnable: Runnable
    private lateinit var handler: Handler
    private var mediaPlayer : MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        seekbar = findViewById(R.id.sbClapping)
        handler = Handler(Looper.getMainLooper())

        val playButton = findViewById<FloatingActionButton>(R.id.fabPlay)
        playButton.setOnClickListener {
            if(mediaPlayer == null)
            {
                mediaPlayer = MediaPlayer.create(this, R.raw.applauding)
                initializeSeekBar()
            }
            mediaPlayer?.start()

        }
        val pauseButton = findViewById<FloatingActionButton>(R.id.fabPause)
        pauseButton.setOnClickListener {
            mediaPlayer?.pause()

        }
        val stopButton = findViewById<FloatingActionButton>(R.id.fabStop)
        stopButton.setOnClickListener {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer = null
            handler.removeCallbacks(runnable)
            seekbar.progress = 0

        }
    }
    private fun initializeSeekBar()
    {
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener
        {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean)
            {
                if (fromUser) mediaPlayer?.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        val tvPlayed = findViewById<TextView>(R.id.tvPlayed)
        val tvDue = findViewById<TextView>(R.id.tvDue)
        seekbar.max = mediaPlayer!!.duration
        runnable = Runnable {
            seekbar.progress = mediaPlayer!!.currentPosition
            val playedTime =  mediaPlayer!!.currentPosition/1000
            tvPlayed.text = "$playedTime secs"
            val duration = mediaPlayer!!.duration/1000
            val dueTime = duration-playedTime
            tvDue.text = "$dueTime secs"

            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)

    }
}