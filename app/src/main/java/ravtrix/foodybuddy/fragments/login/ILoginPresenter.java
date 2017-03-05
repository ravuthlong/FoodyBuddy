package ravtrix.foodybuddy.fragments.login;

/**
 * Created by Ravinder on 2/19/17.
 */

interface ILoginPresenter {
    void checkUserExist(String email, String password);
    void unSubscribe();
}
