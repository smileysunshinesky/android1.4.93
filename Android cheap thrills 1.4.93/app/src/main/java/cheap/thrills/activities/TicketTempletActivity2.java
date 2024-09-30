package cheap.thrills.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Timer;

import cheap.thrills.R;
import cheap.thrills.UniversalResortApplication;
import cheap.thrills.adapters.TicketViewAdapter2;
import cheap.thrills.fonts.ButtonArialMedium;
import cheap.thrills.location.LocationService;
import cheap.thrills.location.Utils;
import cheap.thrills.models.Order;
import cheap.thrills.models.Profile;
import cheap.thrills.retrofit.RestClient;
import cheap.thrills.utils.UniversalOrlandoPreference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TicketTempletActivity2 extends BaseActivity {

    private static final String TAG = "TicketTempletActivity2";
    private RecyclerView rvTickets;
    private List<Order> orderList;
    private TicketViewAdapter2 ticketViewAdapter;
    private ConstraintLayout constraintLayou2;
    private ButtonArialMedium btnViewTickets, btnLogout;
    private ImageButton btnClose;

    Timer socketTimer;
    int LOCATION_CODE = 100;
    int LOCATION_CODE2 = 101;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.light_yellow));
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                    WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_ticket_templet2);

        orderList = UniversalOrlandoPreference.readList(this);
        rvTickets = findViewById(R.id.rvTickets);
        constraintLayou2 = findViewById(R.id.constraintLayou2);
        btnViewTickets = findViewById(R.id.btnViewTickets);
        btnLogout = findViewById(R.id.btnLogout);
        btnClose = findViewById(R.id.btnClose);

        rvTickets.setHasFixedSize(true);
        rvTickets.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        if (!(orderList == null)){
            ticketViewAdapter = new TicketViewAdapter2(orderList, TicketTempletActivity2.this,UniversalOrlandoPreference.readString(TicketTempletActivity2.this, UniversalOrlandoPreference.THEMEPARK_NAME, ""));
            rvTickets.setAdapter(ticketViewAdapter);
        }

        constraintLayou2 = (ConstraintLayout) findViewById(R.id.constraintLayou2);
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_blur);
        Bitmap blurredBitmap = BlurBuilder.blur( this, originalBitmap );
        constraintLayou2.setBackground(new BitmapDrawable(getResources(), blurredBitmap));


        btnViewTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnViewTickets.setVisibility(View.GONE);
                btnLogout.setVisibility(View.GONE);
                constraintLayou2.setVisibility(View.VISIBLE);

                showProgressDialog(TicketTempletActivity2.this);
                Log.e("ORDER_ID",UniversalOrlandoPreference.readString(TicketTempletActivity2.this, UniversalOrlandoPreference.MOBILE, "")+"\n"+UniversalOrlandoPreference.readString(TicketTempletActivity2.this, UniversalOrlandoPreference.LOGIN_ID, ""));
                RestClient.get().ticketsList(UniversalOrlandoPreference.readString(TicketTempletActivity2.this, UniversalOrlandoPreference.MOBILE, ""),UniversalOrlandoPreference.readString(TicketTempletActivity2.this, UniversalOrlandoPreference.LOGIN_ID, "")).enqueue(new Callback<Profile>() {
                    @Override
                    public void onResponse(Call<Profile> call, Response<Profile> response) {
                        dismissProgressDailog();
                        if (response.isSuccessful()) {
                            orderList = response.body().getOrderDetails();
                            orderList = response.body().getOrderDetails();UniversalOrlandoPreference.writeString(TicketTempletActivity2.this, UniversalOrlandoPreference.THEMEPARK_NAME, response.body().getThemeParkName());
                            if (!(orderList == null)){
                                ticketViewAdapter = new TicketViewAdapter2(orderList, TicketTempletActivity2.this, UniversalOrlandoPreference.readString(TicketTempletActivity2.this, UniversalOrlandoPreference.THEMEPARK_NAME, ""));
                                rvTickets.setAdapter(ticketViewAdapter);
                            }
                            ticketViewAdapter.notifyDataSetChanged();
                            if (Integer.valueOf(response.body().getStatus()) == 404) {
                                alertErrorDialog(TicketTempletActivity2.this, response.body().getMessage());
                            } else if (Integer.valueOf(response.body().getStatus()) == 399) {
                                alertErrorDialog(TicketTempletActivity2.this, response.body().getMessage());
                            } else if (Integer.valueOf(response.body().getStatus()) == 401) {
                                alertErrorDialog(TicketTempletActivity2.this, response.body().getMessage());
                            }
                        }else {
                            showToastAlert(TicketTempletActivity2.this, "Tickets are not found");
                        }
                    }

                    @Override
                    public void onFailure(Call<Profile> call, Throwable t) {

                    }
                });
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut(TicketTempletActivity2.this);
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnViewTickets.setVisibility(View.VISIBLE);
                btnLogout.setVisibility(View.VISIBLE);
                constraintLayou2.setVisibility(View.GONE);
            }
        });
    }

    public void logOut(Activity activity) {
        UniversalResortApplication.profile = null;
        SharedPreferences preferences = UniversalOrlandoPreference.getPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(UniversalOrlandoPreference.IS_LOGIN,false).apply();
        Toast.makeText(activity, getString(R.string.signedOut), Toast.LENGTH_LONG).show();
        Intent i = new Intent(activity, OrderIdLoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

    @Override
    protected void onResume() {
        super.onResume();

        if (checkLocationPermission()) {
            if (!Utils.INSTANCE.isLocationServiceEnabled(this)) {
                showGPSAlert();
            }
            Intent intent = new Intent(TicketTempletActivity2.this, LocationService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getApplicationContext().startForegroundService(intent);
            } else getApplicationContext().startService(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static class BlurBuilder {
        private static final float BITMAP_SCALE = 0.4f;
        private static final float BLUR_RADIUS = 24.9f;

        public static Bitmap blur(Context context, Bitmap image) {
            int width = Math.round(image.getWidth() * BITMAP_SCALE);
            int height = Math.round(image.getHeight() * BITMAP_SCALE);

            Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
            Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

            RenderScript rs = RenderScript.create(context);
            ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
            Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
            theIntrinsic.setRadius(BLUR_RADIUS);
            theIntrinsic.setInput(tmpIn);
            theIntrinsic.forEach(tmpOut);
            tmpOut.copyTo(outputBitmap);

            return outputBitmap;
        }
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

        ActivityCompat.requestPermissions(TicketTempletActivity2.this,
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