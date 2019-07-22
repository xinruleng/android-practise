package com.kevin.newsdemo.user.presenter;

import androidx.annotation.NonNull;
import com.kevin.newsdemo.base.BaseResult;
import com.kevin.newsdemo.data.User;
import com.kevin.newsdemo.user.UserContract;
import com.kevin.newsdemo.user.model.UserModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kevin on 2019/07/19 13:54.
 */
public class ProfilePresenter implements UserContract.IProfilePresenter {
    UserModel mUserModel;
    UserContract.IProfileView mProfileView;
    @NonNull
    private CompositeDisposable mCompositeDisposable;
    @Override
    public void getProfile(User user) {
        mProfileView.setLoading(true);
        mCompositeDisposable.add(mUserModel.getProfile(user)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(
            userProfileResult -> {
                mProfileView.setLoading(false);
                if (userProfileResult.isOK()) {
                    mProfileView.showProfileSucceed(userProfileResult);
                }
                else {
                    mProfileView.showProfileFailed(userProfileResult);
                }
            },
            t -> {
                mProfileView.setLoading(false);
                mProfileView.showProfileFailed(BaseResult.error(t));
            }
          ))
        ;
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

    public ProfilePresenter(UserModel mUserModel, UserContract.IProfileView mProfileView) {
        this.mUserModel = mUserModel;
        this.mProfileView = mProfileView;
        mProfileView.setPresenter(this);
        mCompositeDisposable = new CompositeDisposable();
    }
}
