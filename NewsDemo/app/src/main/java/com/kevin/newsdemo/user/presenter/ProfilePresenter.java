package com.kevin.newsdemo.user.presenter;

import com.kevin.newsdemo.base.BaseResult;
import com.kevin.newsdemo.base.schedulers.ImmediateSchedulerProvider;
import com.kevin.newsdemo.data.User;
import com.kevin.newsdemo.user.UserContract;
import com.kevin.newsdemo.user.model.UserModel;

/**
 * Created by kevin on 2019/07/19 13:54.
 */
public class ProfilePresenter implements UserContract.IProfilePresenter {
    UserModel mUserModel;
    UserContract.IProfileView mProfileView;
    ImmediateSchedulerProvider mSchedulerProvider;

    @Override
    public void getProfile(User user) {
        mProfileView.setLoading(true);
        mUserModel.getProfile(user)
          .subscribeOn(mSchedulerProvider.io())
          .observeOn(mSchedulerProvider.ui())
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
          )
        ;
    }

    @Override
    public void start() {

    }

    public ProfilePresenter(UserModel mUserModel, UserContract.IProfileView mProfileView, ImmediateSchedulerProvider immediateSchedulerProvider) {
        this.mUserModel = mUserModel;
        this.mProfileView = mProfileView;
        mProfileView.setPresenter(this);
        this.mSchedulerProvider = immediateSchedulerProvider;
    }
}
