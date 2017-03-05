package ravtrix.foodybuddy.callbacks;

import ravtrix.foodybuddy.model.LoggedInUser;

/**
 * Created by Ravinder on 3/4/17.
 */

public interface OnRetrofitFinishedJSON {

    void onCompleted();
    void onNext(LoggedInUser loggedInUser);
    void onError(Throwable e);
}
