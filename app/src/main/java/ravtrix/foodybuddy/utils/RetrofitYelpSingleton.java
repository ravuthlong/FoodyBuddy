package ravtrix.foodybuddy.utils;

import ravtrix.foodybuddy.network.retrofitrequests.RetrofitYelp;

/**
 * Created by Ravinder on 1/30/17.
 */

public class RetrofitYelpSingleton {

    private static RetrofitYelp retrofitYelp = new RetrofitYelp();

    public static RetrofitYelp getInstance() {
        return retrofitYelp;
    }
}
