package ravtrix.foodybuddy.fragments.login;

import ravtrix.foodybuddy.model.LoggedInUser;

/**
 * Created by Ravinder on 2/19/17.
 */

interface ILoginView {

    void hideProgressbar();
    void storeUser(LoggedInUser loggedInUser, double latitude, double longitude);
    void setEtEmailEmpty();
    void setEtPasswordEmpty();
    void startProfileActivity();
    void showSnackbar(String message);
}
