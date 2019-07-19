package com.kevin.newsdemo.data;

import java.util.Objects;

/**
 * Created by kevin on 2019/07/17 14:04.
 */
public class Profile {
    private String name;
    private String gender;
    private String avartar;

    public Profile() {
    }

    public Profile(String name, String gender, String avartar) {
        this.name = name;
        this.gender = gender;
        this.avartar = avartar;
    }

    public String getAvartar() {
        return avartar;
    }


    public String getGender() {

        return gender;
    }


    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Profile))
            return false;
        Profile profile = (Profile) o;
        return Objects.equals(name, profile.name) &&
          Objects.equals(gender, profile.gender) &&
          Objects.equals(avartar, profile.avartar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, gender, avartar);
    }
}
