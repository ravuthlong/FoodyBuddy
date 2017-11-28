package ravtrix.foodybuddy.fragments.register;

/**
 * Created by Ravinder on 2/19/17.
 */

interface IRegisterView {
    void hideProgressbar();
    void showSnackbar(String message);
    void showSnackbarInt(int message);
    void storeUser(int userID, double latitude, double longitude, String imageURL);
    void startProfileActivity();

}
