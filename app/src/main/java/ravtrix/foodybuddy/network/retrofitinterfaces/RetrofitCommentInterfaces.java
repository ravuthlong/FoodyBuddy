package ravtrix.foodybuddy.network.retrofitinterfaces;

import java.util.List;

import ravtrix.foodybuddy.activities.eventcomments.recyclerview.EventCommentModel;
import ravtrix.foodybuddy.network.networkresponse.Response;
import ravtrix.foodybuddy.network.networkmodel.CommentParam;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Ravinder on 3/27/17.
 */

public class RetrofitCommentInterfaces {

    public interface PostComment {
        @POST("event/comment")
        Observable<Response> postComment(@Body CommentParam commentParam);
    }

    public interface GetEventComments {
        @GET("event/comment/{event_id}")
        Observable<List<EventCommentModel>> getEventComments(@Path("event_id") int event_id);
    }
}
