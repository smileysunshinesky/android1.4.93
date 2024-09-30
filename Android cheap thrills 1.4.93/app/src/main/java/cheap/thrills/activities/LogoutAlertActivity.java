package cheap.thrills.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cheap.thrills.R;

public class LogoutAlertActivity extends AppCompatActivity {

    @BindView(R.id.textViewArialMedium)
    cheap.thrills.fonts.TextViewArialMedium textViewArialMedium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout_alert);
        ButterKnife.bind(this);
        SpannableStringBuilder builder = new SpannableStringBuilder();

        String leave = "Leave the app OPEN, do not close or end task or swipe it away. Doing so will cause issues when you get to the park. ";
        SpannableString redSpannable= new SpannableString(leave);
        redSpannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, leave.length(), 0);
        builder.append(redSpannable);

        String login = "DON'T click \"Login\"";
        SpannableString whiteSpannable= new SpannableString(login);
        whiteSpannable.setSpan(new ForegroundColorSpan(Color.RED), 0, login.length(), 0);
        whiteSpannable.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, login.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(whiteSpannable);

        String until = " until you have called to have your tickets activated at the park.If you're already inside the park,you can close the app.";
        SpannableString blueSpannable = new SpannableString(until);
        blueSpannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, until.length(), 0);
        builder.append(blueSpannable);
        textViewArialMedium.setText(builder, TextView.BufferType.SPANNABLE);;
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.btnLogiIn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogiIn:
                Intent i = new Intent(this, OrderIdLoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                break;
        }
    }
}