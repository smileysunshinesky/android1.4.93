package cheap.thrills.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.HandlerThread
import android.util.Log
import androidx.core.app.ActivityCompat

class LocationHelper {
    var LOCATION_REFRESH_TIME = 5000 // 3 seconds. The Minimum Time to get location update
    var LOCATION_REFRESH_DISTANCE =
        0 // 0 meters. The Minimum Distance to be changed to get location
    lateinit var listener: MyLocationListener
    lateinit var context: Context

    @SuppressLint("MissingPermission")
    fun startListeningUserLocation(context: Context, myListener: MyLocationListener) {
        this.context = context
        this.listener = myListener

        val mLocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationListener: LocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                Log.d("location", location.latitude.toString())
                listener.onLocationChanged(location) // calling listener to inform that updated location is available
            }

            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        }
        mLocationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            LOCATION_REFRESH_TIME.toLong(),
            LOCATION_REFRESH_DISTANCE.toFloat(),
            locationListener
        )
    }
}

interface MyLocationListener {
    fun onLocationChanged(location: Location?)
}
