package com.kitkat.group.clubs.data;

import java.util.Map;

/**
 * Created by Admin on 14/02/2019.
 */

public class Club {

    private String clubName, clubDescription, clubLocation, clubOwner;
    private boolean isPublic;
    private Map<String, String> timestamp;

    public Club() {

    }

    public Club(String clubName, String clubDescription, String clubLocation, String clubOwner, boolean isPublic, Map<String, String> timestamp) {
        this.clubDescription = clubDescription;
        this.clubName = clubName;
        this.clubLocation = clubLocation;
        this.clubOwner = clubOwner;
        this.isPublic = isPublic;
        this.timestamp = timestamp;
    }

    public String getClubName() {
        return clubName;
    }

    public String getClubDescription() {
        return clubDescription;
    }

    public String getClubLocation() {
        return clubLocation;
    }

    public String getClubOwner() {
        return clubOwner;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public Map<String, String> getTimestamp() { return timestamp; }

}
