package cheap.thrills.fonts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;



public class ButtonArialMedium extends Button {

    public ButtonArialMedium(Context context) {
        super(context);
    }

    public ButtonArialMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ButtonArialMedium(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ButtonArialMedium(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void applyCustomFont(Context context) {
        try {
            this.setTypeface(new ArialMedium(context).getFontFamily());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
