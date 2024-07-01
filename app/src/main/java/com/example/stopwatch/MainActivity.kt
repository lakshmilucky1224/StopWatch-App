package com.example.stopwatch

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvTimer: TextView
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button
    private lateinit var btnReset: Button
    private lateinit var btnLap: Button
    private lateinit var lvLaps: ListView

    private var startTime = 0L
    private var timeInMillis = 0L
    private var timeSwapBuff = 0L
    private var updateTime = 0L

    private val handler = Handler()
    private val laps = mutableListOf<String>()
    private lateinit var lapsAdapter: ArrayAdapter<String>

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            timeInMillis = SystemClock.uptimeMillis() - startTime
            updateTime = timeSwapBuff + timeInMillis

            val secs = (updateTime / 1000).toInt()
            val mins = secs / 60
            val hrs = mins / 60
            val milliseconds = (updateTime % 1000).toInt()

            tvTimer.text = String.format("%02d:%02d:%02d:%03d", hrs, mins % 60, secs % 60, milliseconds)
            handler.postDelayed(this, 0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvTimer = findViewById(R.id.tvTimer)
        btnStart = findViewById(R.id.btnStart)
        btnStop = findViewById(R.id.btnStop)
        btnReset = findViewById(R.id.btnReset)
        btnLap = findViewById(R.id.btnLap)
        lvLaps = findViewById(R.id.lvLaps)

        lapsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, laps)
        lvLaps.adapter = lapsAdapter

        btnStart.setOnClickListener {
            startTime = SystemClock.uptimeMillis()
            handler.postDelayed(runnable, 0)
            btnStart.isEnabled = false
            btnStop.isEnabled = true
            btnLap.isEnabled = true
        }

        btnStop.setOnClickListener {
            timeSwapBuff += timeInMillis
            handler.removeCallbacks(runnable)
            btnStart.isEnabled = true
            btnStop.isEnabled = false
            btnLap.isEnabled = false
        }

        btnReset.setOnClickListener {
            startTime = 0L
            timeInMillis = 0L
            timeSwapBuff = 0L
            updateTime = 0L
            tvTimer.text = "00:00:00:000"
            laps.clear()
            lapsAdapter.notifyDataSetChanged()
            btnStart.isEnabled = true
            btnStop.isEnabled = false
            btnLap.isEnabled = false
        }

        btnLap.setOnClickListener {
            laps.add(tvTimer.text.toString())
            lapsAdapter.notifyDataSetChanged()
        }
    }
}
