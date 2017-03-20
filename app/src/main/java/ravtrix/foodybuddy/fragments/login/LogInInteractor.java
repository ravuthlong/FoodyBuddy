package ravtrix.foodybuddy.fragments.login;

import ravtrix.foodybuddy.callbacks.OnRetrofitFinishedJSON;
import ravtrix.foodybuddy.model.LoggedInUser;
import ravtrix.foodybuddy.utils.NetworkUtil;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Ravinder on 2/19/17.
 */

class LoginInteractor implements ILoginInteractor {
    private CompositeSubscription mSubscriptions;

    LoginInteractor() {
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void loginProcess(String email, String password, final OnRetrofitFinishedJSON onRetrofitFinished) {
        mSubscriptions.add(NetworkUtil.getRetrofit(email, password).login()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<LoggedInUser>() {
                    @Override
                    public void onCompleted() {
                        onRetrofitFinished.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onRetrofitFinished.onError(e);
                    }

                    @Override
                    public void onNext(LoggedInUser loggedInUser) {
                        onRetrofitFinished.onNext(loggedInUser);
                    }
                }));
    }

    @Override
    public void unSubscribe() {
        mSubscriptions.unsubscribe();
    }
}
