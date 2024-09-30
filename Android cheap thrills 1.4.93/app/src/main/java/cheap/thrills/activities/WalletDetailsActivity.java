package cheap.thrills.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import cheap.thrills.R;
import cheap.thrills.fonts.TextViewArialBold;
import cheap.thrills.fonts.TextViewArialDoubleBold;
import cheap.thrills.fonts.TextViewArialRegular;
import cheap.thrills.fonts.TextViewGotham;
import cheap.thrills.models.Order;
import cheap.thrills.retrofit.RestClient;
import cheap.thrills.utils.Constants;
import cheap.thrills.utils.UniversalOrlandoPreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletDetailsActivity extends BaseActivity {

    @BindView(R.id.tvProduct)
    TextViewArialRegular tvProduct;
    @BindView(R.id.llMain)
    LinearLayout llMain;
    @BindView(R.id.ll1)
    LinearLayout ll1;
    @BindView(R.id.ll2)
    LinearLayout ll2;
    @BindView(R.id.tvTicketOrderDetailsScreen)
    TextViewGotham tvTicketOrderDetailsScreen;
    @BindView(R.id.rlViewDetails)
    RelativeLayout rlViewDetails;
    @BindView(R.id.tvTicketNumber)
    TextViewArialRegular tvTicketNumber;
    @BindView(R.id.tvCustomer)
    TextViewArialRegular tvCustomer;
    @BindView(R.id.tvTicketOrder)
    TextViewArialDoubleBold tvTicketOrder;
    @BindView(R.id.tvIssuedDate)
    TextViewArialRegular tvIssueddate;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvToolBarTittle)
    TextViewGotham tvToolBarTittle;
    @BindView(R.id.tvViewusedTicket)
    TextViewArialBold tvViewusedTicket;

    private String ticket_order = "", customer = "", ticketDate = "", mobile = "", orderId = "", ticketId = "", type = "";

    Timer timer = new Timer();
    Boolean isRunningTimer = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_wallet_details);
        ButterKnife.bind(this);
        collectUserData();

        tvProduct.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                disablingScreen();
                return false;
            }
        });

        gettingData();
    }

    private void collectUserData() {
        if (!UniversalOrlandoPreference.readString(WalletDetailsActivity.this, UniversalOrlandoPreference.MOBILE, "").equals("")) {
            mobile = UniversalOrlandoPreference.readString(WalletDetailsActivity.this, UniversalOrlandoPreference.MOBILE, "");
        }
        if (!UniversalOrlandoPreference.readString(WalletDetailsActivity.this, UniversalOrlandoPreference.LOGGEDIN_ORDERID, "").equals("")) {
            orderId = UniversalOrlandoPreference.readString(WalletDetailsActivity.this, UniversalOrlandoPreference.LOGGEDIN_ORDERID, "");
        }
    }

    private void gettingData() {
        ticket_order = getIntent().getStringExtra(Constants.TICKET_ORDER);
        customer = getIntent().getStringExtra(Constants.CUSTOMER_NAME);
        ticketDate = getIntent().getStringExtra(Constants.TICKET_DATE);
        ticketId = getIntent().getStringExtra(Constants.TICKET_ID);
        type = getIntent().getStringExtra(Constants.TYPE);
        if (!ticket_order.equals("")) {
            if (type.equals(getString(R.string.typeadult))) {
                tvTicketOrder.setText(ticket_order);
                tvTicketOrderDetailsScreen.setText(ticket_order);
            } else {
                tvTicketOrder.setText(ticket_order);
                tvTicketOrderDetailsScreen.setText(ticket_order);
            }
        }

        tvCustomer.setText(customer);
        if (ticketDate.equals("")) {
            ticketDate = getDate();
        }
        tvIssueddate.setText(ticketDate);
        tvTicketNumber.setText(ticketId);

    }


    @OnClick({R.id.rlViewDetails, R.id.ivBack})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.rlViewDetails:
                setViewDetails();
                break;
        }

    }

    private void setViewDetails() {
        if (ll1.getVisibility() == View.VISIBLE) {
            ll1.setVisibility(View.GONE);
            ll2.setVisibility(View.VISIBLE);

            tvToolBarTittle.setText(getString(R.string.ViewDetails));
        }
    }

    @Override
    public void onBackPressed() {
        if (ll2.getVisibility() == View.VISIBLE) {
            ll1.setVisibility(View.VISIBLE);
            ll2.setVisibility(View.GONE);
            tvToolBarTittle.setText(getString(R.string.walletdetails));
        } else {
            super.onBackPressed();
        }
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
            timer=new Timer();
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

    public void disablingScreen() {
        Log.d("TIMER~~~~WalletDetail", "");
        if (!mobile.equals("") && !orderId.equals("")) {
            RestClient.get().loggingInstantinactiveTickets(mobile, orderId).enqueue(new Callback<Order>() {
                @Override
                public void onResponse(Call<Order> call, Response<Order> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getIsdisabled().equals("1")) {
                            showToastAlert(WalletDetailsActivity.this, getString(R.string.signedOut));
                            logOutAlert(WalletDetailsActivity.this);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Order> call, Throwable t) {
                    Toast.makeText(WalletDetailsActivity.this, t.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
