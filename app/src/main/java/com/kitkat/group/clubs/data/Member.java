package com.kitkat.group.clubs.data;

/**
 * Created by Admin on 14/02/2019.
 */

public class Member {

    private String memberRef;
    private String memberName;

    public Member() {

    }

    public Member(String memberRef) {
        this.memberRef = memberRef;
    }

    public Member(String memberRef, String memberName) {
        this.memberName = memberName;
        this.memberRef = memberRef;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getMemberRef() {
        return memberRef;
    }

}
