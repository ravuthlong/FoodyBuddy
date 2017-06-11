package ravtrix.foodybuddy.callbacks;

import ravtrix.foodybuddy.network.networkresponse.Response;

/**
 * Created by Ravinder on 2/19/17.
 */

public interface OnRetrofitFinished {

    void onCompleted();
    void onNext(Response response);
    void onError(Throwable e);
}
