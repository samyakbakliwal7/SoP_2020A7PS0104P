package com.example.sop2020a7ps0104p

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import android.net.wifi.WifiManager
import android.net.wifi.p2p.*
import android.net.wifi.p2p.WifiP2pManager.ActionListener
import android.widget.*

class MainActivity : AppCompatActivity() {


    private lateinit var toggleButtonWifi : Button
    private lateinit var writeMessageEditTextView: EditText
    lateinit var discoverPeerButton: Button
    lateinit var connectionStatusTextView: TextView
    lateinit var peerListView: ListView
    lateinit var sendButton: Button
    lateinit var readMessage : TextView
    private lateinit var wifiManager: WifiManager
    private lateinit var mManager: WifiP2pManager
    private lateinit var mChannel: WifiP2pManager.Channel
    private lateinit var mReceiver: BroadcastReceiver
    private lateinit var mIntentFilter : IntentFilter
    var peers = ArrayList<WifiP2pDevice> ()
    private var deviceNameArray = ArrayList<String>()
    var deviceArray = ArrayList<WifiP2pDevice>()

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
                wifiManager!!.setWifiEnabled(true)
                toggleButtonWifi.text = "ON"
            }
        }


        discoverPeerButton.setOnClickListener {
            mManager?.discoverPeers(mChannel, object : WifiP2pManager.ActionListener {

                override fun onSuccess() {
                    connectionStatusTextView.text = "Connection established"
                }

                override fun onFailure(reasonCode: Int) {
                   connectionStatusTextView.text = "Connection cannot be established"
                }
            })
        }

        peerListView.setOnItemClickListener { parent, view, position, id ->
            val device :WifiP2pDevice = parent.getItemAtPosition(position) as  WifiP2pDevice
            var config = WifiP2pConfig()
            config.deviceAddress = device.deviceAddress
            //lateinit var listener: WifiP2pManager.ActionListener

            mManager?.connect(mChannel, config, object : WifiP2pManager.ActionListener{
                override fun onSuccess()
                {
                    Toast.makeText( applicationContext, "Connected to ${device.deviceName}" , Toast.LENGTH_SHORT ).show()
                }
                override fun onFailure(i:Int)
                {
                    Toast.makeText( applicationContext ,"Not Connected" , Toast.LENGTH_SHORT ).show()
                }
            })
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
        mManager = getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        mChannel = mManager.initialize(applicationContext, Looper.getMainLooper() ,null)
        mReceiver = WifiDirectBroadcastReceiver(mManager , mChannel , this)
        mIntentFilter = IntentFilter()
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
    }

    var peerListListener = WifiP2pManager.PeerListListener() {
        fun onPeersAvailable(peerList: WifiP2pDeviceList) {
            if(!peerList.deviceList.equals(peers))
            {
                peers.clear()
                peers.addAll(peerList.deviceList)
                deviceNameArray = ArrayList<String>(peerList.deviceList.size)
                deviceArray = ArrayList<WifiP2pDevice>(peerList.deviceList.size)

                var index:Int =0

                var device : WifiP2pDevice
                for(device in peerList.deviceList)
                {
                    deviceNameArray.set(index,device.deviceName)
                    deviceArray.set(index , device)
                    index += 1
                }

                var adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1 , deviceNameArray)
                peerListView.adapter = adapter

            }

            if(peers.size == 0)
            {
                Toast.makeText(applicationContext , "No device found " , Toast.LENGTH_LONG).show()
                return

            }

        }
    }

    var connectionInfoListener = WifiP2pManager.ConnectionInfoListener {

        fun onConnectionInfoAvailable(wifiP2pInfo: WifiP2pInfo)
        {
            val groupOwnerAddress = wifiP2pInfo.groupOwnerAddress

            if(wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner)
            {
                connectionStatusTextView.text = "Host"
            }
            else if(wifiP2pInfo.groupFormed)
            {
                connectionStatusTextView.text = "Client"
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(mReceiver , mIntentFilter)
    }


    override fun onPause() {
        super.onPause()
        unregisterReceiver(mReceiver)
    }

}