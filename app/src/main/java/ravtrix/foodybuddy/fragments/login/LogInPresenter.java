package ravtrix.foodybuddy.fragments.login;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import ravtrix.foodybuddy.callbacks.OnRetrofitFinishedJSON;
import ravtrix.foodybuddy.model.LoggedInUser;
import ravtrix.foodybuddy.model.LogInResponse;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by Ravinder on 2/19/17.
 */

class LoginPresenter implements ILoginPresenter {

    private ILoginView iLoginView;
    private LoginInteractor logIninteractor;

    LoginPresenter(ILoginView iLoginView) {
        this.iLoginView = iLoginView;
        this.logIninteractor = new LoginInteractor();
    }

    @Override
    public void checkUserExist(String email, String password) {

        logIninteractor.loginProcess(email, password, new OnRetrofitFinishedJSON() {

            @Override
            public void onNext(LoggedInUser loggedInUser) {
                handleResponse(loggedInUser);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                handleError(e);
            }
        });
    }

    private void handleResponse(LoggedInUser loggedInUser) {

        iLoginView.hideProgressbar();

        iLoginView.storeUser(loggedInUser); // token, name, email

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
