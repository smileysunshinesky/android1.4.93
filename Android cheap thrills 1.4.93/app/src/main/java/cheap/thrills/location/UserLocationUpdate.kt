package cheap.thrills.location

import android.content.Context
import android.location.Location
import android.util.Log
import cheap.thrills.utils.UniversalOrlandoPreference

class UserLocationUpdate {
    private val TAG = "LocationService"
    private lateinit var mcontext: Context
    fun updateUserLocation(location: Location,mcontext: Context) {
        this.mcontext = mcontext
        val coordinates = UserData.Coordinate(location.latitude, location.longitude)
        val (orderId, userId, name, gid, status) = getUserData()
        val message = Utils.toJson(coordinates, userId, name, status, orderId, gid)
        pushSocket(message)
    }

    private fun pushSocket(message: String) {
        Log.d(TAG, "pushSocket: $message")
        if (SocketHandler.getSocket().connected()) {
            SocketHandler.getSocket().emit("lastKnownLocation", message)
        } else {
            SocketHandler.initSocket()
        }
    }

    fun getUserData(): Array<String> {
        var orderId = ""
        var mobile = ""
        var gid = ""
        var status = "offline"
        if (UniversalOrlandoPreference.readString(
                mcontext,
                UniversalOrlandoPreference.MOBILE,
                ""
            ) != null
        ) {
            mobile = UniversalOrlandoPreference.readString(
                mcontext,
                UniversalOrlandoPreference.MOBILE,
                ""
            )
        }
        if (UniversalOrlandoPreference.readString(
                mcontext,
                UniversalOrlandoPreference.LOGGEDIN_ORDERID,
                ""
            ) != null
        ) {
            orderId = UniversalOrlandoPreference.readString(
                mcontext,
                UniversalOrlandoPreference.LOGGEDIN_ORDERID,
                ""
            )
        }
        if (UniversalOrlandoPreference.readString(
                mcontext,
                UniversalOrlandoPreference.GID,
                ""
            ) != null
        ) {
            gid =
                UniversalOrlandoPreference.readString(mcontext, UniversalOrlandoPreference.GID, "")
        }

        val userId =
            UniversalOrlandoPreference.readString(mcontext, UniversalOrlandoPreference.LOGIN_ID, "")
        val name =
            UniversalOrlandoPreference.readString(mcontext, UniversalOrlandoPreference.USER_NAME, "")
        if (UniversalOrlandoPreference.readBoolean(
                mcontext,
                UniversalOrlandoPreference.IS_LOGIN,
                false
            )
        )
            status = "online"

        return arrayOf(orderId, userId, name, gid, status)
    }
}