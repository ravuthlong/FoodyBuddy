package ravtrix.foodybuddy.utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ravinder on 3/24/17.
 */

public class HelpersConnection {

    public static final String YELP_TOKEN = "zeAZ8CvH8Jh5BXgKlgomA80NvCM_dK_rZahtOnEivGo4GSypKzbRQHA5GlbKppo9NHqmYOq0AFZcKh0gKaYifBQQ0EeX7ids2FTfE0GK5PWunl1BHbmUfyZLhieQWHYx";

    public static final class ServerURL {
        public static final String YELP_API_URL = "https://api.yelp.com";
    }

    private HelpersConnection() {}

    private static OkHttpClient okClientYelp() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {

                        // Customizing a default header for Yelp API because Yelp GET request requires an access token as header
                        // before it can validate and fetch from the Yelp database
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .header("Authorization", "Bearer " + YELP_TOKEN)
                                .method(original.method(), original.body())
                                .build();

                        return chain.proceed(request);
                    }
                });
        return httpClient.build();
    }

    /**
     * Create a retrofit object
     * @param serverURL       the url to the server
     */
    public static Retrofit retrofitBuilderYelp(String serverURL)  {

        return new Retrofit.Builder()
                .baseUrl(serverURL)
                .client(okClientYelp())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
