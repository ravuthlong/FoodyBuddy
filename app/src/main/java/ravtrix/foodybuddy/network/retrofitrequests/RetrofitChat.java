package ravtrix.foodybuddy.network.retrofitrequests;

import ravtrix.foodybuddy.network.retrofitinterfaces.RetrofitChatInterface;
import ravtrix.foodybuddy.utils.NetworkUtil;
import retrofit2.Retrofit;

/**
 * Created by Ravinder on 5/11/17.
 */

public class RetrofitChat {

    private Retrofit retrofit;

    public RetrofitChat() {
        retrofit = NetworkUtil.buildRetrofit();
    }

    public RetrofitChatInterface.InsertChat insertChat() {
        return retrofit.create(RetrofitChatInterface.InsertChat.class);
    }

    public RetrofitChatInterface.GetUserChats getUserChats() {
        return retrofit.create(RetrofitChatInterface.GetUserChats.class);
    }
}
