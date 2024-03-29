package com.example.ebroapp.utils.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import android.location.LocationManager.NETWORK_PROVIDER
import com.example.ebroapp.utils.map.PermissionUtil.checkLocationPermission

class LocationListener(private val context: Context) :
    LocationListener {

    private var locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private var callback: ((Location) -> Unit)? = null

    override fun onLocationChanged(location: Location) {
        callback?.invoke(location)
    }

    @SuppressLint("MissingPermission")
    override fun onProviderEnabled(provider: String) {
        locationManager.getLastKnownLocation(provider)?.let { location ->
            if (provider == NETWORK_PROVIDER) {
                callback?.invoke(location)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun setOnLocationListener(listener: (Location) -> Unit) {
        if (checkLocationPermission(context)) {
            callback = listener
            if (locationManager.isProviderEnabled(GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(GPS_PROVIDER, MIN_UPDATE_TIME, 0f, this)
                locationManager.getLastKnownLocation(GPS_PROVIDER)?.let { location ->
                    callback?.invoke(location)
                }
            } else if (locationManager.isProviderEnabled(NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(NETWORK_PROVIDER, MIN_UPDATE_TIME, 0f, this)
                locationManager.getLastKnownLocation(NETWORK_PROVIDER)?.let { location ->
                    callback?.invoke(location)
                }
            }
        }
    }

//    private fun stop() {
//        locationManager.removeUpdates(this)
//    }

    companion object {
        const val MIN_UPDATE_TIME = 1000L * 60L * 60L * 24L
    }
}
