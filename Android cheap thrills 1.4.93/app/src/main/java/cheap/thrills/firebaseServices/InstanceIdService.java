package cheap.thrills.firebaseServices;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by devil on 3/7/18.
 */

public class InstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
    }
}
