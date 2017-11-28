package ravtrix.foodybuddy.fragments.register;

import ravtrix.foodybuddy.network.networkresponse.RegisterResponse;

/**
 * Created by Ravinder on 4/6/17.
 */

public interface OnRetrofitFinishedRegister {
    void onCompleted();
    void onError(Throwable e);
    void onNext(RegisterResponse registerResponse, String imageURL);
}
