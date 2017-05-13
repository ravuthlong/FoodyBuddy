package ravtrix.foodybuddy.utils;

import ravtrix.foodybuddy.network.retrofitrequests.RetrofitComment;

/**
 * Created by Ravinder on 3/27/17.
 */

public class RetrofitCommentSingleton {

    private static RetrofitComment retrofitComment = new RetrofitComment();

    public static RetrofitComment getInstance() {
        return retrofitComment;
    }
}
