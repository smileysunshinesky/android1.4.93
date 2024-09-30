package cheap.thrills.fonts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;



public class ButtonArialBold extends Button {

    public ButtonArialBold(Context context) {
        super(context);
    }

    public ButtonArialBold(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ButtonArialBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ButtonArialBold(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void applyCustomFont(Context context) {
        try {
            this.setTypeface(new ArialBold(context).getFontFamily());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
