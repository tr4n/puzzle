package com.tr4n.puzzle.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network

/**
 * Class check connection for application
 */

enum class ConnectionType {
    CONNECTED, DISCONNECTED
}


class NetworkConnectionUtil(private val context: Context) {

    var connectionResult: (type: ConnectionType) -> Unit = {}

    var isConnected = false
        private set

    private val networkCallBack: ConnectivityManager.NetworkCallback by lazy {
        object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                super.onLost(network)
                isConnected = false
                connectionResult(ConnectionType.DISCONNECTED)
            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                isConnected = true
                connectionResult(ConnectionType.CONNECTED)
            }
        }
    }

    @Suppress("DEPRECATION")
    private val networkChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val connectivityManager =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo == null) {
                isConnected = false
                connectionResult(ConnectionType.DISCONNECTED)
                return
            }
            if (activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI
                || activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE
            ) {
                isConnected = true
                connectionResult(ConnectionType.CONNECTED)
            }
        }
    }

    /**
     * Register for the first activity when launching the app (in onCreate method)
     * Example: SplashActivity
     */
    fun registerNetworkCallListener() {
        //Android 9 and above
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager.activeNetwork == null) {
            isConnected = false
            connectionResult(ConnectionType.DISCONNECTED)
        }
        connectivityManager.registerDefaultNetworkCallback(networkCallBack)
    }

    /**
     * Unregister from the last activity (in onDestroy method)
     * Example: SampleActivity
     */
    fun unregisterNetworkCallListener() {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.unregisterNetworkCallback(networkCallBack)
    }
}
