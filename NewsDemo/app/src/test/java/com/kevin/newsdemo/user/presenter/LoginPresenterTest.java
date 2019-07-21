package com.kevin.newsdemo.user.presenter;

import com.kevin.newsdemo.base.BaseResult;
import com.kevin.newsdemo.base.ResultCode;
import com.kevin.newsdemo.base.schedulers.ImmediateSchedulerProvider;
import com.kevin.newsdemo.data.Auth;
import com.kevin.newsdemo.data.User;
import com.kevin.newsdemo.user.UserContract;
import com.kevin.newsdemo.user.model.UserModel;
import io.reactivex.Observable;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by kevin on 2019/07/19 12:52.
 */
public class LoginPresenterTest {
    public static final String ID = "123456";
    public static final String TOKEN = "98908989089";
    public static final String REFRESH_TOKEN = "34545234234";

    @Mock
    private UserModel mUserModel;
    @Mock
    private UserContract.View mLoginView;
    private LoginPresenter mLoginPresenter;


    @Before
    public void setupPresenter() {
        MockitoAnnotations.initMocks(this);
        //        mLoginView = mock(UserContract.View.class);
        mLoginPresenter = new LoginPresenter(mUserModel, mLoginView, new ImmediateSchedulerProvider());
        mLoginPresenter.start();
    }

    @Test
    public void should_setPresenter() {
        verify(mLoginView).setPresenter(mLoginPresenter);
    }

    @Test
    public void should_showLoginSucceed_when_loginSucceed() {
        String name = "valid";
        String password = "valid";

        BaseResult<User> result = BaseResult.succeed(new User(new Auth(ID, TOKEN, REFRESH_TOKEN)));
        Observable<BaseResult<User>> observable = Observable.just(result);
        when(mUserModel.login(name, password)).thenReturn(observable);

        mLoginPresenter.login(name, password);

        verify(mLoginView).showLoginSucceed(result);
        verify(mLoginView).setLoading(true);
        verify(mLoginView).setLoading(false);
    }

    @Test
    public void should_showLoginFailed_when_loginFailed() {
        String name = "valid";
        String password = "valid1";

        BaseResult<User> result = BaseResult.error(ResultCode.INVALID_NAME_PASSWORD, new RuntimeException());
        Observable<BaseResult<User>> observable = Observable.just(result);
        when(mUserModel.login(name, password)).thenReturn(observable);

        mLoginPresenter.login(name, password);

        verify(mLoginView).showLoginFailed(result);
        verify(mLoginView).setLoading(true);
        verify(mLoginView).setLoading(false);
    }

    @Test
    public void should_showLoginFailed_when_loginNetError() {
        String name = "valid";
        String password = "valid1";

        final RuntimeException t = new RuntimeException();
        BaseResult<User> result = BaseResult.error(ResultCode.ERROR, t);
        when(mUserModel.login(name, password)).thenReturn(Observable.error(t));
        mLoginPresenter.login(name, password);

        verify(mLoginView).showLoginFailed(result);
        verify(mLoginView).setLoading(true);
        verify(mLoginView).setLoading(false);
    }

}
