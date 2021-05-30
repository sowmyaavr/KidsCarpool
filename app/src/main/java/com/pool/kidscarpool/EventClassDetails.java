package com.pool.kidscarpool;

public class EventClassDetails {
    String ename, eevent, elocation, eowner,emember1,emember2,emember3;;
    EventClassDetails() {
    }


    public EventClassDetails(String ename, String eevent, String elocation, String eowner, String emember1, String emember2, String emember3)
    {
        this.ename = ename;
        this.eevent = eevent;
        this.elocation = elocation;
        this.eowner = eowner;
        this.emember1 = emember1;
        this.emember2 = emember2;
        this.emember3 = emember3;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getEevent() {
        return eevent;
    }

    public void setEevent(String eevent) {
        this.eevent = eevent;
    }

    public String getElocation() {
        return elocation;
    }

    public void setElocation(String elocation) {
        this.elocation = elocation;
    }

    public String getEowner() {
        return eowner;
    }

    public void setEowner(String eowner) {
        this.eowner = eowner;
    }

    public String getEmember1() {
        return emember1;
    }

    public void setEmember1(String emember1) {
        this.emember1 = emember1;
    }

    public String getEmember2() {
        return emember2;
    }

    public void setEmember2(String emember2) {
        this.emember2 = emember2;
    }

    public String getEmember3() {
        return emember3;
    }

    public void setEmember3(String emember3) {
        this.emember3 = emember3;
    }
}
