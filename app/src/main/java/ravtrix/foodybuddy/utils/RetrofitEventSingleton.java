package ravtrix.foodybuddy.utils;

import ravtrix.foodybuddy.network.retrofitrequests.RetrofitEvent;

/**
 * Created by Ravinder on 3/10/17.
 */

public class RetrofitEventSingleton {

    private static RetrofitEvent retrofitEvent = new RetrofitEvent();

    public static RetrofitEvent getInstance() {
        return retrofitEvent;
    }
}
