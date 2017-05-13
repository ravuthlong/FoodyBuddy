package ravtrix.foodybuddy.network.retrofitinterfaces;

import java.util.List;

import ravtrix.foodybuddy.activities.mainpage.model.EventJoined;
import ravtrix.foodybuddy.fragments.maineventfrag.recyclerview.model.Event;
import ravtrix.foodybuddy.network.networkresponse.CreateEventResponse;
import ravtrix.foodybuddy.network.networkresponse.Response;
import ravtrix.foodybuddy.network.networkmodel.EventParam;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Ravinder on 3/4/17.
 */

public class RetrofitInterfaceEvents {

    public interface  GetEvents {
        @GET("event")
        Observable<List<Event>> getEvents();
    }

    public interface PostEvent {
        @POST("event")
        Observable<CreateEventResponse> postEvent(@Body Event event);
    }

    public interface JoinEvent {
        @POST("event/join")
        Observable<Response> joinEvenet(@Body EventParam eventParam);
    }

    public interface GetEventJoined {
        @GET("event/join/user/{userID}")
        Observable<List<EventJoined>> getEventJoined(@Path("userID") int userID);
    }
}
