package ravtrix.foodybuddy.callbacks;

import ravtrix.foodybuddy.model.LogInResponse;

/**
 * Created by Ravinder on 2/19/17.
 */

public interface OnRetrofitFinished {
    void onCompleted();
    void onNext(LogInResponse logInResponse);
    void onError(Throwable e);
}
