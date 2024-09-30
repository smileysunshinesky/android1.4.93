package cheap.thrills.location;

import static io.socket.client.Socket.EVENT_CONNECT;
import static io.socket.client.Socket.EVENT_CONNECT_ERROR;
import static io.socket.client.Socket.EVENT_DISCONNECT;

import android.util.Log;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by ms3 ctfinal.
 */

public class WebSocket {

    private Socket mSocket;
    private static final String TAG = "WebSocket";

    public Socket getSocket() {
        return mSocket;
    }

    public void initSocket(){

        if (mSocket == null) {
            try {
                mSocket = IO.socket("http://3.140.108.123:8080");
            } catch (URISyntaxException e) {
                Log.d(TAG, "initSocket: ");
            }
        }

        mSocket.on(EVENT_DISCONNECT, args -> {
            Log.d(TAG, "EVENT_DISCONNECT: ");
        });

        mSocket.on(EVENT_CONNECT, args -> {
            Log.d(TAG, "EVENT_CONNECT: ");
        });

        mSocket.on(EVENT_CONNECT_ERROR, args -> {
            Log.d(TAG, "EVENT_CONNECT_ERROR: ");
        });

        if (!mSocket.connected()) mSocket.connect();
    }

    public void pushSocket(String message){

        Log.d(TAG, "pushSocket: "+message);
        if (mSocket.connected()) {
            mSocket.emit("lastKnownLocation", message);
        }
    }

}
