package me.erikflores.SilverSurfer.Location;

public class Location {

    private int x, y;

    public Location(){
        this(0, 0);
    }

    public Location(Location location){
        this(location.getX(), location.getY());
    }

    public Location(int x, int y){
        setX(x);
        setY(y);
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public void move(int deltaX, int deltaY){
        setX(getX() + deltaX);
        setY(getY() + deltaY);
    }

    @Override
    public String toString(){
        return "(" + getX() + ", " + getY() + ")";
    }

}
