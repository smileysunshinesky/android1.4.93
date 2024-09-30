package cheap.thrills.fonts;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Dharmani Apps on 1/18/2018.
 */

public class TextViewArialBold extends TextView {
    public TextViewArialBold(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public TextViewArialBold(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public TextViewArialBold(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    public TextViewArialBold(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        applyCustomFont(context);
    }

    public void applyCustomFont(Context context) {
        try {
            this.setTypeface(new ArialBold(context).getFontFamily());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
