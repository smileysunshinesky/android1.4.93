package cheap.thrills.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import cheap.thrills.R;
import butterknife.BindView;
import butterknife.ButterKnife;


public class AlertActivity extends AppCompatActivity {
    @BindView(R.id.tbAlert)
    Toolbar tbAlert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        ButterKnife.bind(this);
        setToolbarClicks();
    }
    private void setToolbarClicks() {
        tbAlert.setNavigationIcon(R.drawable.ic_back_arrow_24dp);
        tbAlert.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
   @Override
    public void onBackPressed() {
        sendBroadcast(new Intent(getString(R.string.alert)));
        super.onBackPressed();
        finish();
    }
}
