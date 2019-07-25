package com.kevin.newsdemo.user.ui

import android.content.Intent
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.kevin.newsdemo.R
import com.kevin.newsdemo.data.Auth
import com.kevin.newsdemo.data.User
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.Serializable

/**
 * Created by kevin on 2019/07/18 12:14.
 */
@RunWith(AndroidJUnit4::class)
class UserInfoActivityTest {
    @Rule
    lateinit var mRule: ActivityTestRule<UserInfoActivity>

    private var mIdlingResource: CountingIdlingResource? = null

    @Before
    fun startMainActivityFromHomeScreen() {
        Log.d(TAG, "startMainActivityFromHomeScreen: ")
        mRule = ActivityTestRule(UserInfoActivity::class.java)
        val user = User(Auth("123456", "98908989089", "34545234234"))
        val intent = Intent(ApplicationProvider.getApplicationContext(), UserInfoActivity::class.java)
        intent.putExtra(UserInfoActivity.USER, user as Serializable)
        var activity = mRule.launchActivity(intent)
        mIdlingResource = activity.mIdlingResource
        IdlingRegistry.getInstance().register(mIdlingResource!!)
        Intents.init()
    }

    @Test
    fun should_show_user_info() {
//        onView(withId(R.id.idText)).check(matches(withText("id: 123456")))
//        onView(withId(R.id.tokenText)).check(matches(withText("token: 98908989089")))
    }

    @Test
    fun should_show_user_profile() {
        onView(withId(R.id.nameTextView)).check(matches(withText("name: John Smith")))
        onView(withId(R.id.genderTextView)).check(matches(withText("gender: male")))
    }


    @After
    fun tearDown() {
        Intents.release()
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource!!)
        }
    }

    companion object {
        private val TAG = "UserInfoActivity"
    }
}
