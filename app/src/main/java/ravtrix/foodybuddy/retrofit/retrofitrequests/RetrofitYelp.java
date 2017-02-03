package ravtrix.foodybuddy.retrofit.retrofitrequests;

import ravtrix.foodybuddy.retrofit.retrofitinterfaces.RetrofitYelpInterfaces;
import ravtrix.foodybuddy.utils.Helpers;
import retrofit2.Retrofit;

/**
 * Created by Ravinder on 1/30/17.
 */

public class RetrofitYelp {

    private Retrofit retrofit;

    public RetrofitYelp() {
        retrofit = Helpers.retrofitBuilder(Helpers.ServerURL.YELP_API_URL);
    }

    public RetrofitYelpInterfaces.FetchAPlace fetchAPlace() {
        return retrofit.create(RetrofitYelpInterfaces.FetchAPlace.class);
    }
}
