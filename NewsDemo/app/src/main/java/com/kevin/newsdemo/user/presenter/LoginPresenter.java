package com.kevin.newsdemo.user.presenter;

import androidx.annotation.NonNull;
import com.kevin.newsdemo.base.BaseResult;
import com.kevin.newsdemo.base.schedulers.BaseSchedulerProvider;
import com.kevin.newsdemo.user.UserContract;
import com.kevin.newsdemo.user.model.UserModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by kevin on 2019/07/19 12:34.
 */
public class LoginPresenter implements UserContract.Presenter {
    private UserModel mUserModel;
    private UserContract.View mLoginView;
    private BaseSchedulerProvider mSchedulerProvider;

    @NonNull
    private CompositeDisposable mCompositeDisposable;

    public LoginPresenter(UserModel mUserModel, UserContract.View mLoginView, BaseSchedulerProvider mSchedulerProvider) {
        this.mUserModel = mUserModel;
        this.mLoginView = mLoginView;
        this.mLoginView.setPresenter(this);
        this.mSchedulerProvider = mSchedulerProvider;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void login(String name, String password) {
        mLoginView.setLoading(true);
        final Disposable disposable = mUserModel.login(name, password)
          .subscribeOn(mSchedulerProvider.io())
          .observeOn(mSchedulerProvider.ui())
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
