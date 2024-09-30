package cheap.thrills.fonts;

import android.content.Context;
import android.graphics.Typeface;

import cheap.thrills.R;


public class ArialRegular {


    Context mContext;
    public static Typeface mTypeface;
    public ArialRegular(Context context){
        mContext = context;
    }

    public Typeface getFontFamily(){
        try{
            if (mTypeface == null)
                mTypeface = Typeface.createFromAsset(mContext.getAssets(),mContext.getString(R.string.arialRegular));
        }catch (Exception e){
            e.printStackTrace();
        }

        return mTypeface;
    }
}
