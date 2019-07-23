package com.kevin.newsdemo.user

import com.kevin.newsdemo.base.BasePresenter
import com.kevin.newsdemo.base.BaseResult
import com.kevin.newsdemo.base.BaseView
import com.kevin.newsdemo.data.User
import com.kevin.newsdemo.data.UserProfile

/**
 * Created by kevin on 2019/07/19 12:29.
 */
interface UserContract {
    interface View : BaseView<Presenter> {
        fun showLoginSucceed(result: BaseResult<User>)

        fun showLoginFailed(result: BaseResult<User>)
    }

    interface Presenter : BasePresenter {
        fun login(name: String, password: String)
    }

    interface IProfileView : BaseView<IProfilePresenter> {
        fun showProfileSucceed(result: BaseResult<UserProfile>)

        fun showProfileFailed(result: BaseResult<UserProfile>)
    }

    interface IProfilePresenter : BasePresenter {
        fun getProfile(user: User)
    }
}
