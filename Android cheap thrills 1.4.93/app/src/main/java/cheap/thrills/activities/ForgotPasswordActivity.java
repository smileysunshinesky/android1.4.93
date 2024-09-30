package cheap.thrills.activities;

import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cheap.thrills.R;
import cheap.thrills.models.Profile;
import cheap.thrills.retrofit.RestClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ForgotPasswordActivity extends BaseActivity {
    Activity mActivity = ForgotPasswordActivity.this;
    String TAG = ForgotPasswordActivity.this.getClass().getSimpleName();

    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.etEmailAddress)
    EditText etEmailAddress;
    @BindView(R.id.tbSignIn)
    Toolbar tbSignIn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        setToolbar();
    }


    private void setToolbar() {
        tbSignIn.setNavigationIcon(R.drawable.baseline_keyboard_backspace_white_24);
        tbSignIn.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void validate() {
        if (etEmailAddress.getText().toString().trim().equals("")) {
            showToastAlert(ForgotPasswordActivity.this, getString(R.string.enterEmail));
        } else if (!etEmailAddress.getText().toString().trim().contains(getString(R.string.emailcheck))) {
            showToastAlert(ForgotPasswordActivity.this, getString(R.string.enterValidEmail));
        } else if (!etEmailAddress.getText().toString().trim().contains(getString(R.string.emailcomcheck))) {
            showToastAlert(ForgotPasswordActivity.this, getString(R.string.enterValidEmail));
        } else {
            if (isNetworkAvailable(mActivity)) {
                forgotpasswordApi();
            } else {
                showToastAlert(mActivity, getString(R.string.checkInternet));

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void forgotpasswordApi() {
        showProgressDialog(ForgotPasswordActivity.this);
        RestClient.get().ForgotPassword(etEmailAddress.getText().toString().trim()).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (response.isSuccessful() && response.body() != null) {
                    dismissProgressDailog();
                    showToastAlert(ForgotPasswordActivity.this, getString(R.string.recovery));
                    finish();
                } else {
                    alertErrorDialog(ForgotPasswordActivity.this, getString(R.string.somethingWentWrong));
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                showToastAlert(ForgotPasswordActivity.this, t.toString());
            }
        });
    }

    @OnClick({R.id.btnSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                validate();
                break;
        }
    }
}


