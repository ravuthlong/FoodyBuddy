package ravtrix.foodybuddy.activities.dealslist;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.exception.ErrorHandlingInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.SigningInterceptor;

/**
 * Created by Emily on 3/5/17.
 */

public class YelpAPIFactory {
    private static final String YELP_API_BASE_URL = "https://api.yelp.com";

    private OkHttpClient httpClient;

    public YelpAPIFactory(String consumerKey, String consumerSecret, String token, String tokenSecret) {
        OkHttpOAuthConsumer consumer = new OkHttpOAuthConsumer(consumerKey, consumerSecret);
        consumer.setTokenWithSecret(token, tokenSecret);

        this.httpClient = new OkHttpClient.Builder()
                .addInterceptor(new SigningInterceptor(consumer))
                .addInterceptor(new ErrorHandlingInterceptor())
                .build();
    }

    public YelpAPI createAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getAPIBaseUrl())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(this.httpClient)
                .build();

        return retrofit.create(YelpAPI.class);
    }

    public String getAPIBaseUrl() {
        return YELP_API_BASE_URL;
    }
}
