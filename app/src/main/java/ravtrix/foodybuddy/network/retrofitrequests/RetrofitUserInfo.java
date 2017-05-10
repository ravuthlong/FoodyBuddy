package ravtrix.foodybuddy.network.retrofitrequests;

import ravtrix.foodybuddy.network.retrofitinterfaces.RetrofitUserInfoInterface;
import ravtrix.foodybuddy.utils.NetworkUtil;
import retrofit2.Retrofit;

/**
 * Created by Ravinder on 3/30/17.
 */

public class RetrofitUserInfo {

    private Retrofit retrofit;

    public RetrofitUserInfo() {
        retrofit = NetworkUtil.buildRetrofit();
    }

    public RetrofitUserInfoInterface.GetAUserPhoto getAUserPhoto() {
        return retrofit.create(RetrofitUserInfoInterface.GetAUserPhoto.class);
    }

    public RetrofitUserInfoInterface.InsertUserImage insertUserImage() {
        return retrofit.create(RetrofitUserInfoInterface.InsertUserImage.class);
    }
}
