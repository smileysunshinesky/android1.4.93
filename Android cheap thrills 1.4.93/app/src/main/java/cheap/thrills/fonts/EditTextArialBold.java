package cheap.thrills.fonts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;


public class EditTextArialBold extends EditText {
    public EditTextArialBold(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public EditTextArialBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public EditTextArialBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
