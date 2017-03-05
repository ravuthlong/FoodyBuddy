package ravtrix.foodybuddy.fragments.register;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import ravtrix.foodybuddy.callbacks.OnRetrofitFinished;
import ravtrix.foodybuddy.model.LogInResponse;
import ravtrix.foodybuddy.model.User;
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
    public void register(User user) {

        registerInteractor.registerProcess(user, new OnRetrofitFinished() {
            @Override
            public void onNext(LogInResponse logInResponse) {
                handleResponse(logInResponse);
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


    private void handleResponse(LogInResponse logInResponse) {

        iRegisterView.hideProgressbar();
        iRegisterView.showSnackbar(logInResponse.getMessage());
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
