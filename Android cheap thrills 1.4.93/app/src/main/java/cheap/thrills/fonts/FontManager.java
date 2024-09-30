package cheap.thrills.fonts;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.text.TextUtils;

import cheap.thrills.UniversalResortApplication;

import java.util.HashMap;
import java.util.Map;

public class FontManager {
    private static FontManager instance;

    private AssetManager mgr;

    private Map<String, Typeface> fonts;

    private FontManager(AssetManager _mgr) {
        mgr = _mgr;
        fonts = new HashMap<String, Typeface>();
    }

    public static void init(AssetManager mgr) {
        instance = new FontManager(mgr);
    }

    public static FontManager getInstance() {
        if (instance == null) {
            // App.getContext() is just one way to get a Context here
            // getContext() is just a method in an Application subclass
            // that returns the application context
            AssetManager assetManager = UniversalResortApplication.getContext().getAssets();
            init(assetManager);
        }
        return instance;
    }

    public Typeface getFont(String asset) {
        if (fonts.containsKey(asset))
            return fonts.get(asset);

        Typeface font = null;

        try {
            font = Typeface.createFromAsset(mgr, asset);
            fonts.put(asset, font);
        } catch (Exception e) {

        }

        if (font == null) {
            try {
                String fixedAsset = fixAssetFilename(asset);
                font = Typeface.createFromAsset(mgr, fixedAsset);
                fonts.put(asset, font);
                fonts.put(fixedAsset, font);
            } catch (Exception e) {

            }
        }

        return font;
    }

    private String fixAssetFilename(String asset) {
        // Empty font filename?
        // Just return it. We can't help.
        if (TextUtils.isEmpty(asset))
            return asset;

        // Make sure that the font ends in '.ttf' or '.ttc'
        if ((!asset.endsWith(".ttf")) && (!asset.endsWith(".ttc")))
            asset = String.format("%s.ttf", asset);

        return asset;
    }
}
