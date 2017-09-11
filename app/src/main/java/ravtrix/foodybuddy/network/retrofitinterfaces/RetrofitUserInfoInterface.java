package ravtrix.foodybuddy.network.retrofitinterfaces;

import ravtrix.foodybuddy.activities.editprofileimage.model.ProfileImageModel;
import ravtrix.foodybuddy.network.networkmodel.NewImageParam;
import ravtrix.foodybuddy.network.networkmodel.UserLocationParam;
import ravtrix.foodybuddy.network.networkresponse.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Ravinder on 3/30/17.
 */

public class RetrofitUserInfoInterface {

    public interface GetAUserPhoto {
        @GET("user/profilepic/{userID}")
        Observable<ProfileImageModel> getAUserPhoto(@Path("userID") int userID);
    }

    public interface InsertUserImage {
        @POST("user/profilepic/new")
        Observable<Void> insertAUserImage(@Body NewImageParam newImageParam);
    }

    public interface UpdateUserLocation {
        @POST("user/location")
        Observable<Response> updateUserLocation(@Body UserLocationParam userLocationParam);
    }

    public interface InsertUserLocation {
        @POST("user/location/new")
        Observable<Response> insertUserLocation(@Body UserLocationParam userLocationParam);
    }
}
