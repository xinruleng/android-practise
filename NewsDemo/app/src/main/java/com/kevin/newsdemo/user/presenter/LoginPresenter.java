package com.kevin.newsdemo.user.presenter;

import android.util.Log;
import androidx.annotation.NonNull;
import com.kevin.newsdemo.base.BaseResult;
import com.kevin.newsdemo.user.UserContract;
import com.kevin.newsdemo.user.model.UserModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kevin on 2019/07/19 12:34.
 */
public class LoginPresenter implements UserContract.Presenter {
    private static final String TAG = "LoginPresenter";
    private UserModel mUserModel;
    private UserContract.View mLoginView;

    @NonNull
    private CompositeDisposable mCompositeDisposable;

    public LoginPresenter(UserModel mUserModel, UserContract.View mLoginView) {
        this.mUserModel = mUserModel;
        this.mLoginView = mLoginView;
        this.mLoginView.setPresenter(this);
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void login(String name, String password) {
        mLoginView.setLoading(true);
        final Disposable disposable = mUserModel.login(name, password)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(
            o -> {
                mLoginView.setLoading(false);
                if (o.isOK()) {
                    mLoginView.showLoginSucceed(o);
                }
                else {
                    mLoginView.showLoginFailed(o);
                }
            },
            t -> {
                Log.d(TAG, "login: net error");
                System.out.println("login: net error");
                mLoginView.setLoading(false);
                mLoginView.showLoginFailed(BaseResult.error(t));
            }
          );

        mCompositeDisposable.add(disposable);
    }

    @Override
    public void start() {

    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
