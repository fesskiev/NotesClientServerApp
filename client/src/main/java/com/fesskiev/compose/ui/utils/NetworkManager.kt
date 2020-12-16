package com.fesskiev.compose.ui.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest

class NetworkManager(context: Context) {

    var isNetworkAvailable: Boolean = false

    init {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val builder: NetworkRequest.Builder = NetworkRequest.Builder()
        connectivityManager.registerNetworkCallback(builder.build(), object : ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {
                    isNetworkAvailable = true
                }

                override fun onLost(network: Network) {
                    isNetworkAvailable = false
                }
            })
    }
}