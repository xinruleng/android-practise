package com.kevin.newsdemo.user;

import com.kevin.newsdemo.base.BasePresenter;
import com.kevin.newsdemo.base.BaseResult;
import com.kevin.newsdemo.base.BaseView;
import com.kevin.newsdemo.data.User;
import com.kevin.newsdemo.data.UserProfile;

/**
 * Created by kevin on 2019/07/19 12:29.
 */
public interface UserContract {
    interface View extends BaseView<Presenter> {
        void showLoginSucceed(BaseResult<User> result);

        void showLoginFailed(BaseResult<User> result);
    }

    interface Presenter extends BasePresenter {
        void login(String name, String password);
    }

    interface IProfileView extends BaseView<IProfilePresenter> {
        void showProfileSucceed(BaseResult<UserProfile> result);

        void showProfileFailed(BaseResult<UserProfile> result);
    }

    interface IProfilePresenter extends BasePresenter {
        void getProfile(User user);
    }
}
