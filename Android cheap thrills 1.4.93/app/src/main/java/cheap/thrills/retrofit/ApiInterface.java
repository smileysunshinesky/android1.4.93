package cheap.thrills.retrofit;

import cheap.thrills.models.DyanmicIconText;
import cheap.thrills.models.ImageObject;
import cheap.thrills.models.Order;
import cheap.thrills.models.Profile;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    /*Retrofit Interfaces*/

    //SignUP
    @FormUrlEncoded
    @POST("https://discovercfl.com/app/OrlandoAdmin/SignUp.php")
    Call<Profile> signUpAccount(@Field("first_name") String first_name, @Field("last_name") String last_name, @Field("email") String email, @Field("password") String password, @Field("update_subscription ") int update_subscription, @Field("term_of_service") int term_of_service);

    @FormUrlEncoded
    @POST("api/Addscreenshot.php")
    Call<Profile> notifyingScreenshot(@Field("gid") String gid, @Field("moblie_no") String moblie_no, @Field("login_id") String login_id, @Field("name") String name, @Field("order_id") String order_id, @Field("type") String type);

    //Login
    @FormUrlEncoded
    @POST("https://discovercfl.com/app/OrlandoAdmin/Login/Login.php")
    Call<Profile> signInAccount(@Field("email") String email, @Field("password") String password);

    //Forgot Password
    @FormUrlEncoded
    @POST("https://discovercfl.com/app/OrlandoAdmin/Forgotpassword.php")
    Call<Profile> ForgotPassword(@Field("email") String email);

    //above are from different server
    //walle
    @GET("QrCode/qrCreateModified.php")
    Call<Profile> ticketsList(@Query("mobile") String mobile, @Query("orderID") String orderID);

    @GET("QrCode/ioslogin.php")
    Call<Profile> ticketsListWithStep_legal(@Query("mobile") String mobile, @Query("orderID") String orderID);

    @FormUrlEncoded
    @POST("QrCode/qrCreate.php")
    Call<Profile> mobilelogin(@Field("mobile") String mobile, @Field("orderID") String orderID);

    @GET("api/Getimage.php")
    Call<ImageObject> images();

    @GET("api/Getimage.php")
    Call<List<String>> imagesList();

    @FormUrlEncoded
    @POST("api/SaveLocation.php")
    Call<Profile> savingLocation(@Field("lat") String lat, @Field("user_long") String user_long, @Field("Gid") String Gid);


    @FormUrlEncoded
    @POST("api/DisableUser.php")
    Call<Order> loggingInstantinactiveTickets(@Field("mobile") String mobile, @Field("orderID") String orderID);

    @GET("api/GetMenu.php")
    Call<DyanmicIconText> geticonImage();
}
