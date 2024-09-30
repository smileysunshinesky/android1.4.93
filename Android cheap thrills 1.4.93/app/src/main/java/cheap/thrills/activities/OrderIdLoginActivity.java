package cheap.thrills.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.firebase.FirebaseApp;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cheap.thrills.R;
import cheap.thrills.UniversalResortApplication;
import cheap.thrills.fonts.TextViewArialRegular;
import cheap.thrills.location.LocationService;
import cheap.thrills.location.MyLocationListener;
import cheap.thrills.location.SocketHandler;
import cheap.thrills.location.SocketUtils;
import cheap.thrills.location.UserData;
import cheap.thrills.location.Utils;
import cheap.thrills.models.Order;
import cheap.thrills.models.Profile;
import cheap.thrills.retrofit.RestClient;
import cheap.thrills.utils.StoreManager;
import cheap.thrills.utils.UniversalOrlandoPreference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderIdLoginActivity extends BaseActivity implements LocationListener, SocketUtils.OSocket, MyLocationListener {
    Activity mActivity = OrderIdLoginActivity.this;
    String TAG = mActivity.getClass().getSimpleName();
    boolean onceLoggedIn = false;
    private List<Order> orderList;
    AlertDialog permissionDialog;
    AlertDialog gpsDialog;

    //@BindView(R.id.etOrder)
    EditText etorderId;
    //@BindView(R.id.etPassword)
    EditText etPassword;
    //@BindView(R.id.btnSignIn)
    Button btnSignIn;
    // @BindView(R.id.tvCreateAccount)
    TextViewArialRegular tvCreateAccount;

   /* @BindView(R.id.tvForgotPassword)
    TextViewArialBold tvForgotPassword;*/
    /*@BindView(R.id.cbCheckBox)
    AppCompatCheckBox cbCheckBox;*/

    private LocationManager locationManager;
    private double latitude = -1, longitude = -1;

    private UserData.Coordinate coordinates;
    String strKidCount, mobile, orderId, userId, name, message, gid;
    String strAdultCount = "";
    String status = "offline";

    Timer socketTimer;
    Boolean isRunningTimer = false;
    TimerTask timerTaskSocket = new TimerTask() {
        @Override
        public void run() {
//            if (latitude != -1 && longitude != -1 && onceLoggedIn) {
//                coordinates = new UserData.Coordinate(latitude, longitude);
//                message = Utils.INSTANCE.toJson(coordinates, userId, name, status, orderId, gid);
//                pushSocket(message);
//            }
        }
    };


    private void pushSocket(String message) {
        Log.d(TAG, "pushSocket: " + message);
        if (SocketHandler.INSTANCE.getSocket().connected()) {
            SocketHandler.INSTANCE.getSocket().emit("lastKnownLocation", message);
        } else {
            SocketHandler.INSTANCE.initSocket();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_id_login);
        /*ButterKnife.bind(this);*/

        FirebaseApp.initializeApp(mActivity);
        UniversalResortApplication.profile = null;
        SharedPreferences preferences = UniversalOrlandoPreference.getPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(UniversalOrlandoPreference.IS_LOGIN,false).apply();
        editor.putBoolean(UniversalOrlandoPreference.IS_First_Launch,true).apply();
        tvCreateAccount = findViewById(R.id.tvCreateAccount);
        etorderId = findViewById(R.id.etOrder);
        etPassword = findViewById(R.id.etPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        StoreManager storeManager = new StoreManager(getApplicationContext());
        etorderId.setText(storeManager.getKeyValue("orderId"));
        etPassword.setText(storeManager.getKeyValue("password"));

        etorderId.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(etorderId.getText().toString().length() >= 6){
                     etPassword.requestFocus();
                }else if(etorderId.getText().toString().length() >= 2){
                   etorderId.setInputType(InputType.TYPE_CLASS_NUMBER);
               } else{
                   etorderId.setInputType(InputType.TYPE_CLASS_TEXT);
               }
            }
        });

        SpannableString ss = new SpannableString("Don’t have an order number yet? Please click here to sign up and place your order.");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Toast.makeText(mActivity, "Clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 39, 50, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new UnderlineSpan(), 39, 50, 0);
        tvCreateAccount.setText(ss);
        tvCreateAccount.setMovementMethod(LinkMovementMethod.getInstance());
        tvCreateAccount.setHighlightColor(Color.TRANSPARENT);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInCheck();
            }
        });
        initData();

