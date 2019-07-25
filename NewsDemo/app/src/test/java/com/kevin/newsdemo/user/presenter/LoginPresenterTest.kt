package com.kevin.newsdemo.user.presenter

import com.kevin.newsdemo.base.BaseResult
import com.kevin.newsdemo.base.ResultCode
import com.kevin.newsdemo.data.Auth
import com.kevin.newsdemo.data.User
import com.kevin.newsdemo.user.UserContract
import com.kevin.newsdemo.user.model.UserModel
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

/**
 * Created by kevin on 2019/07/19 12:52.
 */
@RunWith(PowerMockRunner::class)
@PrepareForTest(UserModel::class)
class LoginPresenterTest {

    @Rule
    var rxJavaRule = RxJavaRule()

    private lateinit var mUserModel: UserModel
    private lateinit var mLoginView: UserContract.View
    private lateinit var mLoginPresenter: LoginPresenter


    @Before
    fun setupPresenter() {
        mUserModel = PowerMockito.mock(UserModel::class.java)
        mLoginView = PowerMockito.mock(UserContract.View::class.java)
        mLoginPresenter = LoginPresenter(mUserModel, mLoginView)
        mLoginPresenter!!.start()
    }

    @Test
    fun should_setPresenter() {
        verify(mLoginView).setPresenter(mLoginPresenter!!)
    }

    @Test
    fun should_showLoginSucceed_when_loginSucceed() {
        val name = "valid"
        val password = "valid"

        val result = BaseResult.succeed(User(Auth(ID, TOKEN, REFRESH_TOKEN)))
        val observable = Observable.just(result)
        `when`(mUserModel!!.login(name, password)).thenReturn(observable)

        mLoginPresenter!!.login(name, password)

        verify(mLoginView).showLoginSucceed(result)
        verify(mLoginView).setLoading(true)
        verify(mLoginView).setLoading(false)
    }

    @Test
    fun should_showLoginFailed_when_loginFailed() {
        val name = "valid"
        val password = "valid1"

        val result = BaseResult.error(ResultCode.INVALID_NAME_PASSWORD, RuntimeException())
        val observable = Observable.just<BaseResult<User>>(result as BaseResult<User>)
        `when`(mUserModel!!.login(name, password)).thenReturn(observable)

        mLoginPresenter!!.login(name, password)

        verify(mLoginView).showLoginFailed(result)
        verify(mLoginView).setLoading(true)
        verify(mLoginView).setLoading(false)
    }

    @Test
    fun should_showLoginFailed_when_loginNetError() {
        val name = "valid"
        val password = "valid1"

        val t = RuntimeException()
        val result = BaseResult.error(ResultCode.ERROR, t)
        `when`(mUserModel!!.login(name, password)).thenReturn(Observable.error(t))
        mLoginPresenter!!.login(name, password)

        verify(mLoginView).showLoginFailed(result as BaseResult<User>)
        verify(mLoginView).setLoading(true)
        verify(mLoginView).setLoading(false)
    }

    @Test
    fun should_subscribe() {
        mLoginPresenter!!.subscribe()
    }

    @Test
    fun should_unsubscribe() {
        mLoginPresenter!!.unsubscribe()
    }

    companion object {
        val ID = "123456"
        val TOKEN = "98908989089"
        val REFRESH_TOKEN = "34545234234"
    }
}
