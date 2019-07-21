package com.kevin.newsdemo.user.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.test.espresso.idling.CountingIdlingResource;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.kevin.newsdemo.R;
import com.kevin.newsdemo.base.BaseActivity;
import com.kevin.newsdemo.base.BaseResult;
import com.kevin.newsdemo.base.schedulers.SchedulerProvider;
import com.kevin.newsdemo.data.User;
import com.kevin.newsdemo.data.UserProfile;
import com.kevin.newsdemo.user.UserContract;
import com.kevin.newsdemo.user.model.UserModel;
import com.kevin.newsdemo.user.model.api.ApiClient;
import com.kevin.newsdemo.user.model.api.UserApi;
import com.kevin.newsdemo.user.presenter.ProfilePresenter;

public class UserInfoActivity extends BaseActivity implements UserContract.IProfileView{
    private static final String TAG = "UserInfoActivity";
    public static final String USER = "user";

    private User mUser;

    @BindView(R.id.idText)
    TextView mIdText;
    @BindView(R.id.tokenText)
    TextView mTokenText;
    @BindView(R.id.nameTextView)
    TextView mNameText;
    @BindView(R.id.genderTextView)
    TextView mGenderText;
    @BindView(R.id.loading)
    View mLoading;
    private CountingIdlingResource mIdlingResource;

    private UserContract.IProfilePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);

        mUser = (User) getIntent().getSerializableExtra(USER);

        toast(mUser.toString());

        mIdText.setText("id: " + mUser.getAuth().getIdToken());
        mTokenText.setText("token: " + mUser.getAuth().getToken());

        UserApi api = ApiClient.getInstance().newApi(UserApi.class);
        final UserModel userModel = new UserModel(api);
        mPresenter = new ProfilePresenter(userModel, this, SchedulerProvider.getInstance());

        loading(true);
        getIdlingResource().increment();

        mPresenter.getProfile(mUser);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    private void loading(final boolean show) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLoading.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    public CountingIdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new CountingIdlingResource("CountingIdlingResource");
        }
        return mIdlingResource;
    }

    @Override
    public void showProfileSucceed(BaseResult<UserProfile> result) {
        getIdlingResource().decrement();
        mNameText.setText("name: " + result.getData().getProfile().getName());
        mGenderText.setText("gender: " + result.getData().getProfile().getGender());
    }

    @Override
    public void showProfileFailed(BaseResult<UserProfile> result) {
        getIdlingResource().decrement();
    }

    @Override
    public void setPresenter(UserContract.IProfilePresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setLoading(boolean active) {
        loading(active);
    }
}
