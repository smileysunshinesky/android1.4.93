package cheap.thrills.fonts;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewGotham  extends TextView {
    public TextViewGotham(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public TextViewGotham(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public TextViewGotham(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    public TextViewGotham(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        applyCustomFont(context);
    }
    public void applyCustomFont(Context context) {
        try {
            this.setTypeface(new GoTham(context).getFontFamily());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
