package com.example.sop2020a7ps0104p

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class MainActivity : AppCompatActivity() {


    lateinit var toggleButtonWifi : Button
    lateinit var textViewWifiOnOff : TextView
    lateinit var discoverPeerButton: Button
    lateinit var connectionStatusTextView: TextView
    lateinit var peerListView: ListView
    lateinit var sendButton: Button
    lateinit var readMessage : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toggleButtonWifi = findViewById(R.id.toggle_wifi_on_off)

    }
}