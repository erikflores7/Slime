package me.erikflores.SilverSurfer.Entity;
import me.erikflores.SilverSurfer.Location.Direction;
import me.erikflores.SilverSurfer.Location.Location;
import me.erikflores.SilverSurfer.Location.Tile;
import me.erikflores.SilverSurfer.Location.TileController;

import java.awt.*;

public class Entity {

    private String name;
    private Location location;
    private boolean isFriendly;

    public Entity(){
        this("NULL", new Location(0, 0), true) ;
    }

    public Entity(String name, Location startLocation, boolean isFriendly){
        this.name = name;
        this.isFriendly = isFriendly;
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

    public void setLocation(int x, int y){ this.location = new Location(x, y); }

    public void tick(){}

    public int getImageIndex(){
        return 0;
    }

    public boolean isFriendly(){
        return isFriendly;
    }

    public Rectangle getBounds(){
        return null;
    }

    /**
     * @param tileController Tile Controller to check for tiles
     * @param direction Direction attempting to move to
     * @return True if can move, no wall
     */
    public boolean checkWallCollision(TileController tileController, Direction direction, int speed){

        Rectangle bounds = getBounds();
        int width = (int) bounds.getWidth();
        int height = (int) bounds.getHeight();
        Location corner1 = new Location((int) bounds.getX(),(int) bounds.getY());
        corner1.move(direction.getX() * speed, direction.getY() * speed);
        Location corner2 = new Location(corner1);

        switch(direction){
            case UP:
                corner1.move(2, 2);
                corner2.move(width - 2, 2);
                break;
            case DOWN: corner1.move(2, height  - 2);
                corner2.move(width - 2, height - 2);
                break;
            case LEFT:
                corner1.move(2, 2);
                corner2.move(2, height - 2);
                break;
            case RIGHT: corner1.move(width - 2, 2);
                corner2.move(width - 2, height - 2);
                break;
        }

        Tile firsTile = tileController.getTileIn(corner1);
        if(firsTile == null || firsTile.isWall()){
            return false;
        }
        Tile secondTile = tileController.getTileIn(corner2);
        return !(secondTile == null || secondTile.isWall()); // TODO Add a center check or move in a bit closer
    }

    public Tile getTile(){
        return null;
    }

    public void damage(int damage){}

    @Override
    public String toString(){
        return  "(" + getName() + ") " + getLocation().toString();
    }
}
