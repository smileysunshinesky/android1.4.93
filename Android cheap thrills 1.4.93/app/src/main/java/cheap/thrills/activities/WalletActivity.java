package cheap.thrills.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import cheap.thrills.R;
import cheap.thrills.adapters.TicketAdapter;
import cheap.thrills.adapters.UserAdapter;
import cheap.thrills.fonts.TextViewGotham;
import cheap.thrills.interfaces.AdapterCallback;
import cheap.thrills.models.Order;
import cheap.thrills.retrofit.RestClient;
import cheap.thrills.utils.Constants;
import cheap.thrills.utils.UniversalOrlandoPreference;
import cheap.thrills.views.NonSwipeableViewPager;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletActivity extends BaseActivity {
    Activity mActivity = WalletActivity.this;
    String TAG = WalletActivity.this.getClass().getSimpleName();

    @BindView(R.id.leftArrow)
    ImageView ivLeft;
    @BindView(R.id.rightarrow)
    ImageView ivRight;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.containerLL)
    LinearLayout llContainer;
    @BindView(R.id.tbWallet)
    Toolbar tbWallet;
    @BindView(R.id.userRecycler)
    RecyclerView userRecycler;
    @BindView(R.id.tvToolBarTittle)
    TextViewGotham tvToolBarTittle;


    Order order;
    ArrayList<Order> orders = new ArrayList<>();
    UserAdapter uAdApter;
    TicketAdapter trialAdapter;
    ArrayList<Order> mList = new ArrayList<Order>();
    ArrayList<Order> userList = new ArrayList<Order>();
    Handler handler;
    Integer kidCount;
    int currentPosition = 0;
    boolean shoulDeVisible = false;
    String ticket_order = "", baseUrl = "", gid = "", order_id = "", loginId = "", name = "", ticketDate = "", customer = "", ticketId = "", mobile = "", type = "", orderId = "";

    Timer timer = new Timer();
    Boolean isRunningTimer = false;

    AdapterCallback mCallBack = new AdapterCallback() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onMethodCallback(Order o) {
            getWindow().setExitTransition(new Explode());
            order = o;

            trialAdapter.notifyDataSetChanged();
            for (int i = 0; i < orders.size(); i++) {

                if (order.getGid() == orders.get(i).getGid()) {
                    viewPager.setCurrentItem(i);
                }
            }


        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_wallet);
        ButterKnife.bind(this);

        collectUserData();

        setToolbar();

        getttingData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                disablingScreen();
            }
        };
        if(!isRunningTimer){
            timer = new Timer();
            timer.schedule(timerTask, 0, 2000);
            isRunningTimer = true;
            Log.d("TIMER START~~~~", "");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isRunningTimer){
            timer.cancel();
            isRunningTimer = false;
            Log.d("TIMER STOP~~~~", "");
        }

    }

    private void collectUserData() {
        if (!UniversalOrlandoPreference.readString(mActivity, UniversalOrlandoPreference.MOBILE, "").equals("")) {
            mobile = UniversalOrlandoPreference.readString(WalletActivity.this, UniversalOrlandoPreference.MOBILE, "");
        }
        if (!UniversalOrlandoPreference.readString(mActivity, UniversalOrlandoPreference.LOGGEDIN_ORDERID, "").equals("")) {
            orderId = UniversalOrlandoPreference.readString(WalletActivity.this, UniversalOrlandoPreference.LOGGEDIN_ORDERID, "");
            loginId = UniversalOrlandoPreference.readString(WalletActivity.this, UniversalOrlandoPreference.LOGGEDIN_ORDERID, "");
        }
        if (!UniversalOrlandoPreference.readString(mActivity, UniversalOrlandoPreference.GID, "").equals("")) {
            gid = UniversalOrlandoPreference.readString(WalletActivity.this, UniversalOrlandoPreference.GID, "");
        }
        if (!UniversalOrlandoPreference.readString(mActivity, UniversalOrlandoPreference.USER_NAME, "").equals("")) {
            name = UniversalOrlandoPreference.readString(WalletActivity.this, UniversalOrlandoPreference.USER_NAME, "");
        }
        if (!UniversalOrlandoPreference.readString(mActivity, UniversalOrlandoPreference.ORDER_ID, "").equals("")) {
            order_id = UniversalOrlandoPreference.readString(WalletActivity.this, UniversalOrlandoPreference.ORDER_ID, "");
        }
        if (!UniversalOrlandoPreference.readString(mActivity, UniversalOrlandoPreference.TYPE, "").equals("")) {
            type = UniversalOrlandoPreference.readString(WalletActivity.this, UniversalOrlandoPreference.TYPE, "");
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void getttingData() {
        if (getIntent() != null) {
            mList = Constants.orderList;
        }
        ticket_order = getIntent().getStringExtra(Constants.TICKET_ORDER);
        customer = getIntent().getStringExtra(Constants.CUSTOMER_NAME);
        ticketDate = getIntent().getStringExtra(Constants.TICKET_DATE);
        ticketId = getIntent().getStringExtra(Constants.TICKET_ID);
        mobile = getIntent().getStringExtra(Constants.MOBILE);
        type = getIntent().getStringExtra(Constants.TYPE);
        kidCount = Integer.valueOf(getIntent().getStringExtra(Constants.KID_COUNT));
        baseUrl = getIntent().getStringExtra(Constants.TICKET_URL);
        shoulDeVisible = getIntent().getBooleanExtra(Constants.VISIBILITYCHECK, false);
        order.setTicketOrder(ticket_order);
        order.setName(customer);
        order.setTdate(ticketDate);
        order.setMobile(mobile);
        order.setType(type);
        order.setQrCode(baseUrl);
        order.setTicketId(ticketId);
        for (int j = 0; j < mList.size(); j++) {
            if (mList.get(j).getMobile().equals(mobile)) {
                if (type.equals(mList.get(j).getType())) {
                    orders.add(mList.get(j));
                }
            }
        }
        if (orders.size() > 1) {
            ivRight.setVisibility(View.VISIBLE);
        } else {
            ivRight.setVisibility(View.GONE);
        }

        if (kidCount > 1) {
            trialAdapter = new TicketAdapter(orders, mActivity, true);
        } else {
            trialAdapter = new TicketAdapter(orders, mActivity, false);
        }

        viewPager.setAdapter(trialAdapter);
        viewPager.setClipToPadding(false);

        if (!ticket_order.equals("")) {
            for (int j = 0; j < mList.size(); j++) {
                if (mList.get(j).getMobile().equals(mobile)) {
                    if (type.equals(mList.get(j).getType())) {
                        userList.add(mList.get(j));
                    }
                }
            }
            userList.get(0).setiSclicked(true);
            uAdApter = new UserAdapter(userList, mActivity, mCallBack, viewPager);
            userRecycler.setNestedScrollingEnabled(false);
            userRecycler.setLayoutManager(new LinearLayoutManager(this));
            userRecycler.setAdapter(uAdApter);
        }

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                disablingScreen();
                return false;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                Log.d("ONPAGESCROLLED", String.valueOf(i));
            }

            @Override
            public void onPageSelected(int ind) {
                if(viewPager != null && uAdApter != null){
                    currentPosition = ind;
                    if (ind == viewPager.getAdapter().getCount() - 1) {
                        ivLeft.setVisibility(View.VISIBLE);
                        ivRight.setVisibility(View.GONE);
                    } else if (ind == 0) {
                        ivRight.setVisibility(View.VISIBLE);
                        ivLeft.setVisibility(View.GONE);
                    } else {
                        ivLeft.setVisibility(View.VISIBLE);
                        ivRight.setVisibility(View.VISIBLE);
                    }

                    for (int i = 0; i < userList.size(); i++) {
                        userList.get(i).setiSclicked(i == ind);
                    }
                    uAdApter.notifyDataSetChanged();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

    }

    @OnClick({R.id.leftArrow, R.id.rightarrow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.leftArrow:
                leftClick();
                break;
            case R.id.rightarrow:
                rightClick();
                break;
        }
    }

    private void rightClick() {
        if(viewPager != null && uAdApter != null)
        {
            viewPager.setCurrentItem(currentPosition + 1);
            for (int i = 0; i < userList.size(); i++) {
                if (i == viewPager.getCurrentItem()) {
                    userList.get(viewPager.getCurrentItem()).setiSclicked(true);
                } else
                    userList.get(i).setiSclicked(false);

            }
            uAdApter.notifyDataSetChanged();
        }
    }

    private void leftClick() {
        if(viewPager != null && uAdApter != null) {
            viewPager.setCurrentItem(currentPosition - 1);
            for (int i = 0; i < userList.size(); i++) {
                if (i == viewPager.getCurrentItem()) {
                    userList.get(viewPager.getCurrentItem()).setiSclicked(true);
                } else
                    userList.get(i).setiSclicked(false);

            }
            uAdApter.notifyDataSetChanged();
        }
    }

    private void setToolbar() {
        tbWallet.setNavigationIcon(R.drawable.baseline_keyboard_backspace_white_24);
        tbWallet.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                orders.clear();
            }
        });
        order = new Order();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sendBroadcast(new Intent(getString(R.string.hitAPi)));
    }

    public void disablingScreen() {
        Log.d("TIMER~~~~WalletActivity", "");
        if (!mobile.equals("") && !orderId.equals("")) {
            RestClient.get().loggingInstantinactiveTickets(mobile, orderId).enqueue(new Callback<Order>() {
                @Override
                public void onResponse(Call<Order> call, Response<Order> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getIsdisabled().equals("1")) {
                            showToastAlert(WalletActivity.this, getString(R.string.signedOut));
                            logOutAlert(WalletActivity.this);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Order> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(WalletActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
