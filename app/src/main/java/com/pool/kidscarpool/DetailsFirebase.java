package com.pool.kidscarpool;

public class DetailsFirebase {
    String Carpool;
    String event;
    String location;
    String owner;
    String member_1, member_2, member_3;

    DetailsFirebase() {
    }

    public DetailsFirebase(String carpool, String event, String location, String owner, String member_1, String member_2, String member_3) {
        Carpool = carpool;
        this.event = event;
        this.location = location;
        this.owner = owner;
        this.member_1 = member_1;
        this.member_2 = member_2;
        this.member_3 = member_3;
    }

    public String getCarpool() {
        return Carpool;
    }

    public void setCarpool(String carpool) {
        Carpool = carpool;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getMember_1() {
        return member_1;
    }

    public void setMember_1(String member_1) {
        this.member_1 = member_1;
    }

    public String getMember_2() {
        return member_2;
    }

    public void setMember_2(String member_2) {
        this.member_2 = member_2;
    }

    public String getMember_3() {
        return member_3;
    }

    public void setMember_3(String member_3) {
        this.member_3 = member_3;
    }
}