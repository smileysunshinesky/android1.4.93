package cheap.thrills.fonts;

import android.content.Context;
import android.graphics.Typeface;

import cheap.thrills.R;


public class ArialDoubleBold {
    Context mContext;
    public static Typeface mTypeface;
    public ArialDoubleBold(Context context){
        mContext = context;
    }

    public Typeface getFontFamily(){
        try{
            if (mTypeface == null)
                mTypeface = Typeface.createFromAsset(mContext.getAssets(),mContext.getString(R.string.arialDouble));
        }catch (Exception e){
            e.printStackTrace();
        }

        return mTypeface;
    }
}
