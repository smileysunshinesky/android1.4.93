package cheap.thrills.retrofit;

import cheap.thrills.utils.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {
    private static ApiInterface REST_CLIENT;
    private static Retrofit retrofit;


    static {
        setupRestClient();
    }

    private RestClient() {
    }

    public static ApiInterface get() {
        return REST_CLIENT;
    }

    private static void setupRestClient() {

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        REST_CLIENT = retrofit.create(ApiInterface.class);

    }


    private static OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(Constants.SERVER_TIME, TimeUnit.SECONDS);
        httpClient.readTimeout(Constants.SERVER_TIME, TimeUnit.SECONDS);
        httpClient.addNetworkInterceptor(logging);
        return httpClient.build();
    }

}
