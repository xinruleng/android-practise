package com.kevin.newsdemo.data

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by kevin on 2019/07/17 13:31.
 */
class JsonParseTest {
    @Test
    fun test_parse_auth() {
        val json = "{\n" +
                "        \"id-token\": \"123456\",\n" +
                "        \"access-token\": \"98908989089\",\n" +
                "        \"refresh-token\": \"34545234234\"\n" +
                "    }"

        val gson = Gson()
        val auth = gson.fromJson(json, Auth::class.java)
        assertEquals("123456", auth.idToken)
        assertEquals("98908989089", auth.token)
        assertEquals("34545234234", auth.refreshToken)
    }

    @Test
    fun test_parse_user() {
        val json = "{\n" +
                "    \"auth\": {\n" +
                "        \"id-token\": \"123456\",\n" +
                "        \"access-token\": \"98908989089\",\n" +
                "        \"refresh-token\": \"34545234234\"\n" +
                "    }\n" +
                "}"

        val gson = Gson()
        val user = gson.fromJson(json, User::class.java)
        val auth = user.auth
        assertEquals("123456", auth.idToken)
        assertEquals("98908989089", auth.token)
        assertEquals("34545234234", auth.refreshToken)
    }

    @Test
    fun test_parse_profile() {
        val json = "{\n" +
                "        \"name\": \"John Smith\",\n" +
                "        \"gender\": \"male\",\n" +
                "        \"avartar\": \"\"\n" +
                "    }"
        val gson = Gson()
        val (name, gender, avartar) = gson.fromJson(json, Profile::class.java)
        assertEquals("John Smith", name)
        assertEquals("male", gender)
        assertEquals("", avartar)
    }

    @Test
    fun test_parse_user_profile() {
        val json = "{\n" +
                "    \"profile\": {\n" +
                "        \"name\": \"John Smith\",\n" +
                "        \"gender\": \"male\",\n" +
                "        \"avartar\": \"\"\n" +
                "    }\n" +
                "}"
        val gson = Gson()
        val userProfile = gson.fromJson(json, UserProfile::class.java)
        val profile = userProfile.profile
        assertEquals("John Smith", profile!!.name)
        assertEquals("male", profile.gender)
        assertEquals("", profile.avartar)
    }
}
