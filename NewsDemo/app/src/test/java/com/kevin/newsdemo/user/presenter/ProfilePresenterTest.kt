package com.kevin.newsdemo.user.presenter

import com.kevin.newsdemo.base.BaseResult
import com.kevin.newsdemo.base.ResultCode
import com.kevin.newsdemo.data.Auth
import com.kevin.newsdemo.data.Profile
import com.kevin.newsdemo.data.User
import com.kevin.newsdemo.data.UserProfile
import com.kevin.newsdemo.user.UserContract
import com.kevin.newsdemo.user.model.UserModel
import com.kevin.newsdemo.user.model.api.UserApi
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
 * Created by kevin on 2019/07/19 13:54.
 */
@RunWith(PowerMockRunner::class)
@PrepareForTest(UserModel::class, UserProfile::class, UserApi::class)
class ProfilePresenterTest {
    @Rule
    var rxJavaRule = RxJavaRule()

    private lateinit var mUserModel: UserModel
    private lateinit var mProfileView: UserContract.IProfileView
    private lateinit var mProfilePresenter: UserContract.IProfilePresenter

    @Before
    fun setupPresenter() {
        mUserModel = PowerMockito.mock(UserModel::class.java)
        mProfileView = PowerMockito.mock(UserContract.IProfileView::class.java)
        mProfilePresenter = ProfilePresenter(mUserModel, mProfileView)
        mProfilePresenter.start()
    }

    @Test
    fun should_setPresenter() {
        verify(mProfileView).setPresenter(mProfilePresenter)
    }

    @Test
    fun should_showProfileSucceed_when_tokenValid() {
        val user = User(Auth(ID, TOKEN, REFRESH_TOKEN))
        val profile = UserProfile(Profile("name", "male", "http://..."))
        val result = BaseResult.succeed(profile)
        val observable = Observable.just(result)
        `when`(mUserModel.getProfile(user)).thenReturn(observable)

        mProfilePresenter.getProfile(user)

        verify(mProfileView).showProfileSucceed(result)
        verify(mProfileView).setLoading(true)
        verify(mProfileView).setLoading(false)
    }

    @Test
    fun should_showProfileFailed_when_tokenInvalid() {
        val user = User(Auth(ID, TOKEN, REFRESH_TOKEN))
        val result = BaseResult.error(ResultCode.TOKEN_ERROR, RuntimeException())
        val observable = Observable.just(result as BaseResult<UserProfile>)
        `when`(mUserModel.getProfile(user)).thenReturn(observable)

        mProfilePresenter.getProfile(user)

        verify(mProfileView).showProfileFailed(result)
        verify(mProfileView).setLoading(true)
        verify(mProfileView).setLoading(false)
    }

    @Test
    fun should_showProfileFailed_when_netError() {
        val user = User(Auth(ID, TOKEN, REFRESH_TOKEN))

        val t = RuntimeException()
        val result = BaseResult.error(ResultCode.ERROR, t)

        `when`(mUserModel.getProfile(user)).thenReturn(Observable.error(t))


        mProfilePresenter.getProfile(user)

        verify(mProfileView).showProfileFailed(result as BaseResult<UserProfile>)
        verify(mProfileView).setLoading(true)
        verify(mProfileView).setLoading(false)
    }

    @Test
    fun should_subscribe() {
        mProfilePresenter.subscribe()
    }

    @Test
    fun should_unsubscribe() {
        mProfilePresenter.unsubscribe()
    }

    companion object {
        val ID = "123456"
        val TOKEN = "98908989089"
        val REFRESH_TOKEN = "34545234234"
    }
}
