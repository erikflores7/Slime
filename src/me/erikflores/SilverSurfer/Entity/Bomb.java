package me.erikflores.SilverSurfer.Entity;

import me.erikflores.SilverSurfer.GameController;
import me.erikflores.SilverSurfer.Location.Location;
import me.erikflores.SilverSurfer.Location.Tile;
import me.erikflores.SilverSurfer.Location.TileController;

import java.awt.*;
import java.util.ArrayList;

public class Bomb extends Entity{

    private int ticksToExplode = 60;
    private int damage = 25;

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

        ArrayList<Tile> tiles = new ArrayList<>();
        Tile baseTile = tileController.getTileIn(new Location(getLocation().getX() + GameController.SIZE / 2,
                                                        getLocation().getY() + GameController.SIZE / 2)); // CENTER
        for(int x = -1; x < 1; x++){
            for(int y = -1; y < 1; y++){
                Tile neighbor = tileController.getTileIn(baseTile.getColumn() + x, baseTile.getRow() + y);
                if(neighbor == null || neighbor.isWall()){
                    continue;
                }
                tiles.add(neighbor);
            }
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