//        onceLoggedIn = UniversalOrlandoPreference.readBoolean(OrderIdLoginActivity.this, UniversalOrlandoPreference.ONCE_LOGGED_IN, false);
//        if (onceLoggedIn) {
//            initData();
//        }

    }

    void initData() {

        if (UniversalOrlandoPreference.readString(mActivity, UniversalOrlandoPreference.MOBILE, "") != null) {
            mobile = UniversalOrlandoPreference.readString(mActivity, UniversalOrlandoPreference.MOBILE, "");
        }
        if (UniversalOrlandoPreference.readString(mActivity, UniversalOrlandoPreference.LOGGEDIN_ORDERID, "") != null) {
            orderId = UniversalOrlandoPreference.readString(mActivity, UniversalOrlandoPreference.LOGGEDIN_ORDERID, "");
        }

        if (UniversalOrlandoPreference.readString(mActivity, UniversalOrlandoPreference.GID, "") != null) {
            gid = UniversalOrlandoPreference.readString(mActivity, UniversalOrlandoPreference.GID, "");
        }

        SocketUtils.getInstance().setSocketInterface(this);

        userId = UniversalOrlandoPreference.readString(this, UniversalOrlandoPreference.LOGIN_ID, "");
        name = UniversalOrlandoPreference.readString(this, UniversalOrlandoPreference.USER_NAME, "");

    }


    /*@OnClick({R.id.btnSignIn, R.id.tvCreateAccount, R.id.tvForgotPassword})
    public void onClick(View mView) {
        switch (mView.getId()) {
            case R.id.btnSignIn:
                signInCheck();
                break;
            case R.id.tvCreateAccount:
                break;
            case R.id.tvForgotPassword:
                break;
        }
    }*/

    public void signInCheck() {
        hideKeyboard(mActivity);
        if (etorderId.getText().toString().equals("")) {
            showToastAlert(mActivity, getString(R.string.order));
        } else if (etPassword.getText().toString().equals("")) {
            showToastAlert(mActivity, getString(R.string.enterPassword));
        }
//        else if (etPassword.getText().toString().trim().length() < 10) {
//            showToastAlert(mActivity, getString(R.string.validMobile));
//        }
        else {
            if (isNetworkAvailable(mActivity)) {
                executeLoginAPI();
            } else {
                showToastAlert(mActivity, getString(R.string.checkInternet));
            }
        }

    }

    public void executeLoginAPI() {
        showProgressDialog(mActivity);

        RestClient.get().ticketsList(etPassword.getText().toString().trim(), etorderId.getText().toString().trim()).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                Log.e("TAG", response.body().toString());
                orderList = response.body().getOrderDetails();
                dismissProgressDailog();
                parseResponse(response);
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                dismissProgressDailog();
                showToastAlert(mActivity, t.toString());
            }
        });

