package com.kitkat.group.clubs.data;

/**
 * Created by Admin on 11/03/2019.
 */

public class ClubUser {

    private static ClubUser instance;
    private String username;

    private ClubUser() {

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public static ClubUser getInstance() {
        if(instance == null) {
            instance = new ClubUser();
        }

        return instance;
    }
}
