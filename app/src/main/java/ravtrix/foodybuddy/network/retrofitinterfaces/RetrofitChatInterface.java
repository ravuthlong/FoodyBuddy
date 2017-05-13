package ravtrix.foodybuddy.network.retrofitinterfaces;

import java.util.List;

import ravtrix.foodybuddy.network.networkmodel.NewChatParam;
import ravtrix.foodybuddy.network.networkresponse.ChatResponse;
import ravtrix.foodybuddy.network.networkresponse.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Ravinder on 5/11/17.
 */

public class RetrofitChatInterface {

    public interface InsertChat {
        @POST("groupchat")
        Observable<Response> insertChat(@Body NewChatParam newChatParam);
    }

    public interface GetUserChats {
        @GET("groupchat/{user_id}")
        Observable<List<ChatResponse>> getUserChats(@Path("user_id") int user_id);
    }
}
