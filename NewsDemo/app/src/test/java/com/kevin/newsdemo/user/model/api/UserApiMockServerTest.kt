package com.kevin.newsdemo.user.model.api

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.kevin.newsdemo.data.*
import io.reactivex.observers.TestObserver
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Created by kevin on 2019/07/20 22:37.
 */
class UserApiMockServerTest {

    private val MOCK_USER = User(Auth(ID, TOKEN, REFRESH_TOKEN))
    private val MOCK_USER_PROFILE = UserProfile(Profile(PROFILE_NAME, PROFILE_GENDER, PROFILE_AVARTAR))

    private lateinit var mUserApi: UserApi
    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()

        val dispatcher = object : Dispatcher() {
            @Throws(InterruptedException::class)
            override fun dispatch(request: RecordedRequest): MockResponse {
                val path = request.path
                if ("/user/login" == path) {
                    return if (request.body.toString() == "[text={\"username\":\"valid\",\"password\":\"valid\"}]") {
                        MockResponse()
                                .addHeader("Content-Type", "application/json;charset=utf-8")
                                .addHeader("Cache-Control", "no-cache")
                                .setBody(LOGIN_SUCCEED_RESPONSE)
                    } else {
                        MockResponse()
                                .addHeader("Content-Type", "application/json;charset=utf-8")
                                .setResponseCode(400)
                                .throttleBody(500, 1, TimeUnit.SECONDS)
                    }
                } else if ("/user/profile" == path) {
                    val idToken = request.getHeader("id-token")
                    val accessToken = request.getHeader("access-token")
                    return if (ID == idToken && TOKEN == accessToken) {
                        MockResponse()
                                .addHeader("Content-Type", "application/json;charset=utf-8")
                                .addHeader("Cache-Control", "no-cache")
                                .setBody(PROFILE_SUCCEED_RESPONSE)
                    } else {
                        MockResponse()
                                .addHeader("Content-Type", "application/json;charset=utf-8")
                                .setResponseCode(400)
                                .throttleBody(500, 1, TimeUnit.SECONDS)
                    }

                }
                return MockResponse()
            }
        }

        //        server.enqueue(mockResponse);
        server!!.dispatcher = dispatcher
    }

    @After
    fun tearDown() {
        try {
            server!!.shutdown()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun mockApi(): UserApi {
        val client = OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build()
        val builder = Retrofit.Builder()
                .baseUrl("http://" + server!!.hostName + ":" + server!!.port)
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

        mUserApi = mockApi()

        val observable = mUserApi!!.login(LoginData(name, password))
        val testObserver = TestObserver<User>()
        observable.subscribe(testObserver)

        testObserver.assertValue(MOCK_USER)
    }

    @Test
    fun should_return_invalid_when_use_invalid_name_and_password() {
        val name = "valid"
        val password = "valid1"
        mUserApi = mockApi()

        val observable = mUserApi!!.login(LoginData(name, password))
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
        mUserApi = mockApi()

        val observable = mUserApi!!.getProfile(ID, TOKEN)
        val testObserver = TestObserver<UserProfile>()
        observable.subscribe(testObserver)

        testObserver.assertValue(MOCK_USER_PROFILE)
    }

    @Test
    @Throws(Exception::class)
    fun should_return_error_when_use_invalid_id_and_token() {
        mUserApi = mockApi()

        val observable = mUserApi.getProfile(ID, TOKEN + 1)
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
