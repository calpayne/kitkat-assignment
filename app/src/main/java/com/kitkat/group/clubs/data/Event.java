package com.kitkat.group.clubs.data;

/**
 * Created by Admin on 19/04/2019.
 */

public class Event {

    private String eventName;
    private String eventDesc;

    public Event() {

    }

    public Event(String eventName, String eventDesc) {
        this.eventName = eventName;
        this.eventDesc = eventDesc;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDesc() {
        return eventDesc;
    }
}
