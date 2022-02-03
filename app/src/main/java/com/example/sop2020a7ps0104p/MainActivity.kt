package com.example.sop2020a7ps0104p

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {


    lateinit var toggleButtonWifi : Button
    lateinit var writeMessageEditTextView: EditText
    lateinit var discoverPeerButton: Button
    lateinit var connectionStatusTextView: TextView
    lateinit var peerListView: ListView
    lateinit var sendButton: Button
    lateinit var readMessage : TextView
    private var wifiManager: WifiManager ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeFunction()
        wifiButtonListener()


    }

    private fun wifiButtonListener() {

        toggleButtonWifi.setOnClickListener {
            if(wifiManager!!.isWifiEnabled)
            {
                wifiManager!!.setWifiEnabled(false)
                toggleButtonWifi.text = "OFF"
            }
            else
            {
                wifiManager!!.isWifiEnabled = false
                toggleButtonWifi.text = "ON"
            }
        }
    }

    private fun initializeFunction() {
        toggleButtonWifi = findViewById(R.id.toggle_wifi_on_off)
        discoverPeerButton = findViewById(R.id.discover_peer)
        connectionStatusTextView = findViewById(R.id.connectionStatus)
        peerListView = findViewById(R.id.list_view_peers_list)
        sendButton = findViewById(R.id.send_button)
        readMessage = findViewById(R.id.read_message_text_view)
        writeMessageEditTextView = findViewById(R.id.write_message_edit_text)

        // INTIALIZE WIFI MANAGER VARIABLE HERE
        wifiManager= applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
//        wifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager
    }
}