package ravtrix.foodybuddy.fragments.register;

import ravtrix.foodybuddy.callbacks.OnRetrofitFinished;
import ravtrix.foodybuddy.model.LogInResponse;
import ravtrix.foodybuddy.model.User;
import ravtrix.foodybuddy.utils.NetworkUtil;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Ravinder on 2/19/17.
 */

class RegisterInteractor implements IRegisterInteractor {
    private CompositeSubscription mSubscriptions;

    RegisterInteractor() {
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void registerProcess(User user, final OnRetrofitFinished onRetrofitFinished) {
        mSubscriptions.add(NetworkUtil.getRetrofit().register(user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<LogInResponse>() {
                    @Override
                    public void onCompleted() {
                        onRetrofitFinished.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onRetrofitFinished.onError(e);
                    }

                    @Override
                    public void onNext(LogInResponse logInResponse) {
                        onRetrofitFinished.onNext(logInResponse);
                    }
                }));
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.unsubscribe();
    }
}
