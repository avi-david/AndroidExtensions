package com.avinashdavid.androidextensions.context

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.support.v4.content.ContextCompat
import android.util.TypedValue

/**
 * @param url: URL string
 * @param uri: Android URI
 * @return Opens the link if the URL or URI are valid and viewable by any available applications on the system
 */
fun Context.openLink(url: String? = null, uri: Uri? = null) {
    if (url == null && uri == null) return
    val i = url?.let {
        try {
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }
        } catch (e: Exception) {
            null
        }
    } ?: run {
        uri?.let {
            Intent(Intent.ACTION_VIEW).apply {
                data = it
            }
        }
    }
    i?.let {
        this.startActivity(it)
    }
}

/**
 * @param dp: density independent pixels
 * @return Number of raw pixels
 */
fun Context.dpToPixels(dp: Int) : Int {
    val dm = resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), dm).toInt()
}

/**
 * @param drawableResourceId: Drawable resource ID that is accessible from this context
 * @param colorResourceId: Color resource ID that is accessible from this context
 */
fun Context.getDrawable(drawableResourceId: Int, colorResourceId: Int?) : Drawable? {
    val drawable = ContextCompat.getDrawable(this, drawableResourceId)
    colorResourceId?.let {
        //TODO: support for SDK_INT < 21
        drawable?.setTint(ContextCompat.getColor(this, colorResourceId))
    }

    return drawable
}

/**
 * @return true if smallestScreenWidthDp is 600 or more
 */
fun Context.isTablet() = this.resources.configuration.smallestScreenWidthDp >= 600

//REGION: Network connectivity
abstract class NetworkChangeReceiver : BroadcastReceiver() {
    /**
     * called when a connectivity change has occurred
     * @see Register this receiver with an intent filter for 'android.net.ConnectivityManager.CONNECTIVITY_ACTION'
     */
    abstract fun onConnectivityChange()

    override fun onReceive(p0: Context?, p1: Intent?) {
        p0?.let {
            if (p1?.action == android.net.ConnectivityManager.CONNECTIVITY_ACTION) {
                onConnectivityChange()
            }
        }
    }
}

/**
 * @return true if the device has any network connectivity
 */
fun Context.isConnectedToInternet() : Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    return activeNetwork?.isConnectedOrConnecting == true
}

/**
 * @throws RuntimePermission exception if android.permission.ACCESS_NETWORK_STATE is not registered in Android Manifest
 * @return true if WiFi is connected
 */
fun Context.checkIfWifiIsConnected() : Boolean {
    val connManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
        if (Build.VERSION.SDK_INT >= 23) {
            connManager?.activeNetwork?.let {
                return connManager.getNetworkCapabilities(it).hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            }
        } else {
            val activeNetworkInfo = connManager?.activeNetworkInfo
            return activeNetworkInfo?.type == ConnectivityManager.TYPE_WIFI && activeNetworkInfo.isConnected
        }
    }
    return false
}
///REGION