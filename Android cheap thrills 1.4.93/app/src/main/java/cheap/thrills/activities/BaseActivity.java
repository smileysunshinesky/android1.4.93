package cheap.thrills.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cheap.thrills.R;
import cheap.thrills.UniversalResortApplication;
import cheap.thrills.utils.Constants;
import cheap.thrills.utils.UniversalOrlandoPreference;
import io.github.rupinderjeet.kprogresshud.KProgressHUD;

public class BaseActivity extends AppCompatActivity {
    public KProgressHUD kProgressHUD;
    public static final int REQUEST_PERMISSION_MICROPHONE = 111;

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.checkMicrophonePermission();
    }

    //Show Alert
    public void showToastAlert(Activity mActivity, String strMessage) {
        Toast.makeText(mActivity, strMessage, Toast.LENGTH_SHORT).show();
    }

    //Check Internet Connection
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    //Check Audio Record
    public void checkMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,  new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_PERMISSION_MICROPHONE);
        }
    }

    //Show Progress Dialog
    public void showProgressDialog(Context context) {
        kProgressHUD = KProgressHUD.create(context).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.pleaseWait))
                .setCancellable(true)

                .setAnimationSpeed(Constants.PROGRESS)
                .setDimAmount(Constants.PROGRESSAMOUNT)
                .show();
    }
    //Hide Progress Dialog
    public void dismissProgressDailog() {
        kProgressHUD.dismiss();
    }

    //Error Alert Dialog
    public void alertErrorDialog(Activity mActivity, String s) {
        final Dialog alertDialog = new Dialog(mActivity);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.dialog_reminder);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tvOk = (TextView) alertDialog.findViewById(R.id.tvOk);
        TextView tvoops = (TextView) alertDialog.findViewById(R.id.tvOops);
        tvoops.setText(s);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }

    public String getDate() {
        String timeStamp = new SimpleDateFormat(getString(R.string.datePattern)).format(Calendar.getInstance().getTime());
        return timeStamp;
    }

    public void logOut(Activity activity) {
        UniversalResortApplication.profile = null;
        SharedPreferences preferences = UniversalOrlandoPreference.getPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(UniversalOrlandoPreference.IS_LOGIN,false);
        editor.clear();
        editor.commit();
        Intent i = new Intent(activity, OrderIdLoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(i);
        activity.finish();
    }

    public void logOutAlert(Activity activity) {
        UniversalResortApplication.profile = null;
        SharedPreferences preferences = UniversalOrlandoPreference.getPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(UniversalOrlandoPreference.IS_LOGIN,false);
        editor.clear();
        editor.commit();
        Intent i = new Intent(activity, LogoutAlertActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(i);
        activity.finish();
    }



}