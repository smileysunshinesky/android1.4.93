package cheap.thrills.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.navigation.NavigationView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cheap.thrills.R;
import cheap.thrills.UniversalResortApplication;
import cheap.thrills.fonts.TextViewArialBold;
import cheap.thrills.fonts.TextViewGotham;
import cheap.thrills.fragments.HomeFragment;
import cheap.thrills.fragments.MyProfileFragment;
import cheap.thrills.models.DyanmicIconText;
import cheap.thrills.models.IconData;
import cheap.thrills.models.Order;
import cheap.thrills.models.Profile;
import cheap.thrills.retrofit.RestClient;
import cheap.thrills.utils.Constants;
import cheap.thrills.utils.UniversalOrlandoPreference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,
        ResultCallback<LocationSettingsResult> {
    /************************
     *Fused Google Location
     **************/
    public static final int REQUEST_PERMISSION_CODE = 919;
    public static String sideTittle = "";
    public static int homeclicked = 0;
    public long mLatitude;
    public long mLongitude;
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected LocationSettingsRequest mLocationSettingsRequest;
    protected Location mCurrentLocation;
    protected Boolean mRequestingLocationUpdates;
    protected String mLastUpdateTime;
    protected String mAccessFineLocation = Manifest.permission.ACCESS_FINE_LOCATION;
    protected String mAccessCourseLocation = Manifest.permission.ACCESS_COARSE_LOCATION;

    String TAG = DrawerActivity.this.getClass().getSimpleName();
    Activity mActivity = DrawerActivity.this;
    String mobile = "", orderId = "";
    Intent i;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvVirtual)
    TextView tvVirtual;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivVirtualLines)
    ImageView ivVirtualLines;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvHorrorNights)
    TextView tvHorrorNights;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivHalloweeen)
    ImageView ivHalloween;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvToolBarTittle)
    TextViewGotham tvToolBarTittle;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvSignIn)
    TextView tvSignIn;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvPrivacyAndLegal)
    TextView tvPrivacyAndLegal;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvLostAndFound)
    TextView tvLostAndFound;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivThingsToDo)
    ImageView ivThingsToDo;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivBuyDownUp)
    ImageView ivBuyDownUp;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llRides)
    LinearLayout llRides;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llShows)
    LinearLayout llShows;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llOnsiteHotels)
    LinearLayout llOnsiteHotels;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llSignOut1)
    LinearLayout llSignOut;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llThemePark)
    LinearLayout llThemePark;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llBuy)
    LinearLayout llBuy;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llHelp)
    LinearLayout llHelp;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llPrivacy)
    LinearLayout llPrivacy;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llEvents)
    LinearLayout llEvents;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.search)
    ImageView ivSearch;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llAmebitiesOptions)
    LinearLayout llAmeletiesOptions;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivAmenitiesDownUp)
    ImageView ivAmenitiesDownUp;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivGuestServiceDownUp)
    ImageView ivGuestServiceDownUp;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llDining)
    LinearLayout llDining;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llShopping)
    LinearLayout llShopping;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tryLinearSlide)
    LinearLayout tryLinearSlide;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llAmexSpecialOffers)
    LinearLayout llAMEXSpecial;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llAmebities)
    LinearLayout llAmebities;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llRestRoom)
    LinearLayout llRestRooms;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llLockers)
    LinearLayout llLockers;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llRentalServices)
    LinearLayout llRental;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivAlert)
    ImageView ivAlert;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.nav_view)
    NavigationView nav_view;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llSettings)
    LinearLayout llSettings;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llExtras)
    LinearLayout llExtras;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llExpress)
    LinearLayout llExpressPass;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llGuestService)
    LinearLayout llGuestService;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llGuestServiceLocations)
    LinearLayout llGuestServiceLocations;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llATMS)
    LinearLayout llATMS;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llLostFound)
    LinearLayout llLostFound;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llRestAreas)
    LinearLayout llServiceAnimal;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llFirstAid)
    LinearLayout llFirstAid;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llContactGuest)
    LinearLayout llContacts;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llSmokingPermitted)
    LinearLayout llSmoking;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llParkingAmenities)
    LinearLayout llParkingReminder;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llUniversalcityWalk)
    LinearLayout llUniversalCityWalk;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llIslandOfAdventure)
    LinearLayout llIslandsOfAdventure;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llUniversalStudios)
    LinearLayout llUniversalStudios;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llUniversalVolcanos)
    LinearLayout llUniversalVolcano;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llThingsToDoOptions)
    LinearLayout llThingsToDoOption;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llthingsToDo)
    LinearLayout llthingsToDo;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llTickets)
    LinearLayout llTickets;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llSignIn)
    LinearLayout llSignIn;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llGuestServicesOption)
    LinearLayout llGuestServicesOption;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llThemeParksOptions)
    LinearLayout llThemeParksOptions;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llHome)
    LinearLayout llHome;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llWallet)
    LinearLayout llWallet;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llfav)
    LinearLayout llFav;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llBuyOptions)
    LinearLayout llBuyOptions;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llParkNotification)
    LinearLayout llPark;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llparkdirections)
    LinearLayout llDirections;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llBlockOutDates)
    LinearLayout llDates;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llHalloween)
    LinearLayout llHalloween;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llvirtualLine)
    LinearLayout llvirtualLine;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llSpecialOffers)
    LinearLayout llSpecial;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llOrders)
    LinearLayout llOrder;
    ActionBarDrawerToggle toggle;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivBuyDownUpThemePark)
    ImageView ivBuyDownUpThemePark;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ivGuestService)
    ImageView ivGuestService;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvSignOut)
    TextViewArialBold tvSignOut;
    String strUserName = "";

    List<IconData> mIconArrayList = new ArrayList<IconData>();

    BroadcastReceiver alert = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            drawer.closeDrawer(GravityCompat.START);
            addFragmentToView(new HomeFragment(), false);
        }
    };

    BroadcastReceiver changeToolBarTittle = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            String t = intent.getStringExtra("tittle");
            if (t == null) {
                ivAlert.setVisibility(View.VISIBLE);
                ivSearch.setVisibility(View.VISIBLE);
                tvToolBarTittle.setText("ajkdjaks");
            } else if (t.equals(getString(R.string.titleHome))) {
                tvToolBarTittle.setText(t);
                ivAlert.setVisibility(View.GONE);
                ivSearch.setVisibility(View.GONE);

            } else if (t.equals(getString(R.string.Profile))) {
                tvToolBarTittle.setText(t);
                ivAlert.setVisibility(View.GONE);
                ivSearch.setVisibility(View.GONE);
            } else if (t.equals(getString(R.string.myWallet))) {
                tvToolBarTittle.setText(t);
                ivAlert.setVisibility(View.GONE);
                ivSearch.setVisibility(View.GONE);
            }
        }
    };


    Timer timer = new Timer();
    Boolean isRunningTimer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String a = UniversalOrlandoPreference.readString(mActivity, UniversalOrlandoPreference.MOBILE, "");


        if (UniversalOrlandoPreference.readString(mActivity, UniversalOrlandoPreference.MOBILE, "") != null) {
            mobile = UniversalOrlandoPreference.readString(mActivity, UniversalOrlandoPreference.MOBILE, "");
        }
        if (UniversalOrlandoPreference.readString(mActivity, UniversalOrlandoPreference.LOGGEDIN_ORDERID, "") != null) {
            orderId = UniversalOrlandoPreference.readString(mActivity, UniversalOrlandoPreference.LOGGEDIN_ORDERID, "");
        }

        /*************
         *Google Location
         **************/
        if (checkPermission()) {
            mRequestingLocationUpdates = false;
            mLastUpdateTime = "";
            /*Update values using data stored in the Bundle.*/
            updateValuesFromBundle(savedInstanceState);
            buildGoogleApiClient();
            createLocationRequest();
            buildLocationSettingsRequest();
            checkLocationSettings();
            updateValuesFromBundle(savedInstanceState);
            /*******************************************/
        } else {
            requestPermission();
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_drawer2);
        ButterKnife.bind(this);
        setUpToolbar();
        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";
    }

    @SuppressLint("SetTextI18n")
    private void setUpToolbar() {

        registerReceiver(alert, new IntentFilter(getString(R.string.alert)));
        registerReceiver(changeToolBarTittle, new IntentFilter(getString(R.string.changeToolBarTittle)));
        if (UniversalOrlandoPreference.readString(DrawerActivity.this, UniversalOrlandoPreference.USER_NAME, "") != null) {
            strUserName = UniversalOrlandoPreference.readString(DrawerActivity.this, UniversalOrlandoPreference.USER_NAME, "");
        }
        /*Redirect Home Fragment*/
        addFragmentToView(new HomeFragment(), false);
        viewsIDwithDrawable();
        if (strUserName != null) {
            strUserName = testSplit(strUserName);
            tvSignIn.setText(getString(R.string.hiText) + strUserName);
            llSignOut.setVisibility(View.VISIBLE);
        } else {
            tvSignIn.setText(getString(R.string.SignIn));
            llSignOut.setVisibility(View.GONE);
        }
    }

    @SuppressLint("RtlHardcoded")
    private void viewsIDwithDrawable() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        toggle.syncState();
        toolbar.setNavigationOnClickListener(v -> {
            if (drawer.isDrawerOpen(Gravity.LEFT)) {


                drawer.closeDrawer(Gravity.LEFT);
                if (homeclicked == 1)
                    tvToolBarTittle.setText(getString(R.string.titleHome));
                else {
                    tvToolBarTittle.setText(getString(R.string.Profile));
                }
                ivAlert.setVisibility(View.GONE);
                ivSearch.setVisibility(View.GONE);
            } else {
                drawer.openDrawer(Gravity.LEFT);
                tvToolBarTittle.setText(getString(R.string.titleOrlando));
                ivAlert.setVisibility(View.VISIBLE);
                ivSearch.setVisibility(View.VISIBLE);
            }
        });
        nav_view.setNavigationItemSelectedListener(this);
        if (tvToolBarTittle.getText().equals(getString(R.string.titleHome))) {
            ivAlert.setVisibility(View.GONE);
            ivSearch.setVisibility(View.GONE);

        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                super.onBackPressed();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void addFragmentToView(Fragment fragment, boolean addToStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (fragment != null) {
            // Replace current fragment by this new one
            ft.replace(R.id.containerLL, fragment);
            if (addToStack)
                ft.addToBackStack(null);
            ft.commitAllowingStateLoss();
        }
    }


    @SuppressLint("RtlHardcoded")
    private void drawerOpenClose(String strTitle) {
        if (drawer.isDrawerOpen(Gravity.LEFT)) {
            drawer.closeDrawer(Gravity.LEFT);
            tvToolBarTittle.setText(strTitle);
            ivAlert.setVisibility(View.GONE);
            ivSearch.setVisibility(View.GONE);
        } else {
            drawer.openDrawer(Gravity.LEFT);
            tvToolBarTittle.setText(getString(R.string.titleOrlando));
            ivAlert.setVisibility(View.VISIBLE);
            ivSearch.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(alert);
        unregisterReceiver(changeToolBarTittle);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.ivAlert, R.id.llSignOut1, R.id.llSignIn, R.id.search, R.id.llIslandOfAdventure,
            R.id.llUniversalStudios, R.id.llUniversalVolcanos, R.id.llAmexSpecialOffers, R.id.llPrivacy,
            R.id.llSettings, R.id.llHelp, R.id.llLockers, R.id.llGuestService, R.id.llRentalServices,
            R.id.llThemePark, R.id.llAmebities, R.id.llRestRoom, R.id.llthingsToDo, R.id.llBuy, R.id.llExpress, R.id.llOrders, R.id.llExtras, R.id.llTickets, R.id.llContactGuest, R.id.llFirstAid,
            R.id.llGuestServiceLocations, R.id.llATMS, R.id.llLostFound,
            R.id.llRestAreas, R.id.llSmokingPermitted, R.id.llSpecialOffers,
            R.id.llHalloween, R.id.llvirtualLine, R.id.llUniversalcityWalk, R.id.llOnsiteHotels,
            R.id.llparkdirections, R.id.llBlockOutDates, R.id.llHome, R.id.llWallet, R.id.llfav, R.id.llParkNotification})

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llSignOut1:
                llSignOut.setVisibility(View.GONE);
                logOut(DrawerActivity.this);
                break;
            case R.id.ivAlert:
                i = new Intent(DrawerActivity.this, AlertActivity.class);
                startActivity(i);
                break;
            case R.id.search:
                i = new Intent(DrawerActivity.this, SearchActivity.class);
                startActivity(i);
                break;
            case R.id.llAmexSpecialOffers:
            case R.id.llthingsToDo:

            case R.id.llSpecialOffers:

            case R.id.llparkdirections:
            case R.id.llContactGuest:

            case R.id.llGuestServiceLocations:

            case R.id.llATMS:
            case R.id.llLostFound:
            case R.id.llSmokingPermitted:

            case R.id.llRestAreas:
            case R.id.llFirstAid:
            case R.id.llfav:
            case R.id.llParkNotification:

            case R.id.llBlockOutDates:
            case R.id.llHalloween:
            case R.id.llvirtualLine:
            case R.id.llTickets:
            case R.id.llExpress:
            case R.id.llExtras:
            case R.id.llOrders:
            case R.id.llUniversalcityWalk:
            case R.id.llHelp:
            case R.id.llOnsiteHotels:
            case R.id.llPrivacy:
            case R.id.llSettings:
            case R.id.llIslandOfAdventure:
            case R.id.llUniversalStudios:
            case R.id.llUniversalVolcanos:
            case R.id.llRestRoom:

            case R.id.llLockers:
            case R.id.llRentalServices:
                drawer.closeDrawer(GravityCompat.START);
                alertErrorDialog(DrawerActivity.this);
                if (DrawerActivity.homeclicked == 1) {
                    drawerOpenClose(getString(R.string.titleHome));
                } else {
                    drawerOpenClose(sideTittle);
                }
                break;
            case R.id.llGuestService:
                if (llGuestServicesOption.getVisibility() == View.VISIBLE) {
                    ivGuestServiceDownUp.setImageDrawable(getResources().getDrawable(R.drawable.baseline_keyboard_arrow_down_black_36));
                    llGuestServicesOption.setVisibility(View.GONE);
                } else {
                    ivGuestServiceDownUp.setImageDrawable(getResources().getDrawable(R.drawable.baseline_keyboard_arrow_up_black_36));
                    llGuestServicesOption.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.llBuy:
                if (llBuyOptions.getVisibility() == View.VISIBLE) {
                    ivBuyDownUp.setImageDrawable(getResources().getDrawable(R.drawable.baseline_keyboard_arrow_down_black_36));
                    llBuyOptions.setVisibility(View.GONE);
                } else {
                    ivBuyDownUp.setImageDrawable(getResources().getDrawable(R.drawable.baseline_keyboard_arrow_up_black_36));
                    llBuyOptions.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.llSignIn:
                drawer.closeDrawer(GravityCompat.START);
                if (!strUserName.equals("")) {
                    sideTittle = getString(R.string.Profile);
                    drawerOpenClose(getString(R.string.Profile));
                    addFragmentToView(new MyProfileFragment(), true);
                } else {
                    tvSignIn.setText(getString(R.string.SignIn));
                    i = new Intent(DrawerActivity.this, SignInActivity.class);
                    startActivity(i);
                }
                break;

            case R.id.llHome:
                sideTittle = getString(R.string.titleHome);
                drawerOpenClose(getString(R.string.titleHome));
                if (DrawerActivity.homeclicked == 0)
                    addFragmentToView(new HomeFragment(), false);
                break;
            case R.id.llWallet:
                startActivity(new Intent(mActivity, MyWalletScreenActivity.class));
                break;
            case R.id.llAmebities:
                if (llAmeletiesOptions.getVisibility() == View.VISIBLE) {
                    ivAmenitiesDownUp.setImageDrawable(getResources().getDrawable(R.drawable.baseline_keyboard_arrow_down_black_36));
                    llAmeletiesOptions.setVisibility(View.GONE);
                } else {
                    ivAmenitiesDownUp.setImageDrawable(getResources().getDrawable(R.drawable.baseline_keyboard_arrow_up_black_36));
                    llAmeletiesOptions.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.llThemePark:
                if (llThemeParksOptions.getVisibility() == View.VISIBLE) {
                    ivBuyDownUpThemePark.setImageDrawable(getResources().getDrawable(R.drawable.baseline_keyboard_arrow_down_black_36));
                    llThemeParksOptions.setVisibility(View.GONE);
                } else {
                    ivBuyDownUpThemePark.setImageDrawable(getResources().getDrawable(R.drawable.baseline_keyboard_arrow_up_black_36));
                    llThemeParksOptions.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    public void alertErrorDialog(Activity mActivity) {
        final Dialog alertDialog = new Dialog(mActivity);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.dialog_reminder);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // set the custom dialog components - text, image and button
        TextView tvOk = (TextView) alertDialog.findViewById(R.id.tvOk);
        tvOk.setOnClickListener(v -> alertDialog.dismiss());
        alertDialog.show();
    }

    public void logOut(Activity activity) {
        UniversalResortApplication.profile = null;
        SharedPreferences preferences = UniversalOrlandoPreference.getPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(UniversalOrlandoPreference.IS_LOGIN,false);
        editor.clear();
        editor.apply();
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


    /*********
     * Support for Marshmallows Version
     * GRANT PERMISSION FOR TAKEING IMAGE
     * 1) ACCESS_FINE_LOCATION
     * 2) ACCESS_COARSE_LOCATION
     **********/
    public boolean checkPermission() {
        int mlocationFineP = ContextCompat.checkSelfPermission(mActivity, mAccessFineLocation);
        int mlocationCourseP = ContextCompat.checkSelfPermission(mActivity, mAccessCourseLocation);
        return mlocationFineP == PackageManager.PERMISSION_GRANTED && mlocationCourseP == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{mAccessFineLocation, mAccessCourseLocation}, REQUEST_PERMISSION_CODE);
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {// If request is cancelled, the result arrays are empty.
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                checkLocationSettings();
            }  // permission denied, boo! Disable the
            // functionality that depends on this permission.

        }
    }


    /******************
     *Google Location
     ************/
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.keySet().contains(Constants.KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        Constants.KEY_REQUESTING_LOCATION_UPDATES);
            }
            if (savedInstanceState.keySet().contains(Constants.KEY_LOCATION)) {
                mCurrentLocation = savedInstanceState.getParcelable(Constants.KEY_LOCATION);
            }
            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(Constants.KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(Constants.KEY_LAST_UPDATED_TIME_STRING);
            }
        }
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Uses a {@link LocationSettingsRequest.Builder} to build
     * a {@link LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest).setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Check if the device's location settings are adequate for the app's needs using the
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} method, with the results provided through a {@code PendingResult}.
     */
    public void checkLocationSettings() {
        if (mGoogleApiClient != null) {
            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(
                            mGoogleApiClient,
                            mLocationSettingsRequest
                    );
            result.setResultCallback(this);
        }
    }

    /**
     * The callback invoked when
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} is called. Examines the
     * {@link LocationSettingsResult} object and determines if
     * location settings are adequate. If they are not, begins the process of presenting a location
     * settings dialog to the user.
     */
    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                if (mCurrentLocation != null) {
                    savingLocationLatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                }


                startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(mActivity, Constants.REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException ignored) {

                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                break;
            default:
                // finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    if (mCurrentLocation != null) {
                        savingLocationLatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                    }

                    startLocationUpdates();
                    break;
                case Activity.RESULT_CANCELED:
                    break;
                default:
            }
        }
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient,
                    mLocationRequest,
                    this
            ).setResultCallback(status -> mRequestingLocationUpdates = true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient,
                this
        ).setResultCallback(status -> mRequestingLocationUpdates = false);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    public void hitIconApi() {
        RestClient.get().geticonImage().enqueue(new Callback<DyanmicIconText>() {
            @Override
            public void onResponse(Call<DyanmicIconText> call, Response<DyanmicIconText> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mIconArrayList.clear();
                    if (Integer.parseInt(response.body().getStatus()) == 1) {
                        mIconArrayList = response.body().getData();
                        Log.e(TAG,"==Zero List Size==");
                        parseResponse(mIconArrayList);
                    }else{
                        tvHorrorNights.setText("");
                        llHalloween.setVisibility(View.GONE);
                        llvirtualLine.setVisibility(View.GONE);
                        ivHalloween.setImageResource(android.R.color.transparent);
                        tvVirtual.setText("");
                        ivVirtualLines.setImageResource(android.R.color.transparent);
                    }
                }
            }

            @Override
            public void onFailure(Call<DyanmicIconText> call, Throwable t) {
                    Log.e(TAG,"===ERROR==="+t.toString());
                tvHorrorNights.setText("");
                llHalloween.setVisibility(View.GONE);
                llvirtualLine.setVisibility(View.GONE);
                ivHalloween.setImageResource(android.R.color.transparent);
                tvVirtual.setText("");
                ivVirtualLines.setImageResource(android.R.color.transparent);
            }
        });
    }

    private void parseResponse(List<IconData> mIconArrayList) {
        tvHorrorNights.setText("");
        llHalloween.setVisibility(View.GONE);
        llvirtualLine.setVisibility(View.GONE);
        ivHalloween.setImageResource(android.R.color.transparent);
        tvVirtual.setText("");
        ivVirtualLines.setImageResource(android.R.color.transparent);

        if (mIconArrayList.size() == 1){
            for (int i = 0; i < mIconArrayList.size(); i++) {
                if (i == 0) {
                    if (mIconArrayList.get(i).getTitle() != null && mIconArrayList.get(i).getTitle().length() > 0) {
                        tvHorrorNights.setText(mIconArrayList.get(i).getTitle());
                        llHalloween.setVisibility(View.VISIBLE);
                    }
                    if (mIconArrayList.get(i).getIcon() != null && mIconArrayList.get(i).getIcon().contains("http")) {
                        Glide.with(getApplicationContext())
                                .load(mIconArrayList.get(i).getIcon())
                                .apply(RequestOptions.placeholderOf(R.drawable.icon_dates).error(R.drawable.icon_dates))
                                .into(ivHalloween);
                    }
                }
            }
        } else if (mIconArrayList.size() == 2){
            for (int i = 0; i < mIconArrayList.size(); i++){
                if (i == 0){
                    if (mIconArrayList.get(i).getTitle() != null && mIconArrayList.get(i).getTitle().length() > 0){
                        tvHorrorNights.setText(mIconArrayList.get(i).getTitle());
                        llHalloween.setVisibility(View.VISIBLE);
                    }
                    if (mIconArrayList.get(i).getIcon() != null && mIconArrayList.get(i).getIcon().contains("http")){
                        Glide.with(getApplicationContext())
                                .load(mIconArrayList.get(i).getIcon())
                                .apply(RequestOptions.placeholderOf(R.drawable.icon_dates).error(R.drawable.icon_dates))
                                .into(ivHalloween);
                    }
                }else if (i == 1){
                    if (mIconArrayList.get(i).getTitle() != null && mIconArrayList.get(i).getTitle().length() > 0){
                        tvVirtual.setText(mIconArrayList.get(i).getTitle());
                        llvirtualLine.setVisibility(View.VISIBLE);
                    }

                    if (mIconArrayList.get(i).getIcon() != null && mIconArrayList.get(i).getIcon().contains("http")){
                        Glide.with(getApplicationContext())
                                .load(mIconArrayList.get(i).getIcon())
                                .apply(RequestOptions.placeholderOf(R.drawable.icon_dates).error(R.drawable.icon_dates))
                                .into(ivVirtualLines);
                    }
                }
            }
        }else if (mIconArrayList.size() == 0){
            tvHorrorNights.setText("");
            llHalloween.setVisibility(View.GONE);
            llvirtualLine.setVisibility(View.GONE);
            ivHalloween.setImageResource(android.R.color.transparent);
            tvVirtual.setText("");
            ivVirtualLines.setImageResource(android.R.color.transparent);
        }else {
            mIconArrayList.size();
            for (int i = 0; i < mIconArrayList.size(); i++){
                if (i == 0){
                    if (mIconArrayList.get(i).getTitle() != null && mIconArrayList.get(i).getTitle().length() > 0){
                        tvHorrorNights.setText(mIconArrayList.get(i).getTitle());
                        llHalloween.setVisibility(View.VISIBLE);
                    }
                    if (mIconArrayList.get(i).getIcon() != null && mIconArrayList.get(i).getIcon().contains("http")){
                        Glide.with(getApplicationContext())
                                .load(mIconArrayList.get(i).getIcon())
                                .apply(RequestOptions.placeholderOf(R.drawable.icon_dates).error(R.drawable.icon_dates))
                                .into(ivHalloween);
                    }
                }else if (i == 1){
                    if (mIconArrayList.get(i).getTitle() != null && mIconArrayList.get(i).getTitle().length() > 0){
                        tvVirtual.setText(mIconArrayList.get(i).getTitle());
                        llvirtualLine.setVisibility(View.VISIBLE);
                    }

                    if (mIconArrayList.get(i).getIcon() != null && mIconArrayList.get(i).getIcon().contains("http")){
                        Glide.with(getApplicationContext())
                                .load(mIconArrayList.get(i).getIcon())
                                .apply(RequestOptions.placeholderOf(R.drawable.icon_dates).error(R.drawable.icon_dates))
                                .into(ivVirtualLines);
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
                if (UniversalOrlandoPreference.readString(DrawerActivity.this, UniversalOrlandoPreference.USER_LAT, "") != null) {
                    savingCurrentLocation(UniversalOrlandoPreference.readString(DrawerActivity.this, UniversalOrlandoPreference.USER_LAT, ""), UniversalOrlandoPreference.readString(DrawerActivity.this, UniversalOrlandoPreference.USER_LNG, ""));
                }
                startLocationUpdates();

            }
        }


        if (!strUserName.equals("")) {
            strUserName = testSplit(strUserName);
            tvSignIn.setText(getString(R.string.hiText) + strUserName);
            llSignOut.setVisibility(View.VISIBLE);
        } else {
            tvSignIn.setText(getString(R.string.SignIn));
            llSignOut.setVisibility(View.GONE);
        }


        if (isNetworkAvailable(DrawerActivity.this)) {
            hitIconApi();
        } else {
            Toast.makeText(DrawerActivity.this, getString(R.string.checkInternet), Toast.LENGTH_SHORT).show();
        }

        if(!isRunningTimer){
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    disablingScreen();
                }
            };
            timer=new Timer();
            timer.schedule(timerTask, 0, 2000);
            isRunningTimer = true;
            Log.d("TIMER START~~~~", "");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                stopLocationUpdates();
            }
        }

        if(isRunningTimer){
            timer.cancel();
            isRunningTimer = false;
            Log.d("TIMER STOP~~~~RESUME", "");
        }
    }

    public void disablingScreen() {
        Log.d("TIMER~~~~Drawer", "");
        if (!mobile.equals("") && !orderId.equals("")) {
            RestClient.get().loggingInstantinactiveTickets(mobile, orderId).enqueue(new Callback<Order>() {
                @Override
                public void onResponse(Call<Order> call, Response<Order> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getIsdisabled().equals("1")) {
                            logOutAlert(mActivity);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Order> call, Throwable t) {

                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (mCurrentLocation == null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        }
    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        mLatitude = Double.doubleToRawLongBits(mCurrentLocation.getLatitude());
        mLongitude = Double.doubleToRawLongBits(mCurrentLocation.getLongitude());

//        try {
//            Geocoder geocoder;
//            List<Address> addresses;
//            geocoder = new Geocoder(this, Locale.getDefault());
//            addresses = geocoder.getFromLocation(Double.longBitsToDouble(mLatitude), Double.longBitsToDouble(mLongitude), 1);
//            String address = addresses.get(0).getAddressLine(0);
//            String city = addresses.get(0).getLocality();
//            String state = addresses.get(0).getAdminArea();
//            String country = addresses.get(0).getCountryName();
//            String postalCode = addresses.get(0).getPostalCode();
//            String knownName = addresses.get(0).getFeatureName();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i("", "Connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i("", "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(Constants.KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(Constants.KEY_LOCATION, mCurrentLocation);
        savedInstanceState.putString(Constants.KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }


    public void savingCurrentLocation(String lat, String lng) {
        RestClient.get().savingLocation(lat, lng, UniversalOrlandoPreference.readString(DrawerActivity.this, UniversalOrlandoPreference.GID, "")).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i("savingLocations", response.body().getMessage());

                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Log.i(TAG, t.toString());
            }
        });
    }

    public void savingLocationLatLng(double lat, double lng) {
        String cominglat = String.valueOf(lat);
        String cominglong = String.valueOf(lng);
        UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.USER_LAT, cominglat);
        UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.USER_LNG, cominglong);
        savingCurrentLocation(cominglat, cominglong);
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public String testSplit(String s) {
        String[] strs = s.split(" ");

        return strs[0];
    }

}