//        RestClient.get().mobilelogin(etPassword.getText().toString().trim(), etorderId.getText().toString().trim()).enqueue(new Callback<Profile>() {
//            @Override
//            public void onResponse(Call<Profile> call, Response<Profile> response) {
//                dismissProgressDailog();
//                parseResponse(response);
//            }
//
//            @Override
//            public void onFailure(Call<Profile> call, Throwable t) {
//                dismissProgressDailog();
//                showToastAlert(mActivity, getString(R.string.pleasecorrectcredentials));
//                etorderId.setText("");
//                etPassword.setText("");
//            }
//        });
    }

    private void parseResponse(Response<Profile> response) {

        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences = this.getSharedPreferences("dfSP", 0);
        editor = sharedPreferences.edit();

        Log.e(TAG, "==Response==" + response.body());
        Log.e(TAG, "==ErrorBody==" + response.errorBody());
        dismissProgressDailog();
        if (response.isSuccessful()) {
            if (Integer.valueOf(response.body().getStatus()) == 404) {
                alertErrorDialog(mActivity,mActivity.getResources().getString(R.string.text_login_error));
            } else if (Integer.valueOf(response.body().getStatus()) == 399) {
                alertErrorDialog(mActivity,mActivity.getResources().getString(R.string.text_login_error));
            } else if (Integer.valueOf(response.body().getStatus()) == 401) {
                if(response.body().getMessage().equalsIgnoreCase("Mobile number or order id is wrong") || response.body().getMessage().equalsIgnoreCase("Incorrect Order ID and/or Mobile Number."))
                alertErrorDialog(mActivity, response.body().getMessage());
                else
                    alertErrorDialog(mActivity,mActivity.getResources().getString(R.string.text_login_error));
            } else {
                StoreManager storeManager = new StoreManager(getApplicationContext());
                storeManager.setKeyValue("orderId",etorderId.getText().toString());
                storeManager.setKeyValue("password",etPassword.getText().toString());
                if (SocketUtils.getInstance().getSocketInterface() != null && SocketUtils.getInstance().getSocketInterface().Timer() != null) {
                    Log.d(TAG, "parseResponse: timer has been canceled...");
                    SocketUtils.getInstance().getSocketInterface().Timer().cancel();
                }
                if (response.body().getThemeParkParenId().equals("2")) {
                    UniversalOrlandoPreference.writeListInPref(mActivity, orderList);
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.THEMEPARK_ID, response.body().getThemeParkParenId());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.GID, response.body().getOrderDetails().get(0).getGid());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.TYPE, response.body().getOrderDetails().get(0).getType());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.MOBILE, etPassword.getText().toString().trim());
                    UniversalOrlandoPreference.writeBoolean(mActivity, UniversalOrlandoPreference.IS_LOGIN, true);
                    UniversalOrlandoPreference.writeBoolean(mActivity, UniversalOrlandoPreference.ONCE_LOGGED_IN, true);
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.LOGGEDIN_ORDERID, etorderId.getText().toString().trim());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.USER_NAME, response.body().getOrderDetails().get(0).getName());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.SET_LINK, response.body().getOrderDetails().get(0).getSet_link());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.TICKET_TYPE, response.body().getOrderDetails().get(0).getTicket_type());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.CUSTOMER_ID, response.body().getOrderDetails().get(0).getCustomer_id());
                    Log.d("testttting 2", "order id " + response.body().getOrderDetails().get(0).getOrderId());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.ORDER_ID, response.body().getOrderDetails().get(0).getOrderId());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.LOGIN_ID, etorderId.getText().toString().trim());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.THEMEPARK_NAME, response.body().getThemeParkName());
                    showToastAlert(OrderIdLoginActivity.this, response.body().getMessage());
                    editor.putBoolean("LoggedIn", true).apply();
                    startActivity(new Intent(mActivity, TicketTempletActivity.class));
                    finish();
                } else if (response.body().getThemeParkParenId().equals("4")) {
                    UniversalOrlandoPreference.writeListInPref(mActivity, orderList);
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.THEMEPARK_ID, response.body().getThemeParkParenId());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.GID, response.body().getOrderDetails().get(0).getGid());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.TYPE, response.body().getOrderDetails().get(0).getType());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.MOBILE, etPassword.getText().toString().trim());
                    UniversalOrlandoPreference.writeBoolean(mActivity, UniversalOrlandoPreference.IS_LOGIN, true);
                    UniversalOrlandoPreference.writeBoolean(mActivity, UniversalOrlandoPreference.ONCE_LOGGED_IN, true);
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.LOGGEDIN_ORDERID, etorderId.getText().toString().trim());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.USER_NAME, response.body().getOrderDetails().get(0).getName());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.SET_LINK, response.body().getOrderDetails().get(0).getSet_link());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.TICKET_TYPE, response.body().getOrderDetails().get(0).getTicket_type());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.CUSTOMER_ID, response.body().getOrderDetails().get(0).getCustomer_id());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.ORDER_ID, response.body().getOrderDetails().get(0).getOrderId());
                    Log.d("testttting 4", "order id " + response.body().getOrderDetails().get(0).getOrderId());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.LOGIN_ID, etorderId.getText().toString().trim());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.THEMEPARK_NAME, response.body().getThemeParkName());
                    showToastAlert(OrderIdLoginActivity.this, response.body().getMessage());
                    editor.putBoolean("LoggedIn", true).apply();
                    startActivity(new Intent(mActivity, TicketTempletActivity2.class));
                    finish();
                } else if (response.body().getThemeParkParenId().equals("3")) {
                    UniversalOrlandoPreference.writeListInPref(mActivity, orderList);
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.THEMEPARK_ID, response.body().getThemeParkParenId());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.GID, response.body().getOrderDetails().get(0).getGid());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.TYPE, response.body().getOrderDetails().get(0).getType());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.MOBILE, etPassword.getText().toString().trim());
                    UniversalOrlandoPreference.writeBoolean(mActivity, UniversalOrlandoPreference.IS_LOGIN, true);
                    UniversalOrlandoPreference.writeBoolean(mActivity, UniversalOrlandoPreference.ONCE_LOGGED_IN, true);
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.LOGGEDIN_ORDERID, etorderId.getText().toString().trim());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.USER_NAME, response.body().getOrderDetails().get(0).getName());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.SET_LINK, response.body().getOrderDetails().get(0).getSet_link());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.TICKET_TYPE, response.body().getOrderDetails().get(0).getTicket_type());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.CUSTOMER_ID, response.body().getOrderDetails().get(0).getCustomer_id());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.ORDER_ID, response.body().getOrderDetails().get(0).getOrderId());
                    Log.d("testttting 3", "order id " + response.body().getOrderDetails().get(0).getOrderId());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.THEMEPARK_NAME, response.body().getThemeParkName());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.LOGIN_ID, etorderId.getText().toString().trim());
                    showToastAlert(OrderIdLoginActivity.this, response.body().getMessage());
                    editor.putBoolean("LoggedIn", true).apply();
                    startActivity(new Intent(mActivity, TicketTempletActivity3.class));
                    finish();
                } else {
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.THEMEPARK_ID, response.body().getThemeParkParenId());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.GID, response.body().getOrderDetails().get(0).getGid());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.TYPE, response.body().getOrderDetails().get(0).getType());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.MOBILE, etPassword.getText().toString().trim());
                    UniversalOrlandoPreference.writeBoolean(mActivity, UniversalOrlandoPreference.IS_LOGIN, true);
                    UniversalOrlandoPreference.writeBoolean(mActivity, UniversalOrlandoPreference.ONCE_LOGGED_IN, true);
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.LOGGEDIN_ORDERID, etorderId.getText().toString().trim());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.USER_NAME, response.body().getOrderDetails().get(0).getName());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.SET_LINK, response.body().getOrderDetails().get(0).getSet_link());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.TICKET_TYPE, response.body().getOrderDetails().get(0).getTicket_type());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.CUSTOMER_ID, response.body().getOrderDetails().get(0).getCustomer_id());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.ORDER_ID, response.body().getOrderDetails().get(0).getOrderId());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.LOGIN_ID, etorderId.getText().toString().trim());
                    showToastAlert(OrderIdLoginActivity.this, response.body().getMessage());
                    editor.putBoolean("LoggedIn", true).apply();
                    startActivity(new Intent(mActivity, MyWalletScreenActivity.class));
                    finish();
                }
            }
        } else {
            showToastAlert(mActivity, getString(R.string.pleasecorrectcredentials));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (checkLocationPermission()) {
            startLocationService();
        }

        Log.d("testttting", "orderid login ");

//        initData();
//        if (socketTimer == null) {
//            socketTimer = new Timer();
//            socketTimer.schedule(timerTaskSocket, 0, 5000);
//        }
//


    }

    private void startLocationService() {
        Intent intent = new Intent(OrderIdLoginActivity.this, LocationService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getApplicationContext().startForegroundService(intent);
        } else getApplicationContext().startService(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }





    @Override
    public void onLocationChanged(@NonNull Location location) {

        Log.d("testtt/", location.getLatitude() + "");
        latitude = location.getLatitude();
        longitude = location.getLongitude();

    }

    @Override
    public Timer Timer() {
        return socketTimer;
    }


    private Boolean checkLocationPermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                permissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
//            } else
//                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            showPermissionAlert();
            return false;
        } else return true;

    }

    private ActivityResultLauncher<String> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (!isGranted) {
                    permissionDialog.dismiss();
                    showPermissionAlert();
                } else {
                    permissionDialog.dismiss();
                    if (!Utils.INSTANCE.isLocationServiceEnabled(this)) {
                        showGPSAlert();
                    } else {
                        startLocationService();
                    }
                }

            });

    private ActivityResultLauncher<Intent> gpsLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (Utils.INSTANCE.isLocationServiceEnabled(this)) {
                    startLocationService();
                } else {
                    showGPSAlert();
                }
            });


    private void showPermissionAlert() {
        String txt = "Your ticket only works when you are close to the main entrance.\n\n" +
                "You need to allow location ALWAYS, in order to view your tickets";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission Mandatory!!");
        builder.setMessage(txt);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                permissionDialog.hide();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    permissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
//                } else
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            }
        });
        permissionDialog = builder.create();
        permissionDialog.show();

    }

    private void showGPSAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("GPS OFF Alert");
        builder.setCancelable(false);
        builder.setMessage("Turn GPS on for location updates");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                gpsDialog.dismiss();
                gpsLauncher.launch(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                gpsDialog.dismiss();
                showGPSAlert();
            }
        });

        gpsDialog = builder.create();
        gpsDialog.show();

    }
}
