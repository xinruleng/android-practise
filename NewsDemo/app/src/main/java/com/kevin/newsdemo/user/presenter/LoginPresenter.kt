package com.kevin.newsdemo.user.presenter

import android.util.Log
import com.kevin.newsdemo.base.BaseResult
import com.kevin.newsdemo.data.User
import com.kevin.newsdemo.user.UserContract
import com.kevin.newsdemo.user.model.UserModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by kevin on 2019/07/19 12:34.
 */
class LoginPresenter(private val mUserModel: UserModel, private val mLoginView: UserContract.View) : UserContract.Presenter {

    private val mCompositeDisposable: CompositeDisposable

    init {
        this.mLoginView.setPresenter(this)
        mCompositeDisposable = CompositeDisposable()
    }

    override fun login(name: String, password: String) {
        mLoginView.setLoading(true)
        val disposable = mUserModel.login(name, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { o ->
                            mLoginView.setLoading(false)
                            if (o.isOK) {
                                mLoginView.showLoginSucceed(o)
                            } else {
                                mLoginView.showLoginFailed(o)
                            }
                        },
                        { t ->
                            Log.d(TAG, "login: net error")
                            println("login: net error")
                            mLoginView.setLoading(false)
                            mLoginView.showLoginFailed(BaseResult.error(t) as BaseResult<User>)
                        }
                )

        mCompositeDisposable.add(disposable)
    }

    override fun start() {

    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        mCompositeDisposable.clear()
    }

    companion object {
        private val TAG = "LoginPresenter"
    }
}
