package cheap.thrills.location

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson


object Utils {

    fun hasInternetConnection(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

        val activeNetwork = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.activeNetwork ?: return false
        } else {
            null
        }
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    fun showErrorSnakeBar(
        view: View,
        text: String
    ) {
        val snackbar = Snackbar.make(
            view, text,
            Snackbar.LENGTH_LONG
        ).setAction("Ok") {

        }.setDuration(Snackbar.LENGTH_LONG)
        snackbar.setActionTextColor(Color.WHITE)
        val snackbarView = snackbar.view
        val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        snackbar.show()
    }


    fun isLocationServiceEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

    fun showGPSAlert(
        context: Context, function: () -> Unit
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("GPS OFF Alert")
        builder.setMessage("Turn GPS on for location updates")
        builder.setPositiveButton("YES") { dialog, which ->
            dialog.dismiss()
            goToLocationGPS(context)
        }
        builder.setNegativeButton("CANCEL") { dialog, which ->
            dialog.dismiss()
            function()
        }
        val dialog = builder.create()
        dialog.show()

    }

    fun goToLocationGPS(context: Context) {
        val intent = Intent()
        intent.action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
        context.startActivity(intent)
    }

    fun toJson(loc: UserData.Coordinate, userId: String, name: String, status: String , orderId: String, gid: String): String {
        val data = UserData(loc, userId,name, status, orderId, "1", gid)
        val gson = Gson()
        val json = gson.toJson(data)
        return json
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showGPSAlert(
        context: Context, view: View) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("GPS OFF Alert")
        builder.setMessage("Turn GPS on for location updates")
        builder.setPositiveButton("YES") { dialog, which ->
            dialog.dismiss()
            goToLocationGPS(context)
        }
        builder.setNegativeButton("CANCEL") { dialog, which ->
            dialog.dismiss()
            showErrorSnakeBar(view, "Location Permission Needed")
        }
        val dialog = builder.create()
        dialog.show()

    }





}