package com.example.ebroapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import io.reactivex.functions.Consumer

class CustomLocationListener(context: Context) : LocationListener {

    private var currentLocation: Location? = null
    private var consumer: Consumer<Location>? = null
    var locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @SuppressLint("MissingPermission")
    fun setUpLocationListener(consumer: Consumer<Location>) {
        this.consumer = consumer
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)
            currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        }
    }

    fun stop() {
        locationManager.removeUpdates(this)
    }

    override fun onLocationChanged(location: Location) {
        currentLocation = location
        consumer?.accept(currentLocation)
    }

    @SuppressLint("MissingPermission")
    override fun onProviderEnabled(provider: String) {
        val location = locationManager.getLastKnownLocation(provider)
        if (provider == LocationManager.NETWORK_PROVIDER) {
            consumer?.accept(location)
        }
    }
}