package ravtrix.foodybuddy.fragments.register;

import ravtrix.foodybuddy.model.User;

/**
 * Created by Ravinder on 2/19/17.
 */

interface IRegisterPresenter {
    void register(User user, String imageBitmap, double longitude, double latitude);
    void insertLocation(int userID, double longitude, double latitude, String imageURL);
    //void uploadImage(String bitmap);
    void unsubscribe();
}
