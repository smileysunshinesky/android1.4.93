package cheap.thrills.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.Toolbar;

import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cheap.thrills.R;
import cheap.thrills.fonts.TextViewArialRegular;
import cheap.thrills.models.Profile;
import cheap.thrills.retrofit.RestClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignInActivity extends BaseActivity {
    Activity mActivity = SignInActivity.this;
    String TAG = mActivity.getClass().getSimpleName();
    @BindView(R.id.tvForgotPassword)
    TextView tvForgotPassword;
    @BindView(R.id.tvCreateAccount)
    TextView tvCreateAccount;
    @BindView(R.id.etEmailAddress)
    EditText etEmailAddress;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.cbCheckBox)
    AppCompatCheckBox cbCheckRememberMe;
    @BindView(R.id.tbSignIn)
    Toolbar tbSignIn;
    @BindView(R.id.btnSignIn)
    Button btnSignIn;
    @BindView(R.id.tvTextCheckBox)
    TextViewArialRegular tvTextCheckBox;

    Intent mIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        setToolbar();

        etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
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


    @OnClick({R.id.tvCreateAccount, R.id.tvForgotPassword, R.id.btnSignIn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvCreateAccount:
                mIntent = new Intent(mActivity, CreateActivity.class);
                startActivity(mIntent);
                break;
            case R.id.tvForgotPassword:
                mIntent = new Intent(mActivity, ForgotPasswordActivity.class);
                startActivity(mIntent);
                break;
            case R.id.btnSignIn:
                validate();
                break;
        }
    }


    public void validate() {
        if (etEmailAddress.getText().toString().trim().equals("")) {
            showToastAlert(mActivity, getString(R.string.enterEmail));
        } else if (!etEmailAddress.getText().toString().trim().contains(getString(R.string.emailcheck))) {
            showToastAlert(mActivity, getString(R.string.enterValidEmail));
        } else if (!etEmailAddress.getText().toString().trim().contains(getString(R.string.emailcomcheck))) {
            showToastAlert(mActivity, getString(R.string.enterValidEmail));
        } else if (etPassword.getText().toString().trim().equals("")) {
            showToastAlert(mActivity, getString(R.string.enterPassword));
        } else {
            if (isNetworkAvailable(mActivity)) {
                executeLoginAPI();
            } else {
                showToastAlert(mActivity, getString(R.string.checkInternet));
            }
        }
    }

    /*Execute SignIn API*/
    private void executeLoginAPI() {
        showProgressDialog(mActivity);
        RestClient.get().signInAccount(etEmailAddress.getText().toString().trim(), etPassword.getText().toString().trim()).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                dismissProgressDailog();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus().equals("1")) {

                    } else {
                        alertErrorDialog(mActivity,getString(R.string.somethingWentWrong));
                    }
                } else {
                    alertErrorDialog(mActivity,getString(R.string.somethingWentWrong));
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                showToastAlert(mActivity, t.toString());

            }
        });
    }

}
