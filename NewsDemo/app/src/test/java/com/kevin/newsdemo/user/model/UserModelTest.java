package com.kevin.newsdemo.user.model;

import com.kevin.newsdemo.base.BaseResult;
import com.kevin.newsdemo.base.ResultCode;
import com.kevin.newsdemo.data.Auth;
import com.kevin.newsdemo.data.Profile;
import com.kevin.newsdemo.data.User;
import com.kevin.newsdemo.data.UserProfile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by kevin on 2019/07/17 22:20.
 */
public class UserModelTest {
    private UserModel model;

    @Before
    public void init() {
        model = new UserModel();
    }

    @Test
    public void should_return_succeed_when_use_valid_name_and_password() {
        String name = "valid";
        String password = "valid";
        try {
            final BaseResult<User> result = model.doLogin(name, password);
            Assert.assertTrue(result.isOK());

            Auth auth = result.getData().getAuth();
            assertEquals("123456", auth.getId());
            assertEquals("98908989089", auth.getToken());
            assertEquals("34545234234", auth.getRefreshToken());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void should_return_invalid_when_use_invalid_name_and_password() {
        String name = "valid";
        String password = "valid1";
        try {
            final BaseResult<User> result = model.doLogin(name, password);
            Assert.assertEquals(ResultCode.INVALID_NAME_PASSWORD, result.getCode());
            Assert.assertNull(result.getData());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void should_return_profile_when_use_valid_id_and_token() {
        String name = "valid";
        String password = "valid";
        try {
            final BaseResult<User> result = model.doLogin(name, password);

            final Auth auth = result.getData().getAuth();
            final BaseResult<UserProfile> profileResult = model.doGetProfile(auth.getId(), auth.getToken());
            Assert.assertTrue(result.isOK());
            Profile profile = profileResult.getData().getProfile();
            assertEquals("John Smith", profile.getName());
            assertEquals("male", profile.getGender());
            assertEquals("", profile.getAvartar());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void should_return_error_when_use_invalid_id_and_token() {
        String name = "valid";
        String password = "valid";
        try {
            final BaseResult<User> result = model.doLogin(name, password);

            final Auth auth = result.getData().getAuth();
            final BaseResult<UserProfile> profileResult = model.doGetProfile(auth.getId() + 1, auth.getToken() + 1);
            Assert.assertEquals(ResultCode.TOKEN_ERROR, profileResult.getCode());
            Assert.assertNull(profileResult.getData());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}