package com.kevin.newsdemo.user.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.test.espresso.idling.CountingIdlingResource
import com.kevin.newsdemo.R
import com.kevin.newsdemo.base.BaseActivity
import com.kevin.newsdemo.base.BaseResult
import com.kevin.newsdemo.base.toast
import com.kevin.newsdemo.data.User
import com.kevin.newsdemo.user.UserContract
import com.kevin.newsdemo.user.model.UserModel
import com.kevin.newsdemo.user.model.api.ApiClient
import com.kevin.newsdemo.user.model.api.UserApi
import com.kevin.newsdemo.user.presenter.LoginPresenter
import com.kevin.newsdemo.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_login.*
import java.io.Serializable

class LoginActivity : BaseActivity(), UserContract.View {

    var mIdlingResource: CountingIdlingResource = CountingIdlingResource("CountingIdlingResource")

    private var mPresenter: UserContract.Presenter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val api = ApiClient.instance.newApi(UserApi::class.java)
        val userModel = UserModel(api)
        mPresenter = LoginPresenter(userModel, this)
        login.setOnClickListener { v -> login() }
    }

    override fun onResume() {
        super.onResume()
        mPresenter!!.subscribe()
    }

    public override fun onPause() {
        super.onPause()
        mPresenter!!.unsubscribe()
    }

    fun login() {
        Log.d(TAG, "login: ")

        loading(true)

        val name = ViewUtils.getText(username)
        val password = ViewUtils.getText(password)

        mIdlingResource.increment()

        mPresenter!!.login(name, password)
    }

    private fun startUserInfoActivity(user: User) {
        val intent = Intent(this, UserInfoActivity::class.java)
        intent.putExtra(UserInfoActivity.USER, user as Serializable)
        startActivity(intent)
    }

    private fun loading(show: Boolean) {
        loading.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun showLoginSucceed(o: BaseResult<User>) {
        mIdlingResource.decrement()
        toast("Login succeed $o")
        startUserInfoActivity(o.data!!)
    }

    override fun showLoginFailed(result: BaseResult<User>) {
        mIdlingResource.decrement()
        toast("Login failed " + result.throwable)
    }

    override fun setPresenter(presenter: UserContract.Presenter) {
        mPresenter = presenter
    }

    override fun setLoading(active: Boolean) {
        loading(active)
    }

    companion object {
        private val TAG = "UserLoginActivity"
    }
}
