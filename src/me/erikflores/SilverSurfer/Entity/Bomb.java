package me.erikflores.SilverSurfer.Entity;

import me.erikflores.SilverSurfer.GameController;
import me.erikflores.SilverSurfer.Location.Direction;
import me.erikflores.SilverSurfer.Location.Location;
import me.erikflores.SilverSurfer.Location.Tile;
import me.erikflores.SilverSurfer.Location.TileController;

import java.awt.*;
import java.util.ArrayList;

public class Bomb extends Entity{

    private int ticksToExplode = 60;
    private int radius = 4;
    private int damage = 10;

    private TileController tileController;

    public Bomb(Location spawn, TileController tileController){
        super("Bomb", spawn, true);
        this.tileController = tileController;
    }

    @Override
    public void tick(){
        ticksToExplode--;
        if(ticksToExplode == 0){
            detonate();
        }
    }

    private void detonate(){
        checkCollision();
        GameController.removeEntity(this);
    }

    private void checkCollision(){

        // Check 4 directions and add those tiles, stop if hit a wall
        ArrayList<Tile> tiles = new ArrayList<>();
        Direction[] directions = Direction.values();
        Tile baseTile = tileController.getTileIn(new Location(getLocation().getX() + GameController.SIZE / 2,
                                                        getLocation().getY() + GameController.SIZE / 2)); // CENTER
        Tile currentTile = baseTile;
        for(int i = 4; i < directions.length; i++){
            Direction direction = directions[i];
            for(int j = 0; j < radius; j++){
                currentTile = tileController.getNeighbor(currentTile, direction);
                if(currentTile == null || currentTile.isWall()){ // If hit a wall or null, stop checking in that dir
                    break;
                }
                tiles.add(currentTile);
            }
            currentTile = baseTile;
        }
        tiles.add(baseTile);

        for(Entity entity : GameController.getEntities()){
            if(isFriendly() != entity.isFriendly() && tiles.contains(entity.getTile())){
                entity.damage(damage);
            }
        }
    }


    @Override
    public Rectangle getBounds(){
        return new Rectangle(getLocation().getX() + 13, getLocation().getY() + 13, 14, 14);
    }

    @Override
    public int getImageIndex(){
        if (ticksToExplode <= 4){
            return 19;
        }
        if(ticksToExplode % 10 >= 8){
            return 18;
        }
        return 17;
    }
}
