package cheap.thrills.fonts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;


public class EditTextArialMedium extends EditText {
    public EditTextArialMedium(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public EditTextArialMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public EditTextArialMedium(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
