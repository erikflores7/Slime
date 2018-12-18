package me.erikflores.SilverSurfer.Entity;

import me.erikflores.SilverSurfer.Location.Location;

public class Entity {

    private String name;
    private Location location;

    public Entity(){
        this("NULL", new Location(0, 0)) ;
    }

    public Entity(String name, Location startLocation){
        this.name = name;
        setLocation(startLocation);
    }

    public String getName(){
        return this.name;
    }

    public Location getLocation(){
        return this.location;
    }

    public void setLocation(Location location){
        this.location = new Location(location);
    }

    public void tick(){}

    public int getImageIndex(){
        return 0;
    }

    @Override
    public String toString(){
        return  "(" + getName() + ") " + getLocation().toString();
    }
}
