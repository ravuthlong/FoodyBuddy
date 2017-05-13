package ravtrix.foodybuddy.utils;

import ravtrix.foodybuddy.network.retrofitrequests.RetrofitChat;

/**
 * Created by Ravinder on 5/11/17.
 */

public class RetrofitChatSingleton {
    private static RetrofitChat retrofitChat = new RetrofitChat();

    public static RetrofitChat getInstance() {
        return retrofitChat;
    }
}
