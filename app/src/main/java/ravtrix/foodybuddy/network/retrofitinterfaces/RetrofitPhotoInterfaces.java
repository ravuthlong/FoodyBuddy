package ravtrix.foodybuddy.network.retrofitinterfaces;

import ravtrix.foodybuddy.model.ImageResponse;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Ravinder on 3/20/17.
 */

public class RetrofitPhotoInterfaces {

    public interface UploadImage {
        @FormUrlEncoded
        @POST("3/image")
        Observable<ImageResponse> uploadImage(@Field("image") String image);
    }
}
