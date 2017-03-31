package ravtrix.foodybuddy.fragments.register;

import ravtrix.foodybuddy.callbacks.OnRetrofitFinished;
import ravtrix.foodybuddy.model.User;

/**
 * Created by Ravinder on 2/19/17.
 */

interface IRegisterInteractor {

    void registerProcess(User user, OnRetrofitFinished onRetrofitFinished);
    //void uploadImage(String image, OnImageResponse onImageResponse);
    void unsubscribe();
}
