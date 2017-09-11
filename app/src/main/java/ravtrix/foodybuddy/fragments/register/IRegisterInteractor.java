package ravtrix.foodybuddy.fragments.register;

import ravtrix.foodybuddy.callbacks.OnRetrofitFinished;
import ravtrix.foodybuddy.model.User;

/**
 * Created by Ravinder on 2/19/17.
 */

interface IRegisterInteractor {

    /**
     *
     * @param user
     * @param bitmap
     * @param onRetrofitFinishedRegister
     */
    void registerProcess(User user, String bitmap, OnRetrofitFinishedRegister onRetrofitFinishedRegister);

    /**
     *
     * @param userID                        - Unique ID of the user
     * @param bitmap                        - String bitmap version of the image
     */
    void uploadImageIMGUR(int userID, String bitmap, OnRetrofitFinishedRegister onRetrofitFinishedRegister);


    void insertLocation(int userID, double longitude, double latitude, OnRetrofitFinished onRetrofitFinished);

    /**
     * Un-subscribe from CompositeSubscription
     */
    void unsubscribe();
}
