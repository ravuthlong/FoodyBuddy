package ravtrix.foodybuddy.network;

/**
 * Created by Emily on 2/6/17.
 */

import ravtrix.foodybuddy.model.LoggedInUser;
import ravtrix.foodybuddy.model.LogInResponse;
import ravtrix.foodybuddy.model.User;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

public interface RetrofitInterface {

    @POST("users")
    Observable<LogInResponse> register(@Body User user);

    @POST("authenticate")
    Observable<LoggedInUser> login();

    @GET("users/{email}")
    Observable<User> getProfile(@Path("email") String email);

    @PUT("users/{email}")
    Observable<LogInResponse> changePassword(@Path("email") String email, @Body User user);

    @POST("users/{email}/password")
    Observable<LogInResponse> resetPasswordInit(@Path("email") String email);

    @POST("users/{email}/password")
    Observable<LogInResponse> resetPasswordFinish(@Path("email") String email, @Body User user);
}