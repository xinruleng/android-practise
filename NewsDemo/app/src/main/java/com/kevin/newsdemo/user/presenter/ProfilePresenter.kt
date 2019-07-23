package com.kevin.newsdemo.user.presenter

import com.kevin.newsdemo.base.BaseResult
import com.kevin.newsdemo.data.User
import com.kevin.newsdemo.data.UserProfile
import com.kevin.newsdemo.user.UserContract
import com.kevin.newsdemo.user.model.UserModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by kevin on 2019/07/19 13:54.
 */
class ProfilePresenter(internal var mUserModel: UserModel, internal var mProfileView: UserContract.IProfileView) : UserContract.IProfilePresenter {
    private val mCompositeDisposable: CompositeDisposable
    override fun getProfile(user: User) {
        mProfileView.setLoading(true)
        mCompositeDisposable.add(mUserModel.getProfile(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { userProfileResult ->
                            mProfileView.setLoading(false)
                            if (userProfileResult.isOK) {
                                mProfileView.showProfileSucceed(userProfileResult)
                            } else {
                                mProfileView.showProfileFailed(userProfileResult)
                            }
                        },
                        { t ->
                            mProfileView.setLoading(false)
                            mProfileView.showProfileFailed(BaseResult.error(t) as BaseResult<UserProfile>)
                        }
                ))
    }

    override fun start() {

    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        mCompositeDisposable.clear()
    }

    init {
        mProfileView.setPresenter(this)
        mCompositeDisposable = CompositeDisposable()
    }
}
