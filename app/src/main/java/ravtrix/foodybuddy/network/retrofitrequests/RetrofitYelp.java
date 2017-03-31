package ravtrix.foodybuddy.network.retrofitrequests;

import ravtrix.foodybuddy.network.retrofitinterfaces.RetrofitYelpInterfaces;
import ravtrix.foodybuddy.utils.HelpersConnection;
import retrofit2.Retrofit;

/**
 * Created by Ravinder on 1/30/17.
 */

public class RetrofitYelp {

    private Retrofit retrofit;

    public RetrofitYelp() {
        retrofit = HelpersConnection.retrofitBuilderYelp(HelpersConnection.ServerURL.YELP_API_URL);
    }


    public RetrofitYelpInterfaces.FetchPlacesWithFilter fetchPlacesWithFilter() {
        return retrofit.create(RetrofitYelpInterfaces.FetchPlacesWithFilter.class);
    }
}
