package com.kevin.newsdemo.user.ui

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.kevin.newsdemo.base.BaseResult
import com.kevin.newsdemo.base.ResultCode
import com.kevin.newsdemo.data.Auth
import com.kevin.newsdemo.data.Profile
import com.kevin.newsdemo.data.User
import com.kevin.newsdemo.data.UserProfile
import com.kevin.newsdemo.user.presenter.ProfilePresenter
import junit.framework.Assert.assertEquals
import kotlinx.android.synthetic.main.activity_user_info.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.modules.junit4.PowerMockRunnerDelegate
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.Serializable

/**
 * Created by kevin on 2019/07/21 15:31.
 */
@RunWith(PowerMockRunner::class)
@PowerMockRunnerDelegate(RobolectricTestRunner::class)
@PrepareForTest(ProfilePresenter::class)
@Config(
        sdk = [21])
@PowerMockIgnore( "org.mockito.*", "org.robolectric.*", "android.*", "androidx.*" )
class UserInfoActivityJvmTest {
    private lateinit var userInfoActivity: UserInfoActivity

//    @Mock
    private lateinit var presenter: ProfilePresenter

    @Before
    fun init() {
//        MockitoAnnotations.initMocks(this)
        presenter = PowerMockito.mock(ProfilePresenter::class.java)
        val intent = Intent(ApplicationProvider.getApplicationContext<Context>(), UserInfoActivity::class.java)
        val user = User(Auth(ID, TOKEN, REFRESH_TOKEN))
        intent.putExtra(UserInfoActivity.USER, user as Serializable)
        val scenario = ActivityScenario.launch<UserInfoActivity>(intent)
        scenario.onActivity({ act ->
            userInfoActivity = act
            userInfoActivity.setPresenter(presenter)
        })
    }

    @Test
    fun should_showUserId_and_Token() {
        assertEquals("id: $ID", userInfoActivity.idText.getText().toString())
        assertEquals("token: $TOKEN", userInfoActivity.tokenText.getText().toString())
    }

    @Test
    fun should_userName_and_password_isEmpty() {
        assertEquals("name", userInfoActivity.nameTextView.getText().toString())
        assertEquals("gender", userInfoActivity.genderTextView.getText().toString())
    }

    @Test
    fun should_showLoading() {
        assertEquals(View.VISIBLE, userInfoActivity.loading.getVisibility())

        userInfoActivity.setLoading(false)
        assertEquals(View.GONE, userInfoActivity.loading.getVisibility())
    }

    @Test
    fun should_showProfileSucceed() {
        userInfoActivity.mIdlingResource.increment()
        val result = BaseResult.succeed(UserProfile(Profile(PROFILE_NAME, PROFILE_GENDER, PROFILE_AVARTAR)))
        userInfoActivity.showProfileSucceed(result)

        assertEquals("name: $PROFILE_NAME", userInfoActivity.nameTextView.getText().toString())
        assertEquals("gender: $PROFILE_GENDER", userInfoActivity.genderTextView.getText().toString())
    }

    @Test
    fun should_showProfileFailed() {
        userInfoActivity.mIdlingResource.increment()
        val result = BaseResult.error(ResultCode.TOKEN_ERROR, RuntimeException())
        userInfoActivity.showProfileFailed(result as BaseResult<UserProfile>)
    }

    @Test
    fun should_unsubscribe() {
        userInfoActivity.onPause()
        verify(presenter).unsubscribe()
    }

    companion object {
        val ID = "123456"
        val TOKEN = "98908989089"
        val REFRESH_TOKEN = "34545234234"

        val PROFILE_NAME = "John Smith"
        val PROFILE_GENDER = "male"
        val PROFILE_AVARTAR = "http://..."
    }
}
