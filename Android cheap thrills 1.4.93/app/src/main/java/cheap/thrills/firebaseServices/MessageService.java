package cheap.thrills.firebaseServices;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class
MessageService extends FirebaseMessagingService {

    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if(!remoteMessage.equals("")){
          }
    }

}
