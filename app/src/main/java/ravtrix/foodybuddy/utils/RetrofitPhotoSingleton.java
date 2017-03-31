package ravtrix.foodybuddy.utils;

import ravtrix.foodybuddy.network.retrofitrequests.RetrofitPhoto;

/**
 * Created by Ravinder on 3/30/17.
 */

public class RetrofitPhotoSingleton {

    private static RetrofitPhoto retrofitPhoto = new RetrofitPhoto();

    public static RetrofitPhoto getRetrofitPhoto() {
        return retrofitPhoto;
    }
}
