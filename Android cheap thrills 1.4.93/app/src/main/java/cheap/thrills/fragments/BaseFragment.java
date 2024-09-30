package cheap.thrills.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import cheap.thrills.R;
import cheap.thrills.UniversalResortApplication;
import cheap.thrills.activities.LogoutAlertActivity;
import cheap.thrills.activities.OrderIdLoginActivity;
import cheap.thrills.utils.Constants;
import cheap.thrills.utils.UniversalOrlandoPreference;
import io.github.rupinderjeet.kprogresshud.KProgressHUD;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {
    public KProgressHUD kProgressHUD;
    View mView;

    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_base, null);
        return mView;
    }


    public void showToast(Context context, String m) {
        Toast.makeText(context, m, Toast.LENGTH_SHORT).show();
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public void showProgressDialog(Context context) {
        kProgressHUD = KProgressHUD.create(context).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.pleaseWait))
                .setCancellable(true)
                .setAnimationSpeed(Constants.PROGRESS)
                .setDimAmount(Constants.PROGRESSAMOUNT)
                .show();
    }

    public void dismissProgressDailog() {
        kProgressHUD.dismiss();
    }

    public void alertErrorDialog(Activity mActivity) {
        final Dialog alertDialog = new Dialog(mActivity);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.dialog_reminder);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tvOk = (TextView) alertDialog.findViewById(R.id.tvOk);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
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
