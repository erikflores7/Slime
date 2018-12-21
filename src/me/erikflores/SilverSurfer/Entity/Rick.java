package me.erikflores.SilverSurfer.Entity;

import me.erikflores.SilverSurfer.GameController;
import me.erikflores.SilverSurfer.Location.Direction;
import me.erikflores.SilverSurfer.Location.Location;
import me.erikflores.SilverSurfer.Location.Tile;
import me.erikflores.SilverSurfer.Location.TileController;

import java.awt.*;

public class Rick extends Entity {

    private int speed;
    private Direction direction;
    private TileController tileController;
    private Player player;

    private double health = 10;
    private int damage = 2;
    private int counter = 0;
    private int cooldown = 0;

    public Rick(Location spawn, int speed, Player player, TileController tileController){
        super("Rick", spawn, false);
        this.speed = speed;
        this.player = player;
        this.tileController = tileController;
        setDirection(Direction.DOWN);
    }


    @Override
    public void tick(){
        if(cooldown == 0) {
            move();
            checkCollision();
            counter++;
        }else{
            cooldown--;
        }
    }

    private void move(){
        Direction[] possibleDirections = {Direction.DOWN, Direction.RIGHT, Direction.LEFT, Direction.UP};
        int x = getLocation().getX();
        int y = getLocation().getY();
        Tile tile = tileController.getTileIn(new Location(x + GameController.SIZE / 2, y + GameController.SIZE / 2)); // Get center

        double bestDistance = 10000;


        for(Direction direction : possibleDirections){
            /*Tile newTile = tileController.getNeighbor(tile, direction);
            if(newTile == null || newTile.isWall()){
                continue;
            }*/
            if(!checkWallCollision(tileController, direction, speed)){
                continue;
            }
            double xDiff = (player.getLocation().getX() - (x + direction.getX() * speed));
            double yDiff = player.getLocation().getY() - (y + direction.getY() * speed);
            double distance = Math.sqrt((xDiff * xDiff) + (yDiff * yDiff));
            if(distance < bestDistance){
                bestDistance = distance;
                setDirection(direction);
            }else if(distance == bestDistance){
                // TODO take random or something
            }
        }
        getLocation().move(getDirection().getX() * speed, getDirection().getY() * speed);
    }

    private Direction getDirection(){
        return this.direction;
    }

    private void setDirection(Direction direction){
        this.direction = direction;
    }

    private void checkCollision(){
        for(Entity entity : GameController.getEntities()){
            if(entity instanceof Player && entity.getBounds().intersects(getBounds())){
                entity.damage(damage);
                cooldown = 10;
            }
        }
    }

    @Override
    public void damage(int damage){
        health -= damage;
        if(health <= 0){
            GameController.addKill();
            GameController.removeEntity(this);
        }
    }

    @Override
    public Rectangle getBounds(){
        return new Rectangle(getLocation().getX(), getLocation().getY(), GameController.SIZE, GameController.SIZE);
    }

    @Override
    public Tile getTile(){ // Center of entity
        return tileController.getTileIn(new Location(getLocation().getX() + GameController.SIZE / 2,
                                                     getLocation().getY() + GameController.SIZE / 2));
    }

    @Override
    public int getImageIndex(){
        if(counter % 32 >= 16){
            return 25;
        }
        return 25;
    }


}
