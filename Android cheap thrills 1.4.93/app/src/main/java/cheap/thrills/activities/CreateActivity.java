package cheap.thrills.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cheap.thrills.R;
import cheap.thrills.UniversalResortApplication;
import cheap.thrills.fonts.TextViewArialBold;
import cheap.thrills.fonts.TextViewArialDoubleBold;
import cheap.thrills.fonts.TextViewArialRegular;
import cheap.thrills.models.Profile;
import cheap.thrills.retrofit.RestClient;
import cheap.thrills.utils.UniversalOrlandoPreference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreateActivity extends BaseActivity {
    Activity mActivity = CreateActivity.this;
    String TAG = CreateActivity.this.getClass().getSimpleName();
    @BindView(R.id.createToolBar)
    Toolbar createToolBar;
    @BindView(R.id.tvSignIn)
    TextView tvSignIn;
    @BindView(R.id.tvTextCheckBox)
    TextView tvTextCheckBox;
    @BindView(R.id.btnSignUp)
    Button btnSignUp;
    @BindView(R.id.cbCheckBoxUpdates)
    CheckBox cbUpdatesl;
    @BindView(R.id.cbCheckBoxTerms)
    CheckBox cbTermsService;
    @BindView(R.id.etEmailAddress)
    EditText etEmail;
    @BindView(R.id.etFirstName)
    EditText etFirstName;
    @BindView(R.id.etLastname)
    EditText etLastname;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.tvToolBarTittle)
    TextViewArialDoubleBold tvToolBarTittle;
    @BindView(R.id.tvAlreadyAccount)
    TextViewArialBold tvAlreadyAccount;
    @BindView(R.id.tvTextCheckBox2)
    TextViewArialRegular tvTextCheckBox2;
    //PUSH NOTIFICATIONS
    public static void getFcmId(Context context) {
        FirebaseApp.initializeApp(context);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                String fcm_id = FirebaseInstanceId.getInstance().getToken();

                return null;
            }

            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //signUp();
            }
        }.execute();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        ButterKnife.bind(this);
        setToolbar();
    }

    private void setToolbar() {
        createToolBar.setNavigationIcon(R.drawable.baseline_keyboard_backspace_white_24);
        createToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (UniversalResortApplication.profile == null) {
            super.onBackPressed();
            finish();
        }
    }
    private void validateCreateAccount() {
        if (etFirstName.getText().toString().equals("")) {
            showToastAlert(CreateActivity.this, getString(R.string.enterFirstName));
        } else if (etLastname.getText().toString().equals("")) {
            showToastAlert(CreateActivity.this, getString(R.string.enterLastName));
        } else if (etEmail.getText().toString().trim().equals("")) {
            showToastAlert(CreateActivity.this, getString(R.string.enterEmail));
        } else if (!etEmail.getText().toString().trim().contains(getString(R.string.emailcheck))) {
            showToastAlert(CreateActivity.this, getString(R.string.enterValidEmail));
        } else if (!etEmail.getText().toString().trim().contains(getString(R.string.emailcomcheck))) {
            showToastAlert(CreateActivity.this, getString(R.string.enterValidEmail));
        } else if (etPassword.getText().toString().equals("")) {
            showToastAlert(CreateActivity.this, getString(R.string.enterPassword));
        } else if (etPassword.getText().toString().length() < 8) {
            showToastAlert(CreateActivity.this, getString(R.string.enterValidPassword));
        } else if (!cbUpdatesl.isChecked()) {
            showToastAlert(CreateActivity.this, getString(R.string.updates));
        } else if (!cbTermsService.isChecked()) {
            showToastAlert(CreateActivity.this, getString(R.string.enterTermsPrivacy));
        } else {
            if (isNetworkAvailable(CreateActivity.this)) {
                createApiHitting();
            } else {
                showToastAlert(CreateActivity.this, getString(R.string.checkInternet));
            }

        }
    }

    private void createApiHitting() {
        showProgressDialog(CreateActivity.this);
        RestClient.get().signUpAccount(etFirstName.getText().toString().trim(), etLastname.getText().toString().trim(), etEmail.getText().toString().trim(), etPassword.getText().toString().trim(), 1, 1).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                dismissProgressDailog();
                if (response.isSuccessful() && response.body() != null) {
                    showToastAlert(CreateActivity.this, response.body().getMessage());
                    UniversalOrlandoPreference.writeBoolean(mActivity, UniversalOrlandoPreference.IS_LOGIN, true);
                    UniversalOrlandoPreference.writeBoolean(mActivity, UniversalOrlandoPreference.ONCE_LOGGED_IN, true);
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.USER_NAME, etFirstName.getText().toString().trim());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.USER_EMAIL, etEmail.getText().toString().trim());
                    UniversalOrlandoPreference.writeString(mActivity, UniversalOrlandoPreference.USER_LAST_NAME, etLastname.getText().toString().trim());
                    Intent mIntent = new Intent(mActivity, DrawerActivity.class);
                    mIntent.putExtra(getString(R.string.profileMaking), true);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mIntent);
                } else {
                    alertErrorDialog(CreateActivity.this,getString(R.string.somethingWentWrong));
                }
            }

            public void onFailure(Call<Profile> call, Throwable t) {
                showToastAlert(CreateActivity.this, t.toString());

            }
        });
 }

    @OnClick({R.id.tvSignIn, R.id.btnSignUp})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSignIn:
                finish();
                break;
            case R.id.btnSignUp:
                validateCreateAccount();
                break;
        }
    }
}
