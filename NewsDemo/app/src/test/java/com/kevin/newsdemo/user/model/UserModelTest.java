package com.kevin.newsdemo.user.model;

import com.kevin.newsdemo.base.ResultCode;
import com.kevin.newsdemo.data.Auth;
import com.kevin.newsdemo.data.Profile;
import com.kevin.newsdemo.data.UserProfile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by kevin on 2019/07/17 22:20.
 */
public class UserModelTest {
    private static final String TAG = "UserModelTest";
    private UserModel model;


    @Before
    public void init() {
        model = new UserModel();
    }

    @Test
    public void should_return_succeed_when_use_valid_name_and_password() throws InterruptedException {
        String name = "valid";
        String password = "valid";

        model.login(name, password)
          .subscribe(result -> {
                Assert.assertTrue(result.isOK());
                Auth auth = result.getData().getAuth();
                assertEquals("123456", auth.getIdToken());
                assertEquals("98908989089", auth.getToken());
                assertEquals("34545234234", auth.getRefreshToken());
            }
          )
        ;
    }

    @Test
    public void should_return_invalid_when_use_invalid_name_and_password() throws InterruptedException {
        String name = "valid";
        String password = "valid1";
        model.login(name, password)
          .subscribe(
            result -> {
                Assert.assertFalse(result.isOK());
                Assert.assertEquals(ResultCode.INVALID_NAME_PASSWORD, result.getCode());
            })
        ;
    }

    @Test
    public void should_return_profile_when_use_valid_id_and_token() throws Exception {
        String name = "valid";
        String password = "valid";

        model.login(name, password)
          .flatMap(result -> model.getProfile(result.getData()))
          .subscribe(result -> {
              Assert.assertTrue(result.isOK());
              checkUserProfile(result.getData());
          });
    }

    private void checkUserProfile(UserProfile userProfile) {
        Profile profile = userProfile.getProfile();
        assertEquals("John Smith", profile.getName());
        assertEquals("male", profile.getGender());
        assertEquals("", profile.getAvartar());
    }

    @Test
    public void should_return_error_when_use_invalid_id_and_token() throws Exception {
        String name = "valid";
        String password = "valid";
        model.login(name, password)
          .flatMap(result -> {
              final Auth auth = result.getData().getAuth();
              auth.setIdToken(auth.getIdToken() + 1);
              auth.setToken(auth.getToken() + 1);
              return model.getProfile(result.getData());
          })
          .subscribe(result -> {
              Assert.assertFalse(result.isOK());
              Assert.assertEquals(ResultCode.TOKEN_ERROR, result.getCode());
          });
    }
}
