package ravtrix.foodybuddy.network.retrofitinterfaces;

import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Ravinder on 1/30/17.
 */

public class RetrofitYelpInterfaces {

    public interface FetchPlacesWithFilter {
        @GET("/v3/businesses/search")
        Call<JsonObject> fetchPlacesWithFilter(@QueryMap HashMap<String, String> values);
    }
}
