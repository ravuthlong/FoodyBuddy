package ravtrix.foodybuddy.network.retrofitrequests;

import ravtrix.foodybuddy.network.retrofitinterfaces.RetrofitCommentInterfaces;
import ravtrix.foodybuddy.utils.NetworkUtil;
import retrofit2.Retrofit;

/**
 * Created by Ravinder on 3/27/17.
 */

public class RetrofitComment {

    private Retrofit retrofit;

    public RetrofitComment() {
        retrofit = NetworkUtil.buildRetrofit();
    }

    public RetrofitCommentInterfaces.PostComment postComment() {
        return retrofit.create(RetrofitCommentInterfaces.PostComment.class);
    }

    public RetrofitCommentInterfaces.GetEventComments getEventComments() {
        return retrofit.create(RetrofitCommentInterfaces.GetEventComments.class);
    }
}
