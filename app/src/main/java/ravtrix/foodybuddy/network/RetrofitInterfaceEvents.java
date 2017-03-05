package ravtrix.foodybuddy.network;

import java.util.List;

import ravtrix.foodybuddy.fragments.maineventfrag.recyclerview.model.Event;
import ravtrix.foodybuddy.model.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Ravinder on 3/4/17.
 */

public interface RetrofitInterfaceEvents {

    @GET("event")
    Observable<List<Event>> getEvents();

    @POST("event")
    Observable<Response> postEvent(@Body Event event);
}
