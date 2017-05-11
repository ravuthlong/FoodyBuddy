package ravtrix.foodybuddy.localstore;

import android.content.Context;
import android.content.SharedPreferences;

import ravtrix.foodybuddy.model.LoggedInUser;

/**
 * Created by Ravinder on 2/18/17.
 */

public class UserLocalStore {

    private static final String SP_NAME = "userDetails";    // unique name for the local store
    private SharedPreferences userLocalDataStore;           // Object to access local store

    // Creation of new LocalStore object gives access to userLocalDataStore
    public UserLocalStore(Context context) {
        userLocalDataStore = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(LoggedInUser loggedInUser) {
        SharedPreferences.Editor spEditor = userLocalDataStore.edit();
        spEditor.putString("token", loggedInUser.getToken());
        spEditor.putInt("userID", loggedInUser.getUserID());
        spEditor.putString("email", loggedInUser.getEmail());
        spEditor.putBoolean("isLogin", true);
        spEditor.apply();
    }

    // Return current logged in user if exist
    public LoggedInUser getLoggedInUser() {
        String token = userLocalDataStore.getString("token", "");
        int userID = userLocalDataStore.getInt("userID", 0);
        String email = userLocalDataStore.getString("email", "");
        return new LoggedInUser(token, userID, email);
    }

    // Check if user is logged in or not
    public boolean isUserLoggedIn() {
        return userLocalDataStore.getBoolean("isLogin", false);
    }


    public void changeCity(String city) {
        SharedPreferences.Editor spEditor = userLocalDataStore.edit();
        spEditor.putString("city", city);
        spEditor.apply();
    }

    public void changeLatitude(double latitude) {
        SharedPreferences.Editor spEditor = userLocalDataStore.edit();
        spEditor.putLong("latitude", Double.doubleToRawLongBits(latitude));
        spEditor.apply();
    }

    public void changeLongitude(double longitude) {
        SharedPreferences.Editor spEditor = userLocalDataStore.edit();
        spEditor.putLong("longitude", Double.doubleToRawLongBits(longitude));
        spEditor.apply();
    }

    // Clear user data when user logs out
    public void clearUserData() {
        SharedPreferences.Editor spEditor = userLocalDataStore.edit();
        spEditor.clear();
        spEditor.apply();
    }
}
