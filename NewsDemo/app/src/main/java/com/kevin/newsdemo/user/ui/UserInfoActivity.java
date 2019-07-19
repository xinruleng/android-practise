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
import com.kevin.newsdemo.base.CallBack;
import com.kevin.newsdemo.base.ResultCode;
import com.kevin.newsdemo.data.User;
import com.kevin.newsdemo.data.UserProfile;
import com.kevin.newsdemo.user.model.UserModel;

public class UserInfoActivity extends BaseActivity {
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

        loading(true);
        getIdlingResource().increment();
        final UserModel userModel = new UserModel();
        userModel.getProfile(mUser, new CallBack<UserProfile>() {
            @Override
            public void onSuccess(final UserProfile userProfile) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading(false);
                        getIdlingResource().decrement();
                        mNameText.setText("name: " + userProfile.getProfile().getName());
                        mGenderText.setText("gender: " + userProfile.getProfile().getGender());
                    }
                });
            }

            @Override
            public void onFailed(ResultCode code, Throwable throwable) {
                loading(false);
                getIdlingResource().decrement();
            }
        });

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
}
