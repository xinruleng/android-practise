package com.kevin.newsdemo.user.presenter;

import com.kevin.newsdemo.base.BaseResult;
import com.kevin.newsdemo.base.ResultCode;
import com.kevin.newsdemo.data.Auth;
import com.kevin.newsdemo.data.Profile;
import com.kevin.newsdemo.data.User;
import com.kevin.newsdemo.data.UserProfile;
import com.kevin.newsdemo.user.UserContract;
import com.kevin.newsdemo.user.model.UserModel;
import io.reactivex.Observable;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by kevin on 2019/07/19 13:54.
 */
public class ProfilePresenterTest {
    public static final String ID = "123456";
    public static final String TOKEN = "98908989089";
    public static final String REFRESH_TOKEN = "34545234234";
    @Rule
    public RxJavaRule rxJavaRule = new RxJavaRule();
    
    @Mock
    private UserModel mUserModel;
    @Mock
    private UserContract.IProfileView mProfileView;
    private UserContract.IProfilePresenter mProfilePresenter;

    @Before
    public void setupPresenter() {
        MockitoAnnotations.initMocks(this);
        mProfilePresenter = new ProfilePresenter(mUserModel, mProfileView);
        mProfilePresenter.start();
    }

    @Test
    public void should_setPresenter() {
        verify(mProfileView).setPresenter(mProfilePresenter);
    }

    @Test
    public void should_showProfileSucceed_when_tokenValid() {
        User user = new User(new Auth(ID, TOKEN, REFRESH_TOKEN));
        UserProfile profile = new UserProfile(new Profile("name", "male", "http://..."));
        BaseResult<UserProfile> result = BaseResult.succeed(profile);
        Observable<BaseResult<UserProfile>> observable = Observable.just(result);
        when(mUserModel.getProfile(user)).thenReturn(observable);

        mProfilePresenter.getProfile(user);

        verify(mProfileView).showProfileSucceed(result);
        verify(mProfileView).setLoading(true);
        verify(mProfileView).setLoading(false);
    }

    @Test
    public void should_showProfileFailed_when_tokenInvalid() {
        User user = new User(new Auth(ID, TOKEN, REFRESH_TOKEN));
        BaseResult<UserProfile> result = BaseResult.error(ResultCode.TOKEN_ERROR, new RuntimeException());
        Observable<BaseResult<UserProfile>> observable = Observable.just(result);
        when(mUserModel.getProfile(user)).thenReturn(observable);

        mProfilePresenter.getProfile(user);

        verify(mProfileView).showProfileFailed(result);
        verify(mProfileView).setLoading(true);
        verify(mProfileView).setLoading(false);
    }

    @Test
    public void should_showProfileFailed_when_netError() {
        User user = new User(new Auth(ID, TOKEN, REFRESH_TOKEN));

        final RuntimeException t = new RuntimeException();
        BaseResult<UserProfile> result = BaseResult.error(ResultCode.ERROR, t);

        when(mUserModel.getProfile(user)).thenReturn(Observable.error(t));


        mProfilePresenter.getProfile(user);

        verify(mProfileView).showProfileFailed(result);
        verify(mProfileView).setLoading(true);
        verify(mProfileView).setLoading(false);
    }

    @Test
    public void should_unsubscribe() {
        mProfilePresenter.unsubscribe();
    }
}
