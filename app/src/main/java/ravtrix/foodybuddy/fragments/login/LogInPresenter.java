package ravtrix.foodybuddy.fragments.login;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import ravtrix.foodybuddy.callbacks.OnRetrofitFinishedJSON;
import ravtrix.foodybuddy.location.OnLocationReceived;
import ravtrix.foodybuddy.location.UserLocation;
import ravtrix.foodybuddy.model.LoggedInUser;
import ravtrix.foodybuddy.network.networkresponse.LogInResponse;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by Ravinder on 2/19/17.
 */

class LoginPresenter implements ILoginPresenter {

    private ILoginView iLoginView;
    private LoginInteractor logIninteractor;
    private Activity activity;

    LoginPresenter(ILoginView iLoginView, Activity activity) {
        this.iLoginView = iLoginView;
        this.logIninteractor = new LoginInteractor();
        this.activity = activity;
    }

    @Override
    public void checkUserExist(String email, String password) {

        logIninteractor.loginProcess(email, password, new OnRetrofitFinishedJSON() {

            @Override
            public void onNext(final LoggedInUser loggedInUser) {
                new UserLocation(activity).startLocationListener(new OnLocationReceived() {
                    @Override
                    public void onLocationReceived(double latitude, double longitude) {
                        handleResponse(loggedInUser, latitude, longitude);
                    }
                });
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e(LoginPresenter.class.getSimpleName(), "LOG IN ERROR");
                handleError(e);
            }
        });
    }

    private void handleResponse(LoggedInUser loggedInUser, double latitude, double longitude) {

        iLoginView.hideProgressbar();

        iLoginView.storeUser(loggedInUser, latitude, longitude); // token, name, email

        iLoginView.setEtEmailEmpty();
        iLoginView.setEtPasswordEmpty();
        iLoginView.startProfileActivity();
    }

    private void handleError(Throwable error) {

        iLoginView.hideProgressbar();

        if (error instanceof HttpException) {

            Gson gson = new GsonBuilder().create();

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                LogInResponse logInResponse = gson.fromJson(errorBody,LogInResponse.class);
                iLoginView.showSnackbar(logInResponse.getMessage());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            iLoginView.showSnackbar("Network Error !");
        }
    }

    @Override
    public void unSubscribe() {
        logIninteractor.unSubscribe();
    }
}
