package cheap.thrills;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import cheap.thrills.activities.LocationPermissionActivity;
import cheap.thrills.activities.MyWalletScreenActivity;
import cheap.thrills.activities.OrderIdLoginActivity;
import cheap.thrills.activities.TicketTempletActivity;
import cheap.thrills.activities.TicketTempletActivity2;
import cheap.thrills.activities.TicketTempletActivity3;
import cheap.thrills.utils.Constants;
import cheap.thrills.utils.UniversalOrlandoPreference;

public class SplashActivity extends AppCompatActivity {

    Activity maActivity = SplashActivity.this;
    String existingUser = "";
    String themepark_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setSplash();
    }

    private void setSplash() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean loggedIn = UniversalOrlandoPreference.readBoolean(SplashActivity.this, UniversalOrlandoPreference.IS_LOGIN, false);
                boolean isFirstLaunch = UniversalOrlandoPreference.readBoolean(SplashActivity.this, UniversalOrlandoPreference.IS_First_Launch, false);
                themepark_id = UniversalOrlandoPreference.readString(SplashActivity.this, UniversalOrlandoPreference.THEMEPARK_ID, "");
                if (!loggedIn) {
                    if(!isFirstLaunch)
                        startActivity(new Intent(maActivity, LocationPermissionActivity.class));
                    else
                        startActivity(new Intent(maActivity, OrderIdLoginActivity.class));
                } else {
                    if (themepark_id.equals("2")){
                        startActivity(new Intent(maActivity, TicketTempletActivity.class));
                    }else if (themepark_id.equals("4")){
                        startActivity(new Intent(maActivity, TicketTempletActivity2.class));
                    }else if (themepark_id.equals("3")){
                        startActivity(new Intent(maActivity, TicketTempletActivity3.class));
                    }else {
                        startActivity(new Intent(maActivity, MyWalletScreenActivity.class));
                    }
                }
                finish();
            }
        }, Constants.SPLASH_TIME_OUT);
    }

}
