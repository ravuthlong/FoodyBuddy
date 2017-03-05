package ravtrix.foodybuddy.fragments.login;

import ravtrix.foodybuddy.callbacks.OnRetrofitFinishedJSON;

/**
 * Created by Ravinder on 2/19/17.
 */

interface ILoginInteractor {

    void loginProcess(String email, String password, OnRetrofitFinishedJSON onRetrofitFinished);
    void unSubscribe();
}
