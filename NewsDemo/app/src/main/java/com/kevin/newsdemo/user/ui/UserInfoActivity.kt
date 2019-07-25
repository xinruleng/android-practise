package com.kevin.newsdemo.user.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.test.espresso.idling.CountingIdlingResource
import butterknife.ButterKnife
import com.kevin.newsdemo.R
import com.kevin.newsdemo.base.BaseActivity
import com.kevin.newsdemo.base.BaseResult
import com.kevin.newsdemo.base.toast
import com.kevin.newsdemo.data.User
import com.kevin.newsdemo.data.UserProfile
import com.kevin.newsdemo.user.UserContract
import com.kevin.newsdemo.user.model.UserModel
import com.kevin.newsdemo.user.model.api.ApiClient
import com.kevin.newsdemo.user.model.api.UserApi
import com.kevin.newsdemo.user.presenter.ProfilePresenter
import kotlinx.android.synthetic.main.activity_user_info.*

class UserInfoActivity : BaseActivity(), UserContract.IProfileView {

    private lateinit var mUser: User

    var mIdlingResource: CountingIdlingResource = CountingIdlingResource("CountingIdlingResource")

    private lateinit var mPresenter: UserContract.IProfilePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        setContentView(R.layout.activity_user_info)
        ButterKnife.bind(this)

        var u: User? = intent.getSerializableExtra(USER) as User
        mUser = u!!
        toast(mUser.toString())

        idText.text = "id: " + mUser!!.auth!!.idToken!!
        tokenText.text = "token: " + mUser!!.auth!!.token!!

        val api = ApiClient.instance.newApi(UserApi::class.java)
        val userModel = UserModel(api)
        mPresenter = ProfilePresenter(userModel, this)

        loading(true)
        mIdlingResource.increment()

        mPresenter!!.getProfile(mUser)

    }

    override fun onResume() {
        super.onResume()
        mPresenter!!.subscribe()
    }

    public override fun onPause() {
        super.onPause()
        mPresenter!!.unsubscribe()
    }

    private fun loading(show: Boolean) {
        loading.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun showProfileSucceed(result: BaseResult<UserProfile>) {
        mIdlingResource.decrement()
        nameTextView.text = "name: " + result.data?.profile!!.name
        genderTextView.text = "gender: " + result.data!!.profile!!.gender
    }

    override fun showProfileFailed(result: BaseResult<UserProfile>) {
        mIdlingResource.decrement()
    }

    override fun setPresenter(presenter: UserContract.IProfilePresenter) {
        mPresenter = presenter
    }

    override fun setLoading(active: Boolean) {
        loading(active)
    }

    companion object {
        private val TAG = "UserInfoActivity"
        val USER = "user"
    }
}
