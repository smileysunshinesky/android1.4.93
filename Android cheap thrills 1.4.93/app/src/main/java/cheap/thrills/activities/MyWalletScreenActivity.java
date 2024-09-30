package cheap.thrills.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cheap.thrills.R;
import cheap.thrills.UniversalResortApplication;
import cheap.thrills.adapters.MyWalletAdapter;
import cheap.thrills.fonts.TextViewArialBold;
import cheap.thrills.fonts.TextViewGotham;
import cheap.thrills.location.LocationService;
import cheap.thrills.location.Utils;
import cheap.thrills.models.Order;
import cheap.thrills.models.Profile;
import cheap.thrills.retrofit.RestClient;
import cheap.thrills.utils.UniversalOrlandoPreference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyWalletScreenActivity extends BaseActivity  {
    private static final String TAG = "MyWalletScreenActivity";
    ArrayList<Order> mList = new ArrayList<>();
    Activity mActivity = MyWalletScreenActivity.this;

    ArrayList<Order> mActualList = new ArrayList<>();
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.rlMain)
    LinearLayout rlMain;
    @BindView(R.id.rlUsedTickets)
    RelativeLayout rlUsedTickets;
    MyWalletAdapter mAdapter;
    View mRooView;
    @BindView(R.id.tvToolBarTittle)
    TextViewGotham tvToolBarTittle;
    @BindView(R.id.profileToolBar)
    Toolbar profileToolBar;
    @BindView(R.id.ivScan)
    ImageView ivScan;
    @BindView(R.id.tvViewusedTicket)
    TextViewArialBold tvViewusedTicket;
    @BindView(R.id.tvContactGuest)
    TextViewArialBold tvContactGuest;
    @BindView(R.id.ll1)
    LinearLayout ll1;

    String strKidCount, mobile, orderId, userId, name, message, gid;
    String strAdultCount = "";
    String status = "online";
    int LOCATION_CODE = 100;
    int LOCATION_CODE2 = 101;

    BroadcastReceiver hitAPi = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mActualList.clear();
            mList.clear();
            setApiData();

        }
    };

    Timer timer;
    Boolean isRunningTimer = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_my_wallet_screen);
        ButterKnife.bind(this);
        details();
        if (UniversalOrlandoPreference.readString(mActivity, UniversalOrlandoPreference.MOBILE, "") != null) {
            mobile = UniversalOrlandoPreference.readString(mActivity, UniversalOrlandoPreference.MOBILE, "");
        }
        if (UniversalOrlandoPreference.readString(mActivity, UniversalOrlandoPreference.LOGGEDIN_ORDERID, "") != null) {
            orderId = UniversalOrlandoPreference.readString(mActivity, UniversalOrlandoPreference.LOGGEDIN_ORDERID, "");
        }

        if (UniversalOrlandoPreference.readString(mActivity, UniversalOrlandoPreference.GID, "") != null) {
            gid = UniversalOrlandoPreference.readString(mActivity, UniversalOrlandoPreference.GID, "");
        }

        userId = UniversalOrlandoPreference.readString(this, UniversalOrlandoPreference.LOGIN_ID, "");
        name = UniversalOrlandoPreference.readString(this, UniversalOrlandoPreference.USER_NAME, "");

//        SocketHandler.INSTANCE.initSocket();

