package com.kevin.newsdemo.user.ui

import android.content.Intent
import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kevin.newsdemo.R
import com.kevin.newsdemo.base.BaseResult
import com.kevin.newsdemo.base.ResultCode
import com.kevin.newsdemo.data.Auth
import com.kevin.newsdemo.data.User
import com.kevin.newsdemo.user.presenter.LoginPresenter
import kotlinx.android.synthetic.main.activity_login.*
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.shadows.ShadowToast

/**
 * Created by kevin on 2019/07/21 14:20.
 */
@RunWith(AndroidJUnit4::class)
class LoginActivityJvmTest {
    private lateinit var loginActivity: LoginActivity
    @Mock
    private lateinit var presenter: LoginPresenter

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        val scenario = ActivityScenario.launch(LoginActivity::class.java)
        scenario.onActivity { act ->
            loginActivity = act
            loginActivity.setPresenter(presenter)
        }
    }

    @After
    fun tearDown() {
        //        loginActivity.setPresenter(null);
    }

    @Test
    fun should_userName_and_password_isEmpty() {
        assertEquals(loginActivity.username.getText().toString(), "")
        assertEquals(loginActivity.password.getText().toString(), "")
    }

    @Test
    fun should_start_UserInfoActivity_when_login_succeed() {
        val NAME = "valid"
        val PASSWORD = "valid"

        loginActivity.username.setText(NAME)
        loginActivity.password.setText(PASSWORD)

        loginActivity.login.performClick()

        verify(presenter).login(NAME, PASSWORD)


    }

    @Test
    fun should_showLoginSucceed() {
        val NAME = "valid"
        val PASSWORD = "valid"

        loginActivity.username.setText(NAME)
        loginActivity.password.setText(PASSWORD)

        loginActivity.findViewById<View>(R.id.login).performClick()

        val result = BaseResult.succeed(User(Auth(ID, TOKEN, REFRESH_TOKEN)))
        loginActivity.showLoginSucceed(result)

        val latestToast = ShadowToast.getTextOfLatestToast()
        Assert.assertTrue(latestToast.startsWith("Login succeed"))

        val expectedIntent = Intent(loginActivity, UserInfoActivity::class.java)
        val actual = shadowOf(RuntimeEnvironment.application).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }

    @Test
    fun should_showLoginFailed() {
        val NAME = "valid"
        val PASSWORD = "valid"

        loginActivity.username.setText(NAME)
        loginActivity.password.setText(PASSWORD)

        loginActivity.findViewById<View>(R.id.login).performClick()

        val result = BaseResult.error(ResultCode.INVALID_NAME_PASSWORD, RuntimeException())
        loginActivity.showLoginFailed(result as BaseResult<User>)

        val latestToast = ShadowToast.getTextOfLatestToast()
        Assert.assertTrue(latestToast.startsWith("Login failed"))
    }

    @Test
    fun should_showLoading() {
        assertEquals(View.GONE, loginActivity.loading.getVisibility())

        loginActivity.setLoading(true)
        assertEquals(View.VISIBLE, loginActivity.loading.getVisibility())

        loginActivity.setLoading(false)
        assertEquals(View.GONE, loginActivity.loading.getVisibility())

    }

    @Test
    fun should_unsubscribe() {
        loginActivity.onPause()
        verify(presenter).unsubscribe()
    }

    companion object {
        val ID = "123456"
        val TOKEN = "98908989089"
        val REFRESH_TOKEN = "34545234234"
    }
}
