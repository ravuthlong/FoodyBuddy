package ravtrix.foodybuddy.utils;

import ravtrix.foodybuddy.network.retrofitrequests.RetrofitUserInfo;

/**
 * Created by Ravinder on 3/30/17.
 */

public class RetrofitUserInfoSingleton {

    private static RetrofitUserInfo retrofitUserInfo = new RetrofitUserInfo();

    public static RetrofitUserInfo getRetrofitUserInfo() {
        return retrofitUserInfo;
    }
}
