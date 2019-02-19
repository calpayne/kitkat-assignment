package com.kitkat.group.clubs.data;

/**
 * Created by Admin on 14/02/2019.
 */

public class Club {

    private String clubName;
    private String clubDescription;
    private String clubOwner;
    //private List<Member> members;

    public Club() {

    }

    public Club(String clubName, String clubDescription, String clubOwner) {
        this.clubDescription = clubDescription;
        this.clubName = clubName;
        this.clubOwner = clubOwner;
    }

    public String getClubName() {
        return clubName;
    }

    public String getClubDescription() {
        return clubDescription;
    }

    public String getClubOwner() {
        return clubOwner;
    }

}
