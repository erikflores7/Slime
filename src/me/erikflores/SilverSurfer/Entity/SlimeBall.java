package me.erikflores.SilverSurfer.Entity;

import me.erikflores.SilverSurfer.GameController;
import me.erikflores.SilverSurfer.Location.*;

public class SlimeBall extends Entity {

    private Direction direction;
    private TileController tileController;
    private int speed;

    public SlimeBall(Location spawn, Direction direction, int speed, int offset, TileController tileController){
        super("SlimeBall", spawn);
        switch (direction){
            case RIGHT:
            case IDLE_RIGHT: this.direction = Direction.RIGHT;
                getLocation().move(0, offset);
                break;
            case LEFT:
            case IDLE_LEFT: this.direction = Direction.LEFT;
                getLocation().move(0, offset);
                break;
            case UP:
            case IDLE_UP: this.direction = Direction.UP;
                getLocation().move(-offset, 0);
                break;
            case DOWN:
            case IDLE_DOWN: this.direction = Direction.DOWN;
                getLocation().move(offset, 0);
                break;
        }
        this.speed = speed;
        this.tileController = tileController;
    }

    @Override
    public void tick(){
        move();
    }

    private void move(){
        getLocation().move(getDirection().getX() * speed, getDirection().getY() * speed);
        checkCollision();
    }

    private void checkCollision(){
        // Check center of ball
        Tile tile = tileController.getTileIn(new Location(getLocation().getX() + 24, getLocation().getY() + 24));
        if(tile == null || tile.isWall()){ // TODO Add enemy collision
            die();
        }
    }

    private void die(){
        GameController.removeEntity(this);
        this.speed = 0;
    }

    private Direction getDirection(){
        return this.direction;
    }

    @Override
    public int getImageIndex(){
        if(this.speed == 0){
            return 63;
        }
        return 16;
    }

}
