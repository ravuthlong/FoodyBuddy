package ravtrix.foodybuddy.fragments.register;

import ravtrix.foodybuddy.model.RegisterResponse;

/**
 * Created by Ravinder on 3/24/17.
 */

public interface OnImageResponse {

    void onComplete();
    void onError(Throwable e);
    void onNext(RegisterResponse registerResponse);
}
