package com.kitkat.group.clubs.data;

/**
 * Created by Admin on 14/02/2019.
 */

public class Club {

    private String clubID, clubName, clubNameSearch, clubDescription, clubLocation, clubOwner;
    private boolean isPublic;

    public Club() {

    }

    public Club(String clubName, String clubDescription, String clubOwner) {
        this.clubDescription = clubDescription;
        this.clubName = clubName;
        clubNameSearch = clubName.toLowerCase();
        this.clubOwner = clubOwner;
    }

    public Club(String clubID, String clubName, String clubDescription, String clubLocation, String clubOwner, boolean isPublic) {
        this.clubID = clubID;
        this.clubDescription = clubDescription;
        this.clubName = clubName;
        clubNameSearch = clubName.toLowerCase();
        this.clubLocation = clubLocation;
        this.clubOwner = clubOwner;
        this.isPublic = isPublic;
    }

    public String getClubID() {
        return clubID;
    }

    public void setClubID(String clubID) {
        this.clubID = clubID;
    }

    public String getClubNameSearch() {
        return clubNameSearch;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
        this.clubNameSearch = clubName.toLowerCase();
    }

    public String getClubDescription() {
        return clubDescription;
    }

    public void setClubDescription(String clubDescription) {
        this.clubDescription = clubDescription;
    }

    public String getClubLocation() {
        return clubLocation;
    }
  
    public void setClubLocation(String clubLocation) {
        this.clubLocation = clubLocation;
    }
  
    public void setClubOwner(String clubOwner) {
        this.clubOwner = clubOwner;
    }

    public String getClubOwner() {
        return clubOwner;
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    @Override
    public String toString(){
        return "Club Name: " + clubName + "\n" +
                "Club Description: " + clubDescription + "\n" +
                "Club Location: " + (clubLocation == null ? "Not set" : clubLocation) + "\n";
    }
}
