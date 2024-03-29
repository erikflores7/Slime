package me.erikflores.SilverSurfer.Location;

import java.awt.*;

public class Tile {

    private Location location;
    private int size;
    private int row, col;
    private boolean isWall = false;

    private Shape bounds;

    private int h;
    private int g;
    private Tile parent;

    public Tile(int row, int col, int size, int collision){
        this.row = row;
        this.col = col;
        this.location = new Location(col * size, row * size);
        this.size = size;
        setWall(collision == 1);

        this.bounds = new Rectangle(getLocation().getX(), getLocation().getY(), size, size);
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

    public Shape getBounds(){
        return bounds;
    }

     @Override
    public String toString(){
        return "Tile: (" + getColumn() + ", " + getRow() + ")";
     }

     @Override
    public boolean equals(Object object){
        if(object instanceof Tile) {
            Tile other = (Tile) object;
            return (getRow() == other.getRow() && getColumn() == other.getColumn());
        }
        return false;
    }

    public void updateH(Tile playerTile){
        this.h = Math.abs((playerTile.getColumn() - getColumn()) + (playerTile.getRow() - getRow()));
    }

    public void updateGCost(int gCost){
        this.g = gCost;
    }

    public int getH(){
        return this.h;
    }

    public int getG(){
        return this.g;
    }

    public int getFCost(){
        return h + g;
    }

    public Tile getParent(){
        return parent;
    }

    public void setParent(Tile parent){
        this.parent = parent;
    }


}
