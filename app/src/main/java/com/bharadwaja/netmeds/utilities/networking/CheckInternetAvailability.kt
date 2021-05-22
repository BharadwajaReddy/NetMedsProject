package com.bharadwaja.netmeds.utilities.networking

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class CheckInternetAvailability {

    fun isNetworkAvailbale(context: Context): Boolean {
        val conManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val internetInfo: NetworkInfo? = conManager.activeNetworkInfo
        return internetInfo != null && internetInfo.isConnected
    }
}