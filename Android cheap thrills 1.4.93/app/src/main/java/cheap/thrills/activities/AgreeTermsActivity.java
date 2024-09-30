package cheap.thrills.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cheap.thrills.R;

public class AgreeTermsActivity extends BaseActivity {

    @BindView(R.id.etType)
    cheap.thrills.fonts.EditTextArialMedium etType;
    @BindView(R.id.leftImage)
    ImageView leftImage;
    @BindView(R.id.rightImage)
    ImageView rightImage;
    @BindView(R.id.textViewArialMedium)
    cheap.thrills.fonts.TextViewArialMedium textViewArialMedium;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree_terms);
        ButterKnife.bind(this);
        SpannableStringBuilder sb = new SpannableStringBuilder();
        String regular1Text = "First, we need you to agree to a couple of things…. Once you are logged in, NO SCREEN SHOTS are allowed. If you attempt a screen shot, your app access will be restricted. I ";
        String clickableIText = "agree";
        String regular2Text = " I will not take any screen shots. If you ";
        String clickableYouText = "agree";
        String regular3Text = " that you won’t take screen shots then please click the word ";
        String clickableWordText = "agree";
        sb.append(regular1Text);
        sb.append(clickableIText);
        sb.append(regular2Text);
        sb.append(clickableYouText);
        sb.append(regular3Text);
        sb.append(clickableWordText);
        sb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                setThirdTerms();
            }
            @Override
            public void updateDrawState(TextPaint ds) {// override updateDrawState
                ds.setUnderlineText(false); // set to false to remove underline
            }
        }, regular1Text.length(), regular1Text.length()+clickableYouText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                setThirdTerms();
            }
            @Override
            public void updateDrawState(TextPaint ds) {// override updateDrawState
                ds.setUnderlineText(false); // set to false to remove underline
            }
        }, (regular2Text.length() + regular1Text.length()+clickableYouText.length()), (regular2Text.length() + regular1Text.length()+clickableYouText.length()+clickableYouText.length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                setThirdTerms();
            }
            @Override
            public void updateDrawState(TextPaint ds) {// override updateDrawState
                ds.setUnderlineText(false); // set to false to remove underline
            }
        }, sb.length()-clickableWordText.length(), sb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewArialMedium.setMovementMethod(LinkMovementMethod.getInstance());
        textViewArialMedium.setText(sb);
    }

    void setSecondTerms(){
        leftImage.setVisibility(View.GONE);
        rightImage.setVisibility(View.VISIBLE);
        etType.setVisibility(View.VISIBLE);
        textViewArialMedium.setText("Next, I agree that I do not work directly or indirectly for the theme park or any third party agency affiliated with the theme park I am purchasing tickets for. If you agree that you have no affiliation with any of the theme parks then type agree in the box below");
        etType.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(etType.getText().toString().equalsIgnoreCase("agree")){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etType.getApplicationWindowToken(), 0);
                    leftImage.setVisibility(View.VISIBLE);
                    leftImage.setImageResource(R.drawable.agree2);
                    rightImage.setVisibility(View.GONE);
                    etType.setVisibility(View.GONE);
                    textViewArialMedium.clearFocus();
                    textViewArialMedium.setText("Finally, I understand that my tickets can only be used for admission into the park,and may not be used to get a disability pass, when required by the park. If you understand that you cannot get a disability pass, tap the wheelchair icon to the left.");
                    leftImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent mIntent = new Intent(AgreeTermsActivity.this, OrderIdLoginActivity.class);
                            startActivity(mIntent);
                            finish();
                        }
                    });
                }
            }
        });
    }

    void setThirdTerms(){
        leftImage.setVisibility(View.VISIBLE);
        leftImage.setImageResource(R.drawable.agree2);
        textViewArialMedium.setText("Finally, I understand that my tickets can only be used for admission into the park,and may not be used to get a disability pass, when required by the park. If you understand that you cannot get a disability pass, tap the wheelchair icon to the left.");
        leftImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(AgreeTermsActivity.this, OrderIdLoginActivity.class);
                startActivity(mIntent);
                finish();
            }
        });
    }
}