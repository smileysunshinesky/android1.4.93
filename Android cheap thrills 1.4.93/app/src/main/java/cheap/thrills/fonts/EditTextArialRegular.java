package cheap.thrills.fonts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;


public class EditTextArialRegular extends EditText {
    public EditTextArialRegular(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public EditTextArialRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public EditTextArialRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }


    public void applyCustomFont(Context context) {
        try {
            this.setTypeface(new ArialRegular(context).getFontFamily());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
