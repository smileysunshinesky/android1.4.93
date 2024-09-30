package cheap.thrills;

import android.app.Application;
import android.content.Context;

import cheap.thrills.models.Data;

/**
 * Created by android-da on 10/23/18.
 */

public class UniversalResortApplication extends Application {
    public static Data profile;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        UniversalResortApplication.context = getApplicationContext();
    }

    public static Context getContext() {
        return UniversalResortApplication.context;
    }
}