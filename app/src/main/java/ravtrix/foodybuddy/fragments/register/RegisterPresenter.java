package ravtrix.foodybuddy.fragments.register;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import ravtrix.foodybuddy.callbacks.OnRetrofitFinished;
import ravtrix.foodybuddy.model.User;
import ravtrix.foodybuddy.network.networkresponse.LogInResponse;
import ravtrix.foodybuddy.network.networkresponse.RegisterResponse;
import ravtrix.foodybuddy.network.networkresponse.Response;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by Ravinder on 2/19/17.
 */

class RegisterPresenter implements IRegisterPresenter {

    private IRegisterView iRegisterView;
    private RegisterInteractor registerInteractor;

    RegisterPresenter(IRegisterView iRegisterView) {
        this.iRegisterView = iRegisterView;
        this.registerInteractor = new RegisterInteractor();
    }

    @Override
    public void register(User user, final String imageBitmap, final double longitude, final double latitude) {

        registerInteractor.registerProcess(user, imageBitmap, new OnRetrofitFinishedRegister() {
            @Override
            public void onNext(RegisterResponse registerResponse, String imageURL) {
                insertLocation(registerResponse.getMessage(), longitude, latitude, imageURL);
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e(RegisterPresenter.class.getSimpleName(), "ERROR INSERTING REGISTER");
                handleError(e);
            }
        });
    }

    @Override
    public void insertLocation(final int userID, final double longitude, final double latitude, final String imageURL) {

        registerInteractor.insertLocation(userID, longitude, latitude, new OnRetrofitFinished() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onNext(Response response) {
                handleResponse(userID, latitude, longitude, imageURL);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(RegisterPresenter.class.getSimpleName(), "ERROR INSERTING LOCATION");
            }
        });
    }

    private void handleResponse(int userID, double latitude, double longitude, String imageURL) {

        iRegisterView.hideProgressbar();
        iRegisterView.storeUser(userID, latitude, longitude, imageURL); // pass back userid
        iRegisterView.startProfileActivity();
    }

    private void handleError(Throwable error) {

        iRegisterView.hideProgressbar();

        if (error instanceof HttpException) {

            Gson gson = new GsonBuilder().create();

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                LogInResponse logInResponse = gson.fromJson(errorBody,LogInResponse.class);
                iRegisterView.showSnackbar(logInResponse.getMessage());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            iRegisterView.showSnackbar("Network Error !");
        }
    }

    @Override
    public void unsubscribe() {
        registerInteractor.unsubscribe();
    }
}
