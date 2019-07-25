package com.kevin.newsdemo.user.model

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import com.kevin.newsdemo.base.BaseResult
import com.kevin.newsdemo.base.ResultCode
import com.kevin.newsdemo.data.*
import com.kevin.newsdemo.user.model.api.UserApi
import io.reactivex.Observable
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.create
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import retrofit2.Response

/**
 * Created by kevin on 2019/07/17 22:20.
 */
class UserModelTest {
    private var model: UserModel? = null

    private val MOCK_USER = User(Auth(ID, TOKEN, REFRESH_TOKEN))
    private val MOCK_USER_PROFILE = UserProfile(Profile(PROFILE_NAME, PROFILE_GENDER, PROFILE_AVARTAR))

    @Before
    fun init() {

    }

    @Test
    @Throws(InterruptedException::class)
    fun should_return_succeed_when_use_valid_name_and_password() {
        val name = "valid"
        val password = "valid"

        val mockApi = mock(UserApi::class.java)
        `when`(mockApi.login(LoginData(name, password))).thenReturn(Observable.just(MOCK_USER))

        model = UserModel(mockApi)
        model!!.login(name, password)
                .subscribe({ result ->
                    Assert.assertTrue(result.isOK)
                    Assert.assertEquals(MOCK_USER, result.data)
                },
                        { throwable -> throwable.printStackTrace() }
                )
    }

    @Test
    @Throws(InterruptedException::class)
    fun should_return_invalid_when_use_invalid_name_and_password() {
        val name = "valid"
        val password = "valid1"

        val mockApi = mock(UserApi::class.java)
        val body = create("application/json; charset=utf-8".toMediaType(), "")
        val throwable = HttpException(Response.error<Any>(400, body))

        `when`(mockApi.login(LoginData(name, password))).thenReturn(Observable.error(throwable))

        model = UserModel(mockApi)
        model!!.login(name, password)
                .subscribe { result -> Assert.assertEquals(ResultCode.INVALID_NAME_PASSWORD, result.code) }
    }

    @Test
    @Throws(Exception::class)
    fun should_return_profile_when_use_valid_id_and_token() {
        val name = "valid"
        val password = "valid"

        val mockApi = mock(UserApi::class.java)
        `when`(mockApi.login(LoginData(name, password))).thenReturn(Observable.just(MOCK_USER))
        `when`(mockApi.getProfile(ID, TOKEN)).thenReturn(Observable.just(MOCK_USER_PROFILE))

        model = UserModel(mockApi)

        model!!.login(name, password)
                .flatMap<BaseResult<UserProfile>> { result -> model!!.getProfile(result.data!!) }
                .subscribe({ result ->
                    Assert.assertTrue(result.isOK)
                    Assert.assertEquals(MOCK_USER_PROFILE, result.data)
                })
    }

    @Test
    @Throws(Exception::class)
    fun should_return_error_when_use_invalid_id_and_token() {
        val name = "valid"
        val password = "valid"

        val mockApi = mock(UserApi::class.java)
        `when`(mockApi.login(LoginData(name, password))).thenReturn(Observable.just(MOCK_USER))
        val body = create("application/json; charset=utf-8".toMediaType(), "")
        val throwable = HttpException(Response.error<Any>(400, body))
        `when`(mockApi.getProfile(ID, TOKEN)).thenReturn(Observable.error(throwable))

        model = UserModel(mockApi)

        model!!.login(name, password)
                .flatMap<BaseResult<UserProfile>> { result -> model!!.getProfile(result.data!!) }
                .subscribe({ result ->
                    Assert.assertFalse(result.isOK)
                    Assert.assertEquals(ResultCode.TOKEN_ERROR, result.code)
                })
    }

    companion object {
        private val TAG = "UserModelTest"
        val ID = "123456"
        val TOKEN = "98908989089"
        val REFRESH_TOKEN = "34545234234"

        val PROFILE_NAME = "John Smith"
        val PROFILE_GENDER = "male"
        val PROFILE_AVARTAR = "http://..."
    }
}
