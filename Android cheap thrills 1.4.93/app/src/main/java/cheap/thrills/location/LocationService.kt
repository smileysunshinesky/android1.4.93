package cheap.thrills.location

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import cheap.thrills.R
import cheap.thrills.location.SocketHandler.getSocket
import cheap.thrills.location.SocketHandler.initSocket
import cheap.thrills.location.Utils.toJson
import cheap.thrills.utils.UniversalOrlandoPreference
import java.util.*

class LocationService : Service() {
    private val NOTIFICATION_CHANNEL_ID = "my_notification_location"
    private val TAG = "LocationService"
    override fun onCreate() {
        super.onCreate()
        isServiceStarted = true
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setOngoing(false)
                .setSmallIcon(R.mipmap.ic_logo_cheapthrills)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_ID, NotificationManager.IMPORTANCE_LOW
            )
            notificationChannel.description = NOTIFICATION_CHANNEL_ID
            notificationChannel.setSound(null, null)
            notificationManager.createNotificationChannel(notificationChannel)
            startForeground(1, builder.build())
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val onceLoggedIn = UniversalOrlandoPreference.readBoolean(
            this,
            UniversalOrlandoPreference.ONCE_LOGGED_IN,
            false
        );
        if (onceLoggedIn) {
            startListener()
        }
        return START_STICKY
    }

    private fun startListener() {
        initSocket()
        LocationHelper().startListeningUserLocation(
            this, object : MyLocationListener {
                override fun onLocationChanged(location: Location?) {

                    Log.d(TAG, "run: Running ${location?.latitude}")
                    mLocation = location
                    mLocation?.let {
                        AppExecutors.instance?.networkIO()?.execute {
                            updateUserLocation(it)
                        }
                    }
                }
            })
    }

    fun updateUserLocation(location: Location) {
        val coordinates = UserData.Coordinate(location.latitude, location.longitude)
        val (orderId, userId, name, gid, status) = getUserData()
        val message = toJson(coordinates, userId, name, status, orderId, gid)
        pushSocket(message)
    }

    private fun pushSocket(message: String) {
        Log.d(TAG, "pushSocket: $message")
        if (getSocket().connected()) {
            getSocket().emit("lastKnownLocation", message)
        } else {
            initSocket()
        }
    }

    fun getUserData(): Array<String> {
        var orderId = ""
        var mobile = ""
        var gid = ""
        var status = "offline"
        if (UniversalOrlandoPreference.readString(
                this,
                UniversalOrlandoPreference.MOBILE,
                ""
            ) != null
        ) {
            mobile = UniversalOrlandoPreference.readString(
                this,
                UniversalOrlandoPreference.MOBILE,
                ""
            )
        }
        if (UniversalOrlandoPreference.readString(
                this,
                UniversalOrlandoPreference.LOGGEDIN_ORDERID,
                ""
            ) != null
        ) {
            orderId = UniversalOrlandoPreference.readString(
                this,
                UniversalOrlandoPreference.LOGGEDIN_ORDERID,
                ""
            )
        }
        if (UniversalOrlandoPreference.readString(
                this,
                UniversalOrlandoPreference.GID,
                ""
            ) != null
        ) {
            gid =
                UniversalOrlandoPreference.readString(this, UniversalOrlandoPreference.GID, "")
        }

        val userId =
            UniversalOrlandoPreference.readString(this, UniversalOrlandoPreference.LOGIN_ID, "")
        val name =
            UniversalOrlandoPreference.readString(this, UniversalOrlandoPreference.USER_NAME, "")
        if (UniversalOrlandoPreference.readBoolean(
                this,
                UniversalOrlandoPreference.IS_LOGIN,
                false
            )
        ) {
            status = "online"
        } else {
            status = "offline"
        }


        return arrayOf(orderId, userId, name, gid, status)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        isServiceStarted = false

    }

    companion object {
        var mLocation: Location? = null
        var isServiceStarted = false
    }


}


