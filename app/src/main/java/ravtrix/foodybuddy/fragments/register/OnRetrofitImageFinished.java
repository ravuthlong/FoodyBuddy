package ravtrix.foodybuddy.fragments.register;

import ravtrix.foodybuddy.model.RegisterResponse;

/**
 * Created by Ravinder on 4/6/17.
 */

public interface OnRetrofitImageFinished {
    void onCompleted();
    void onError(Throwable e);
    void onNext(RegisterResponse registerResponse);
}