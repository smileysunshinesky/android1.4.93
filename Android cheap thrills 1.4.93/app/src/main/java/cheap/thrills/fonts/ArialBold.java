package cheap.thrills.fonts;

import android.content.Context;
import android.graphics.Typeface;

import cheap.thrills.R;


public class ArialBold {

    public static Typeface mTypeface;
    String path = "";
    Context mContext;

    public ArialBold(Context context) {
        mContext = context;
    }

    public Typeface getFontFamily() {
        path = mContext.getString(R.string.arialBold);
        try {
            if (mTypeface == null)
                mTypeface = Typeface.createFromAsset(mContext.getAssets(), mContext.getString(R.string.arialBold));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mTypeface;
    }
}
