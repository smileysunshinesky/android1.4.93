package cheap.thrills.fonts;

import android.content.Context;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextviewItalic extends TextView {

    public TextviewItalic(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public TextviewItalic(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public TextviewItalic(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TextviewItalic(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        applyCustomFont(context);
    }

    public void applyCustomFont(Context context) {
        try {
            this.setTypeface(new ArialItalic(context).getFontFamily());
        } catch (Exception e) {
            e.printStackTrace();
        }}
    }
