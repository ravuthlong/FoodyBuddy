package ravtrix.foodybuddy.network.retrofitrequests;

import ravtrix.foodybuddy.network.retrofitinterfaces.RetrofitPhotoInterfaces;
import ravtrix.foodybuddy.utils.NetworkUtil;
import retrofit2.Retrofit;

/**
 * Created by Ravinder on 3/24/17.
 */

public class RetrofitPhoto {
    private Retrofit retrofit;

    public RetrofitPhoto() {
        retrofit = NetworkUtil.buildRetrofitIMGUR();
    }

    public RetrofitPhotoInterfaces.UploadImage uploadImage() {
        return retrofit.create(RetrofitPhotoInterfaces.UploadImage.class);
    }
}