//        startTrackingLocation();

        registerReceiver(hitAPi, new IntentFilter(getString(R.string.hitAPi)));
        setToolbar();
        executeApi();
        clickMainLayoutTouch();

    }


    @Override
    protected void onResume() {
        super.onResume();

        if (checkLocationPermission()) {
            if (!Utils.INSTANCE.isLocationServiceEnabled(this)) {
                showGPSAlert();
            }
            Intent intent = new Intent(MyWalletScreenActivity.this, LocationService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getApplicationContext().startForegroundService(intent);
            } else getApplicationContext().startService(intent);
        }

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                disablingScreen();
            }
        };
        if (!isRunningTimer) {
            timer = new Timer();
            timer.schedule(timerTask, 0, 3000);
            isRunningTimer = true;
            Log.d("TIMER START~~~~", "");
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (isRunningTimer) {
            timer.cancel();
            isRunningTimer = false;
            Log.d("TIMER STOP~~~~", "");
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void clickMainLayoutTouch() {
        rlMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if (isNetworkAvailable(mActivity))
//                    disablingScreen();
                return false;
            }
        });
    }

    private void executeApi() {
        if (isNetworkAvailable(mActivity)) {
            setApiData();
        } else {
            showToastAlert(mActivity, getString(R.string.checkInternet));
        }
    }

    public void setApiData() {
        showProgressDialog(mActivity);
        final String mobile = UniversalOrlandoPreference.readString(mActivity, UniversalOrlandoPreference.MOBILE, "");
        String orderIdParam = UniversalOrlandoPreference.readString(mActivity, UniversalOrlandoPreference.LOGGEDIN_ORDERID, "");

        RestClient.get().ticketsList(mobile, orderIdParam).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                dismissProgressDailog();
                parseResponse(response);
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                showToastAlert(mActivity, t.toString());
            }
        });
    }

    private void parseResponse(Response<Profile> response) {
        if (response.isSuccessful() && response.body() != null) {
            Log.e("MyWalletScreenActivity", "==Body==" + response.body().toString());
            if (Integer.valueOf(response.body().getStatus()) == 404) {
                showToastAlert(mActivity, response.body().getMessage());
                logOut(mActivity);
            } else if (Integer.valueOf(response.body().getStatus()) == 399) {
                showToastAlert(mActivity, response.body().getMessage());
                logOut(mActivity);
            } else {
                strAdultCount = response.body().getAdultCount();
                strKidCount = response.body().getKidCount();


                if (response.body().getOrderDetails().size() > 0) {

                    if (response.body().getOrderDetails().size() < 1) {

                    } else {
                        if (response.body().getOrderDetails().get(0).getIsdisabled().equals("1")) {
                            logOutAlert(mActivity);
                        } else {
                            mList.addAll(response.body().getOrderDetails());

                            for (int i = 0; i < mList.size(); i++) {
                                Order mOrder = mList.get(i);

                                if (!contains(mActualList, mOrder.getMobile(), mOrder.getType())) {
                                    mActualList.add(mOrder);
                                }

                                if (!containsKid(mActualList, mOrder.getMobile(), mOrder.getType())) {
                                    mActualList.add(mOrder);
                                }
                            }

                            setWidgetData();
                        }
                    }
                }
            }
        } else {
            showToastAlert(mActivity, response.message());
        }
    }

    boolean contains(ArrayList<Order> list, String mo, String type) {
        for (Order item : list) {
            if (item.getMobile().equals(mo) && item.getType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    boolean containsKid(ArrayList<Order> list, String mo, String type) {
        for (Order item : list) {
            if (item.getMobile().equals(mo) && item.getType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    private void setWidgetData() {
        mAdapter = new MyWalletAdapter(mActivity, mActualList, mList, strAdultCount, strKidCount);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(hitAPi);
    }

    @OnClick(R.id.rlMain)
    public void onClick() {
//        if (isNetworkAvailable(mActivity))
//            disablingScreen();
    }

    public void disablingScreen() {
        Log.d("TIMER~~~~MyWallet", "");

        if (!mobile.equals("") && !orderId.equals("")) {
            RestClient.get().loggingInstantinactiveTickets(mobile, orderId).enqueue(new Callback<Order>() {
                @Override
                public void onResponse(Call<Order> call, Response<Order> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getIsdisabled().equals("1")) {
                            if (mActivity != null && !mActivity.isFinishing()) {
                                showToastAlert(mActivity, getString(R.string.signedOut));
                                logOutAlert(mActivity);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<Order> call, Throwable t) {
                    Toast.makeText(MyWalletScreenActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void setToolbar() {
        profileToolBar.setNavigationIcon(R.drawable.baseline_keyboard_backspace_white_24);
        profileToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = "offline";
                logOut(MyWalletScreenActivity.this);
            }
        });
    }

    public void details() {
        String path = Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_PICTURES
                + File.separator + getString(R.string.screenShotKey) + File.separator;

        FileObserver fileObserver = new FileObserver(path, FileObserver.CREATE) {
            @Override
            public void onEvent(int event, String path) {
            }
        };

        fileObserver.startWatching();
    }

    public void logOut(Activity activity) {
        UniversalResortApplication.profile = null;
        SharedPreferences preferences = UniversalOrlandoPreference.getPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(UniversalOrlandoPreference.IS_LOGIN,false).apply();
        Toast.makeText(activity, getString(R.string.signedOut), Toast.LENGTH_LONG).show();
        Intent i = new Intent(activity, OrderIdLoginActivity.class);
//        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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


    private Boolean checkLocationPermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            showPermissionAlert();
            return false;
        } else return true;
    }

    private ActivityResultLauncher<String> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (!isGranted)
                    showPermissionAlert();
            });

    private ActivityResultLauncher<Intent> gpsLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (Utils.INSTANCE.isLocationServiceEnabled(this)) {
                    onResume();
                } else {
                    showGPSAlert();
                }

            });
    private void LocationPermission() {

        ActivityCompat.requestPermissions(MyWalletScreenActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location access granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Location access is required!", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == LOCATION_CODE2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(MainActivity.this, R.string.permission_granted, Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Location access granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Location access is required!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showPermissionAlert() {
        String txt = "Your ticket only works when you are close to the main entrance.\n\n" +
                "You need to allow location, in order to view your tickets";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission Mandatory!!");
        builder.setMessage(txt);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                LocationPermission();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    private void showGPSAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("GPS OFF Alert");
        builder.setCancelable(false);
        builder.setMessage("Turn GPS on for location updates");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                gpsLauncher.launch(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                showPermissionAlert();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
