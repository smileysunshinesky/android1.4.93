package cheap.thrills.location

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.client.Socket.*
import io.socket.emitter.Emitter
import java.net.URISyntaxException

object SocketHandler {

    private var mSocket: Socket?= null
    var message = ""
    var location: UserData.Coordinate? = null

    private const val TAG = "SocketHandler"
    @Synchronized
    fun initSocket() {

        try {
            mSocket = IO.socket("http://18.117.240.68:8080")
        } catch (e: URISyntaxException) {
            Log.d(TAG, "error $e")
        }

        mSocket?.let {
            it.on(EVENT_DISCONNECT, Emitter.Listener {
                Log.d(TAG, "socket disconnected")
            })
            it.on(EVENT_PING, Emitter.Listener {
                Log.d(TAG, "socket ping")
            })
            it.on(EVENT_CONNECT, Emitter.Listener {
                Log.d(TAG, "socket connected")
            })
            it.on(EVENT_PONG, Emitter.Listener {
                Log.d(TAG, "socket pong")
            })
            it.on(EVENT_CONNECT_ERROR, Emitter.Listener {
                Log.d(TAG, "socket connection error")
            })
            it.on(EVENT_CONNECT_TIMEOUT, Emitter.Listener {
                Log.d(TAG, "socket connection time out")
            })
        }

        establishConnection()

    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket!!
    }

    @Synchronized
    fun establishConnection() {
        mSocket?.connect()
    }

    @Synchronized
    fun closeConnection() {
        mSocket?.disconnect()
    }
}