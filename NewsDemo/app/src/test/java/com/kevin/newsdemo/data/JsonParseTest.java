package com.kevin.newsdemo.data;

import com.google.gson.Gson;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by kevin on 2019/07/17 13:31.
 */
public class JsonParseTest {
    @Test
    public void test_parse_auth() {
        String json = "{\n" +
                "        \"id-token\": \"123456\",\n" +
                "        \"access-token\": \"98908989089\",\n" +
                "        \"refresh-token\": \"34545234234\"\n" +
                "    }";

        Gson gson = new Gson();
        Auth auth = gson.fromJson(json, Auth.class);
        assertEquals("123456", auth.getId());
        assertEquals("98908989089", auth.getToken());
        assertEquals("34545234234", auth.getRefreshToken());
    }

    @Test
    public void test_parse_user() {
        String json = "{\n" +
                "    \"auth\": {\n" +
                "        \"id-token\": \"123456\",\n" +
                "        \"access-token\": \"98908989089\",\n" +
                "        \"refresh-token\": \"34545234234\"\n" +
                "    }\n" +
                "}";

        Gson gson = new Gson();
        User user = gson.fromJson(json, User.class);
        Auth auth = user.getAuth();
        assertEquals("123456", auth.getId());
        assertEquals("98908989089", auth.getToken());
        assertEquals("34545234234", auth.getRefreshToken());
    }

    @Test
    public void test_parse_profile() {
        String json = "{\n" +
                "        \"name\": \"John Smith\",\n" +
                "        \"gender\": \"male\",\n" +
                "        \"avartar\": \"\"\n" +
                "    }";
        Gson gson = new Gson();
        Profile profile = gson.fromJson(json, Profile.class);
        assertEquals("John Smith", profile.getName());
        assertEquals("male", profile.getGender());
        assertEquals("", profile.getAvartar());
    }

    @Test
    public void test_parse_user_profile() {
        String json = "{\n" +
                "    \"profile\": {\n" +
                "        \"name\": \"John Smith\",\n" +
                "        \"gender\": \"male\",\n" +
                "        \"avartar\": \"\"\n" +
                "    }\n" +
                "}";
        Gson gson = new Gson();
        UserProfile userProfile = gson.fromJson(json, UserProfile.class);
        Profile profile = userProfile.getProfile();
        assertEquals("John Smith", profile.getName());
        assertEquals("male", profile.getGender());
        assertEquals("", profile.getAvartar());
    }
}