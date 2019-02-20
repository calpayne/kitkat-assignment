package com.kitkat.group.clubs.data;

/**
 * Created by Admin on 14/02/2019.
 */

public class Club {

    private String clubName, clubDescription, clubLocation, clubOwner;
    private boolean isPublic;
    //private List<Member> members;

    public Club() {

    }

    public Club(String clubName, String clubDescription, String clubLocation, String clubOwner, boolean isPublic) {
        this.clubDescription = clubDescription;
        this.clubName = clubName;
        this.clubLocation = clubLocation;
        this.clubOwner = clubOwner;
        this.isPublic = isPublic;
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

}
