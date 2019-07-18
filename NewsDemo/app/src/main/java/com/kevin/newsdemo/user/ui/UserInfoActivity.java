package com.kevin.newsdemo.user.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.kevin.newsdemo.R;
import com.kevin.newsdemo.base.BaseActivity;
import com.kevin.newsdemo.base.CallBack;
import com.kevin.newsdemo.base.ResultCode;
import com.kevin.newsdemo.data.User;
import com.kevin.newsdemo.data.UserProfile;
import com.kevin.newsdemo.user.model.UserModel;

public class UserInfoActivity extends BaseActivity {
    public static final String USER = "user";

    private User mUser;

    private TextView mIdText;
    private TextView mTokenText;
    private TextView mNameText;
    private TextView mGenderText;
    private View mLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        mUser = (User) getIntent().getSerializableExtra(USER);

        toast(mUser.toString());

        mIdText = findViewById(R.id.idText);
        mTokenText = findViewById(R.id.tokenText);
        mNameText = findViewById(R.id.nameTextView);
        mGenderText = findViewById(R.id.genderTextView);

        mLoading = findViewById(R.id.loading);

        mIdText.setText("id: " + mUser.getAuth().getId());
        mTokenText.setText("token: " + mUser.getAuth().getToken());

        loading(true);

        final UserModel userModel = new UserModel();
        userModel.getProfile(mUser, new CallBack<UserProfile>() {
            @Override
            public void onSuccess(final UserProfile userProfile) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mNameText.setText("name: "+userProfile.getProfile().getName());
                        mGenderText.setText("gender: "+userProfile.getProfile().getGender());
                        loading(false);
                    }
                });
            }

            @Override
            public void onFailed(ResultCode code, Throwable throwable) {
                loading(false);
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
}
