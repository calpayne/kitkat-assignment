package com.kitkat.group.clubs.view.models;

import com.kitkat.group.clubs.data.Event;

/**
 * Created by Admin on 20/04/2019.
 */

public class EventViewModel extends Event {

    private String clubId;
    private String ownerId;
    private String eventId;

    public EventViewModel() {

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

    public String getClubId() {
        return clubId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getEventId() {
        return eventId;
    }

}
