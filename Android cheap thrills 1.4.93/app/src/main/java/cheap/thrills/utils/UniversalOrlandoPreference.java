package cheap.thrills.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cheap.thrills.models.Order;

public class UniversalOrlandoPreference {
    public static final String MOBILE = "mobile";
    public static final String GID = "Gid";
    public static final String TYPE = "type";
    public static final String LOGGEDIN_ORDERID = "loginOrderId";
    //Prefrence  Mode
    public static final String PREF_NAME = "App_PREF";
    public static final int MODE = Context.MODE_PRIVATE;
    public static final String ORDER_ID = "orderId";
    public static final String LOGIN_ID = "login_id";
    /*Prefrences Names*/
    public static final String ONCE_LOGGED_IN = "once_logged_in";
    public static final String IS_LOGIN = "is_login";
    public static final String IS_First_Launch = "isFirstLaunch";
    public static final String USER_NAME = "user_name";
    public static final String SET_LINK = "set_link";
    public static final String TICKET_TYPE = "ticket_type";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_LAST_NAME = "last_name";
    public static final String USER_LAT = "lattitude";
    public static final String USER_LNG = "longitude";
    public static final String CUSTOMER_ID = "customer_id";
    public static final String THEMEPARK_ID = "themepark_id";
    public static final String THEMEPARK_NAME = "ThemePark_Name";

    public static final String TICKET_LIST = "ticket_list";

    public static void writeBoolean(Context context, String key, boolean value) {
        getEditor(context).putBoolean(key, value).commit();
    }

    public static boolean readBoolean(Context context, String key,
                                      boolean defValue) {
        return getPreferences(context).getBoolean(key, defValue);
    }

    public static void writeInteger(Context context, String key, int value) {
        getEditor(context).putInt(key, value).commit();

    }

    public static int readInteger(Context context, String key, int defValue) {
        return getPreferences(context).getInt(key, defValue);
    }

    public static void writeString(Context context, String key, String value) {
        getEditor(context).putString(key, value).commit();

    }

    public static String readString(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }

    public static void writeFloat(Context context, String key, float value) {
        getEditor(context).putFloat(key, value).commit();
    }

    public static float readFloat(Context context, String key, float defValue) {
        return getPreferences(context).getFloat(key, defValue);
    }

    public static void writeLong(Context context, String key, long value) {
        getEditor(context).putLong(key, value).commit();
    }

    public static long readLong(Context context, String key, long defValue) {
        return getPreferences(context).getLong(key, defValue);
    }

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, MODE);
    }

    public static Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    public static void writeListInPref(Context context, List<Order> orderList){
        Gson gson = new Gson();
        String jsonString = gson.toJson(orderList);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(TICKET_LIST, jsonString);
        editor.apply();
    }

    public static List<Order> readList(Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonString = pref.getString(TICKET_LIST,"");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Order>>() {}.getType();
        List<Order> list = gson.fromJson(jsonString, type);
        return list;
    }
}
