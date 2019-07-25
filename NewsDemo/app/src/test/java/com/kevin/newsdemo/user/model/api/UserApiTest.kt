package com.kevin.newsdemo.user.model.api

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.kevin.newsdemo.data.*
import com.kevin.newsdemo.user.model.UserModel
import io.reactivex.observers.TestObserver
import okhttp3.OkHttpClient
import org.junit.Assert
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by kevin on 2019/07/20 22:37.
 */
class UserApiTest {

    private val MOCK_USER = User(Auth(ID, TOKEN, REFRESH_TOKEN))
    private val MOCK_USER_PROFILE = UserProfile(Profile(PROFILE_NAME, PROFILE_GENDER, PROFILE_AVARTAR))

    private lateinit var mUserApi: UserApi

    private fun mockApi(resp: String): UserApi {
        return mockApi(200, resp)
    }

    private fun mockApi(code: Int, resp: String): UserApi {
        val client = OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .addNetworkInterceptor(MockInterceptor(code, resp))
                .build()
        val builder = Retrofit.Builder()
                .baseUrl(UserModel.HOST)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

        val retrofit = builder.build()

        return retrofit.create(UserApi::class.java)
    }

    @Test
    fun should_return_succeed_when_use_valid_name_and_password() {
        val name = "valid"
        val password = "valid"

        mUserApi = mockApi(LOGIN_SUCCEED_RESPONSE)

        val observable = mUserApi.login(LoginData(name, password))
        val testObserver = TestObserver<User>()
        observable.subscribe(testObserver)

        testObserver.assertValue(MOCK_USER)
    }

    @Test
    fun should_return_invalid_when_use_invalid_name_and_password() {
        val name = "valid"
        val password = "valid"
        mUserApi = mockApi(400, "")

        val observable = mUserApi.login(LoginData(name, password))
        val testObserver = TestObserver<User>()
        observable.subscribe(testObserver)

        testObserver.assertError({ throwable ->
            Assert.assertTrue(throwable is HttpException)
            val httpException = throwable as HttpException
            Assert.assertEquals(400, httpException.code().toLong())
            true
        })
    }

    @Test
    @Throws(Exception::class)
    fun should_return_profile_when_use_valid_id_and_token() {
        mUserApi = mockApi(PROFILE_SUCCEED_RESPONSE)

        val observable = mUserApi.getProfile(ID, TOKEN)
        val testObserver = TestObserver<UserProfile>()
        observable.subscribe(testObserver)

        testObserver.assertValue(MOCK_USER_PROFILE)
    }

    @Test
    @Throws(Exception::class)
    fun should_return_error_when_use_invalid_id_and_token() {
        mUserApi = mockApi(400, "")

        val observable = mUserApi.getProfile(ID, TOKEN)
        val testObserver = TestObserver<UserProfile>()
        observable.subscribe(testObserver)
        testObserver.assertError({ throwable ->
            Assert.assertTrue(throwable is HttpException)
            val httpException = throwable as HttpException
            Assert.assertEquals(400, httpException.code().toLong())
            true
        })
    }

    companion object {
        val ID = "123456"
        val TOKEN = "98908989089"
        val REFRESH_TOKEN = "34545234234"

        val PROFILE_NAME = "John Smith"
        val PROFILE_GENDER = "male"
        val PROFILE_AVARTAR = "http://..."

        val LOGIN_SUCCEED_RESPONSE = "{\n" +
                "    \"auth\": {\n" +
                "        \"id-token\": \"123456\",\n" +
                "        \"access-token\": \"98908989089\",\n" +
                "        \"refresh-token\": \"34545234234\"\n" +
                "    }\n" +
                "}"


        val PROFILE_SUCCEED_RESPONSE = "{\n" +
                "    \"profile\": {\n" +
                "        \"name\": \"John Smith\",\n" +
                "        \"gender\": \"male\",\n" +
                "        \"avartar\": \"http://...\"\n" +
                "    }\n" +
                "}"
    }
}
