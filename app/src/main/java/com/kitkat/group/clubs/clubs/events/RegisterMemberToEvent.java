package com.kitkat.group.clubs.clubs.events;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Admin on 19/04/2019.
 */

public class RegisterMemberToEvent {

    private static DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

    public static void register(String clubId, String eventId, String memberId, String memberName) {
        databaseRef.child("clubs").child(clubId).child("events").child(eventId).child("register").child(memberId).setValue(memberName);
    }

    public static void unregister(String clubId, String eventId, String memberId) {
        databaseRef.child("clubs").child(clubId).child("events").child(eventId).child("register").child(memberId).removeValue();
    }

}
