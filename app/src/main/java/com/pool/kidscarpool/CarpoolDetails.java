package com.pool.kidscarpool;

public class CarpoolDetails {
   // String UID;
    String Name;
    String Event;
    String Location;
    String Owner;

   public CarpoolDetails(){
   }
   public  CarpoolDetails(String name,String event,String location,String owner){
     //   this.UID=id;
        this.Name = name;
        this.Event=event;
        this.Location=location;
        this.Owner=owner;
    }
  /* public String getUID(){
        return UID;
    }*/

    public String getEvent() {
        return Event;
    }

    public String getLocation() {
        return Location;
    }

    public String getOwner() {
        return Owner;
    }

    public String getName() {

        return Name;
    }
}
