package me.erikflores.SilverSurfer.Location;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Tile {

    private Location location;
    private int size;
    private int row, col;
    private boolean isWall = false;

    private Shape shape;

    public Tile(int row, int col, int size){
        this.row = row;
        this.col = col;
        this.location = new Location(col * size, row * size);
        this.size = size;


        this.shape = new Rectangle2D.Double(getLocation().getX(), getLocation().getY(), size, size);
    }

    public Tile(int row, int col){
        this(row, col, 20);
    }

    public Tile(){
        this(0, 0);
    }

    public int getRow(){
        return this.row;
    }

    public int getColumn(){
        return this.col;
    }

    public Location getLocation(){
        return this.location;
    }

    public boolean isWall(){
        return this.isWall;
    }

    public void setWall(boolean wall){
        this.isWall = wall;
    }

    public Shape getShape(){
        return shape;
    }

     @Override
    public String toString(){
        return "Tile: (" + getColumn() + ", " + getRow() + ")";
     }

}
