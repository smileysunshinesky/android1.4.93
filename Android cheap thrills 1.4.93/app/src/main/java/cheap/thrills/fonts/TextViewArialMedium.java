package cheap.thrills.fonts;

import android.content.Context;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;



public class TextViewArialMedium extends TextView {
    public TextViewArialMedium(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public TextViewArialMedium(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public TextViewArialMedium(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TextViewArialMedium(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        applyCustomFont(context);
    }

    public void applyCustomFont(Context context) {
        try {
            this.setTypeface(new ArialMedium(context).getFontFamily());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
