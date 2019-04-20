package com.kitkat.group.clubs.view.models;

/**
 * Created by Admin on 20/04/2019.
 */

public class EventViewModel {

    private String clubId;
    private String ownerId;
    private String eventId;
    private String eventName;
    private String eventDate;
    private String eventDesc;

    public EventViewModel() {

    }

    public EventViewModel(String clubId, String ownerId, String eventName, String eventDate, String eventDesc) {
        this.clubId = clubId;
        this.ownerId = ownerId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventDesc = eventDesc;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventDesc() {
        return eventDesc;
    }

}
