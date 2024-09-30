package cheap.thrills.fonts;

import android.content.Context;
import android.graphics.Typeface;

import cheap.thrills.R;

public class HandelGothic {
    public static Typeface mTypeface;
    String path = "";
    Context mContext;

    public HandelGothic(Context context) {
        mContext = context;
    }

    public Typeface getFontFamily() {
        path = mContext.getString(R.string.handel__gothic);
        try {
            if (mTypeface == null)
                mTypeface = Typeface.createFromAsset(mContext.getAssets(), mContext.getString(R.string.handel__gothic));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mTypeface;
    }
}
