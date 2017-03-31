package ravtrix.foodybuddy.utils;

/**
 * Created by Emily on 2/6/17.
 */

import android.util.Base64;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ravtrix.foodybuddy.network.retrofitinterfaces.RetrofitInterface;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

public class NetworkUtil {

    public static RetrofitInterface getRetrofit() {

        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(rxAdapter)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitInterface.class);
    }

    public static RetrofitInterface getRetrofit(String email, String password) {

        String credentials = email + ":" + password;
        final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(),Base64.NO_WRAP);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request original = chain.request();
                Request.Builder builder = original.newBuilder()
                        .addHeader("Authorization", basic)
                        .method(original.method(),original.body());
                return  chain.proceed(builder.build());
            }
        });
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(httpClient.build())
                .addCallAdapterFactory(rxAdapter)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RetrofitInterface.class);
    }

    public static RetrofitInterface getRetrofit(final String token) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder builder = original.newBuilder()
                        .addHeader("x-access-token", token)
                        .method(original.method(),original.body());
                return  chain.proceed(builder.build());
            }
        });

        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(httpClient.build())
                .addCallAdapterFactory(rxAdapter)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RetrofitInterface.class);
    }

    public static Retrofit buildRetrofit() {

        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(rxAdapter)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public static Retrofit buildRetrofitIMGUR() {

        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_IMGUR)
                .client(okClientIMGUR())
                .addCallAdapterFactory(rxAdapter)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static OkHttpClient okClientIMGUR() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {

                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .header("Authorization", "Client-ID " + Constants.IMGUR_CLIENT_ID)
                                .method(original.method(), original.body())
                                .build();

                        return chain.proceed(request);
                    }
                });
        return httpClient.build();
    }
}