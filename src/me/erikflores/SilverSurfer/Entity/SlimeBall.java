package me.erikflores.SilverSurfer.Entity;

import me.erikflores.SilverSurfer.GameController;
import me.erikflores.SilverSurfer.Location.*;

import java.awt.*;

public class SlimeBall extends Entity {

    private Direction direction;
    private TileController tileController;
    private int speed;

    private static final int damage = 10;

    public SlimeBall(Location spawn, Direction direction, int speed, int offset, boolean isFriendly, TileController tileController){
        super("SlimeBall", spawn, isFriendly);
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
        Tile tile = tileController.getTileIn(new Location(getLocation().getX() + GameController.SIZE / 2,
                                                          getLocation().getY() + GameController.SIZE / 2));
        if(tile == null || tile.isWall()){
            die();
            return;
        }
        for(Entity entity : GameController.getEntities()){
            if(entity.isFriendly() != isFriendly()){
                if(entity.getBounds().intersects(getBounds())){
                    entity.damage(damage);
                    die();
                    return;
                }
            }
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
    public Rectangle getBounds(){
        return new Rectangle(getLocation().getX() + 13, getLocation().getY() + 13, 14, 14);
    }

    @Override
    public int getImageIndex(){
        return 16;
    }

}
