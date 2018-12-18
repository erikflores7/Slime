package me.erikflores.SilverSurfer.Location;

public enum Direction {

    IDLE_RIGHT(0, 0), IDLE_LEFT(0, 0), IDLE_UP(0, 0), IDLE_DOWN(0, 0),
    UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

    private int x, y;
    Direction(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

}
