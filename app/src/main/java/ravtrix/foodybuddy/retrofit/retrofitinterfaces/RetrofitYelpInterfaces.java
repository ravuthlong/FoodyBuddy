package ravtrix.foodybuddy.retrofit.retrofitinterfaces;

import com.google.gson.JsonObject;

import ravtrix.foodybuddy.utils.Helpers;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Ravinder on 1/30/17.
 */

public class RetrofitYelpInterfaces {
    private String token = Helpers.YELP_TOKEN;

    public interface FetchAPlace {
        //@Headers("Authorization: Bearer zeAZ8CvH8Jh5BXgKlgomA80NvCM_dK_rZahtOnEivGo4GSypKzbRQHA5GlbKppo9NHqmYOq0AFZcKh0gKaYifBQQ0EeX7ids2FTfE0GK5PWunl1BHbmUfyZLhieQWHYx")
        @GET("/v3/businesses/search")
        Call<JsonObject> fetchAPlace(@Query("location") String city, @Query(value = "term", encoded = true) String keyword);
    }
}
