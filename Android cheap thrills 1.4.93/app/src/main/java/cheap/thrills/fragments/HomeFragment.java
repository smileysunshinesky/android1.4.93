package cheap.thrills.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cheap.thrills.R;
import cheap.thrills.activities.DrawerActivity;
import cheap.thrills.adapters.SlidingImageAdapter;
import cheap.thrills.models.ImageObject;
import cheap.thrills.models.Profile;
import cheap.thrills.retrofit.RestClient;
import cheap.thrills.utils.Constants;
import cheap.thrills.utils.UniversalOrlandoPreference;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragment {
    View mRooView;
    int NUM_PAGES;
    @BindView(R.id.ivSignIn)
    ImageView ivSignIn;
    @BindView(R.id.firstLL)
    LinearLayout firstLL;
    @BindView(R.id.secondLL)
    LinearLayout secondLL;
    @BindView(R.id.thirdLL)
    LinearLayout thirdLL;
    @BindView(R.id.showsLL)
    LinearLayout showsLL;
    @BindView(R.id.diningLL)
    LinearLayout diningLL;
    @BindView(R.id.buyTicketsLL)
    LinearLayout buyTicketsLL;
    @BindView(R.id.topParentLL)
    LinearLayout topParentLL;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.txtLeftTV)
    TextView txtLeftTV;
    @BindView(R.id.txtRightTV)
    TextView txtRightTV;
    @BindView(R.id.centerAnimLL)
    LinearLayout centerAnimLL;
    @BindView(R.id.rideLL)
    LinearLayout rideLL;
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingUpPanelLayout;
    @BindView(R.id.layoutTopHeaderLL)
    RelativeLayout layoutTopHeaderLL;
    Animation rotate_forward, rotate_backward;
    @BindView(R.id.llMap)
    LinearLayout llMap;
    @BindView(R.id.indicator)
    CirclePageIndicator indicator;

    @Nullable
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    Boolean isFabOpen = false;
    int currentPage = 1;
    SlidingImageAdapter sAdapter;
    List<String> images = new ArrayList<>();
    Unbinder unbinder;

    Timer timerScrollImg = new Timer();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRooView = inflater.inflate(R.layout.new_home_fragment_sliding, container, false);
        unbinder = ButterKnife.bind(this, mRooView);
        changeToolbarText();
        setPanelState();
        setAnimations();

        return mRooView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("AAAAAA", "RESUME");

//        logOut(getActivity());
        executeImagesAPI();

        final Runnable setImageRunnable = new Runnable() {
            public void run() {
                assert viewPager != null;
                if(viewPager.getAdapter()!=null){
                    int pos = viewPager.getCurrentItem();
                    pos = pos + 1;
                    if(pos >= viewPager.getAdapter().getCount()) pos = 0;
                    viewPager.setCurrentItem(pos);
                }
            }
        };

        TimerTask timerTask = new TimerTask(){
            @Override
            public void run() {
                getActivity().runOnUiThread(setImageRunnable);
            }
        };



//        TimerTask timerTask = new TimerTask() {
//
//            public void run() {
//                assert viewPager != null;
//                if(viewPager.getAdapter()!=null){
//                    int pos = viewPager.getCurrentItem();
//                    pos = pos + 1;
//                    if(pos >= viewPager.getAdapter().getCount()) pos = 0;
//                    viewPager.setCurrentItem(pos);
//                }
//            }
//        };
//        currentPage = 0;
        timerScrollImg=new Timer();
        timerScrollImg.schedule(timerTask, 0, 2000);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("AAAAAA", "PAUSE");
        timerScrollImg.cancel();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("AAAAAA", "STOP");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("AAAAAA", "START");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void executeImagesAPI() {
        if (isNetworkAvailable(Objects.requireNonNull(getActivity())))
            setPhotosData();
        else {
            showToast(getActivity(), getString(R.string.checkInternet));
        }
    }


    private void setAnimations() {
        rotate_forward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_backward);
    }

    private void setPanelState() {
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        slidingUpPanelLayout.setTouchEnabled(false);
    }

    public void animateFAB(){
        if (isFabOpen) {
            llMap.setBackground(getResources().getDrawable(R.drawable.mapnotcut));
            floatingActionButton.startAnimation(rotate_backward);
            floatingActionButton.setImageResource(R.drawable.ic_location_24dp);
            isFabOpen = false;
            txtRightTV.setVisibility(View.VISIBLE);
            txtLeftTV.setVisibility(View.VISIBLE);
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        } else {
            floatingActionButton.startAnimation(rotate_forward);
            floatingActionButton.setImageResource(R.drawable.ic_close_24dp);
            isFabOpen = true;
            txtRightTV.setVisibility(View.GONE);
            txtLeftTV.setVisibility(View.GONE);
            llMap.setBackground(getResources().getDrawable(R.drawable.map_imagewithcut));
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }

    private void setViewPager() {
        sAdapter = new SlidingImageAdapter(images, getActivity());
        assert viewPager != null;
        viewPager.setAdapter(sAdapter);
        viewPager.setClipToPadding(false);
        if (UniversalOrlandoPreference.readString(getActivity(), UniversalOrlandoPreference.USER_LAT, "") != null) {
            savingCurrentLocation(UniversalOrlandoPreference.readString(getActivity(), UniversalOrlandoPreference.USER_LAT, ""), UniversalOrlandoPreference.readString(getActivity(), UniversalOrlandoPreference.USER_LNG, ""));
        }
    }




    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.floatingActionButton, R.id.ivSignIn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.floatingActionButton:
                animateFAB();
                break;
            case R.id.ivSignIn:
                alertErrorDialog(getActivity());
                break;
        }
    }

    public void setPhotosData() {
        showProgressDialog(getActivity());
        RestClient.get().images().enqueue(new Callback<ImageObject>() {
            @Override
            public void onResponse(Call<ImageObject> call, Response<ImageObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    dismissProgressDailog();
                    images.clear();
                    images.addAll(response.body().getData().get(0).getImage());

                    setViewPager();
                }
            }

            @Override
            public void onFailure(Call<ImageObject> call, Throwable t) {
                dismissProgressDailog();
                Log.i("Exception", t.toString());

            }
        });

    }

    public void circularBarIndicator() {
        indicator.setViewPager(viewPager);
        final float density = getResources().getDisplayMetrics().density;
        //Set circle indicator radius
        indicator.setRadius(5 * density);
        NUM_PAGES = images.size();
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 1;
                }
                if (viewPager != null)
                    viewPager.setCurrentItem(currentPage++, true);

            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
                Log.d("ADFDKFDKDKDKDKD", "DKFLSDJFKLDF");
            }
        }, Constants.SPLASH_TIME_OUT, Constants.SPLASH_TIME_OUT);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    public void savingCurrentLocation(String lat, String lng) {
        RestClient.get().savingLocation(lat, lng, UniversalOrlandoPreference.readString(getActivity(), UniversalOrlandoPreference.GID, "")).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i("savingLocations", response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Log.i("Exception", t.toString());
            }
        });
    }

    private void changeToolbarText() {
        DrawerActivity.homeclicked = 1;
        Intent i = new Intent(getString(R.string.changeToolBarTittle));
        i.putExtra(getString(R.string.title), getString(R.string.titleHome));
        Objects.requireNonNull(getActivity()).sendBroadcast(i);
    }
}
