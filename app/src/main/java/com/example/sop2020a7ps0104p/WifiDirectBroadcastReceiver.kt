package com.example.sop2020a7ps0104p

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pManager
import android.widget.Toast

class WifiDirectBroadcastReceiver(private val manager: WifiP2pManager,
                                  private val channel: WifiP2pManager.Channel,
                                  private val activity: MainActivity) : BroadcastReceiver() {
    private var mManager: WifiP2pManager = manager
    private var mChannel: WifiP2pManager.Channel = channel
    private var mActivity: MainActivity = activity

    override fun onReceive(context: Context?, intent: Intent?) {
        var action: String? = intent?.action

        if(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action))
        {
            var state : Int? = intent?.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE , -1)
            if(state==WifiP2pManager.WIFI_P2P_STATE_ENABLED)
            {
                Toast.makeText(context , "Wi-Fi is ON"  , Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(context , "Wi-Fi is OFF"  , Toast.LENGTH_SHORT).show()
            }
        }
        else if(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action))
        {
            if(mManager!=null)
            {
                mManager.requestPeers(mChannel , mActivity.peerListListener)
            }
        }
        else if(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action))
        {
            if(mManager==null)
            {
                return
            }

            var networkInfo = intent?.getParcelableExtra<NetworkInfo>(WifiP2pManager.EXTRA_NETWORK_INFO)

            if(networkInfo.isConnected)
            {
                mManager.requestConnectionInfo(mChannel,mActivity.connectionInfoListener)
            }
            else
            {
                mActivity.connectionStatusTextView.text = "Device Disconnected"
            }
        }
        else if(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action))
        {

        }

    }
}