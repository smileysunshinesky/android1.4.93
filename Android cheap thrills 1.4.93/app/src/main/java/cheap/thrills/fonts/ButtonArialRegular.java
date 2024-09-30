package cheap.thrills.fonts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;


public class ButtonArialRegular extends Button{
    public ButtonArialRegular(Context context) {
        super(context);
    }

    public ButtonArialRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ButtonArialRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ButtonArialRegular(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    public void applyCustomFont(Context context) {
        try {
            this.setTypeface(new ArialRegular(context).getFontFamily());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
