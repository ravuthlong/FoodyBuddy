package ravtrix.foodybuddy.fragments.register;

import ravtrix.foodybuddy.model.User;

/**
 * Created by Ravinder on 2/19/17.
 */

interface IRegisterInteractor {

    /**
     *
     * @param user
     * @param bitmap
     * @param onRetrofitFinished
     */
    void registerProcess(User user, String bitmap, OnRetrofitImageFinished onRetrofitFinished);

    /**
     *
     * @param userID                        - Unique ID of the user
     * @param bitmap                        - String bitmap version of the image
     */
    void uploadImageIMGUR(int userID, String bitmap, OnRetrofitImageFinished onRetrofitImageFinished);

    /**
     * Un-subscribe from CompositeSubscription
     */
    void unsubscribe();
}
