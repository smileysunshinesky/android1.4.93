package cheap.thrills.utils;

import java.util.ArrayList;

import cheap.thrills.models.Order;

public class Constants {

//    public static final String BASE_URL = "https://cheapthrillstix.com/app/appadmin/";//MAIN URL
    public static final String BASE_URL = "https://dbaseconnect.com/cheapthrills/";
    //public static final String BASE_URL = "https://discovercfl.com/app/OrlandoAdmin/";//MAIN URL
   //public static final String BASE_URL = "http://192.168.2.5/OrlandoAdmin/";//MAIN URL
//  public static final String BASE_URL = "https://www.dharmani.com/app/OrlandoAdmin/";

    public static final String KID_COUNT = "kidcount";
    //https://www.dharmani.com/OrlandoAdmin/
    public static final String TICKET_ORDER = "ticketOrder";
    public static final String TICKET_DATE = "Tdate";
    public static final String TICKET_URL = "url";
    public static final String GID = "gid";
    public static final String TICKET_ID = "ticketId";
    public static final String CUSTOMER_NAME = "customer";
    public static final String CODE_IMAGE = "code_image";
    public static final String LIST = "list";
    public final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    public final static String KEY_LOCATION = "location";
    public final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";
    public static final String TAG = "LocationOnOff";
    public static final int REQUEST_CHECK_SETTINGS = 0x1;
    public final static String DEFAULT_IMAGE = "https://www.dharmani.com/appadmin/uploads/1.png";
    public final static int PROGRESS = 2;
    public final static int TIMER = 10000;
    public final static float PROGRESSAMOUNT = 0.5f;
    public static int SPLASH_TIME_OUT = 500;
    public static int SERVER_TIME = 500;
    public static String VISIBILITYCHECK = "visibilty";
    public static String TYPE = "type";
    public static String MOBILE = "mobile";


    public static ArrayList<Order> orderList = new ArrayList<>();
}
