package cheap.thrills.fonts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class TextViewHandelGothic extends TextView {
    public TextViewHandelGothic(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public TextViewHandelGothic(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public TextViewHandelGothic(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    public TextViewHandelGothic(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        applyCustomFont(context);
    }
    public void applyCustomFont(Context context) {
        try {
            this.setTypeface(new HandelGothic(context).getFontFamily());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
