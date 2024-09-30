package cheap.thrills.utils;

import android.app.Activity;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;

import cheap.thrills.R;
import cheap.thrills.activities.WalletActivity;

import java.io.File;

public abstract class ScreenShotContentObserver extends ContentObserver {
    private Activity context;
    private boolean isFromEdit = false;
    private String previousPath;

    public ScreenShotContentObserver(Handler handler, WalletActivity activity) {
        super(handler);
        this.context = activity;
    }


    @Override
    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        if (!selfChange) {
            context.sendBroadcast(new Intent(context.getString(R.string.screenshotCapturing)));
        }
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, new String[]{
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATA
            }, null, null, null);
            if (cursor != null && cursor.moveToLast()) {
                int displayNameColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                String fileName = cursor.getString(displayNameColumnIndex);
                String path = cursor.getString(dataColumnIndex);
                if (new File(path).lastModified() >= System.currentTimeMillis() - Constants.TIMER) {
                    if (isScreenshot(path) && !isFromEdit && !(previousPath != null && previousPath.equals(path))) {
                        onScreenShot(path, fileName);
                    }
                    previousPath = path;
                    isFromEdit = false;
                } else {
                    cursor.close();
                    return;
                }
            }
        } catch (Throwable t) {
            isFromEdit = true;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        super.onChange(selfChange, uri);
    }

    private boolean isScreenshot(String path) {

        return path != null && path.toLowerCase().contains(context.getString(R.string.screenshot));

    }

    protected abstract void onScreenShot(String path, String fileName);
}
