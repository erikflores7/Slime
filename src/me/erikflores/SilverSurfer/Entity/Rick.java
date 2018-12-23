package me.erikflores.SilverSurfer.Entity;

import me.erikflores.SilverSurfer.GameController;
import me.erikflores.SilverSurfer.Location.*;
import me.erikflores.SilverSurfer.Location.Pathfinder.PathFinder;

import java.awt.*;
import java.util.ArrayList;

public class Rick extends Entity {

    private int speed;
    private Direction direction = null;
    private TileController tileController;
    private Player player;

    private Direction queuedDirection = null;

    private double health;
    private int damage = 2;
    private int counter = 0;
    private int coolDown = 0;
    private int damaged = 0;

    public Rick(Location spawn, int speed, Player player, TileController tileController){
        super("Rick", spawn, false);
        this.speed = speed;
        this.player = player;
        this.tileController = tileController;
        this.health = 6 + (GameController.ROUND * 2);
    }


    @Override
    public void tick(){
        if(coolDown == 0) {
            move();
            checkCollision();
            counter++;
        }else{
            coolDown--;
        }
    }

    private void move(){

        if(queuedDirection != null){
            if(!checkWallCollision(queuedDirection)){ // Check if can finally change direction without colliding
                getLocation().move(getDirection().getX() * speed, getDirection().getY() * speed);
                return;
            }else{ // If so, change direction
                setDirection(queuedDirection);
                queuedDirection = null;
            }
        }

        Direction[] possibleDirections = {Direction.DOWN, Direction.RIGHT, Direction.LEFT, Direction.UP};

        PathFinder path = new PathFinder(tileController, player.getTile(), getTile()); // TODO save this and limit how many checks there are
        ArrayList<Tile> tilePath = path.getPath();

        if(tilePath.size() > 2) { // 2 means it is on the player
            Tile nextTile = tilePath.get(tilePath.size() - 3); // Tile after the starting
            for (Direction direction : possibleDirections) {
                if (tileController.getNeighbor(getTile(), direction).equals(nextTile)) { // Find direction to get to nextTile
                    if(getDirection() == null){ // start
                        setDirection(direction);
                        break;
                    }
                    if(direction != getDirection()){
                        queuedDirection = direction;
                    }
                    break;
                }
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

    /**
     * Check collision with players, damage them, and stop for 10 ticks
     */
    private void checkCollision(){
        for(Entity entity : GameController.getEntities()){
            if(entity instanceof Player && entity.getBounds().intersects(getBounds())){
                entity.damage(damage);
                coolDown = 10;
            }
        }
    }

    /**
     * Adjusted version of Entity's as AI will only try to change direction when it has to unlike player
     *  Allows us to check neighboring tiles for walls
     * @param direction Direction to check
     * @return True if there is no colliding wall
     */
    private boolean checkWallCollision(Direction direction){
        Rectangle bounds = getBounds();
        int width = (int) bounds.getWidth();
        int height = (int) bounds.getHeight();
        Location corner1 = new Location((int) bounds.getX(),(int) bounds.getY());
        Location corner2 = new Location(corner1);

        switch(direction){ // Adjust depending on which way facing
            case UP:
                corner1.move(0, 0);
                corner2.move(width, 0);
                break;
            case DOWN: corner1.move(0, height);
                corner2.move(width, height);
                break;
            case LEFT:
                corner1.move(0, 0);
                corner2.move(0, height);
                break;
            case RIGHT: corner1.move(width, 0);
                corner2.move(width, height);
                break;
        }

        Tile firsTile = tileController.getNeighbor(tileController.getTileIn(corner1), direction);
        if(firsTile == null || firsTile.isWall()){
            return false;
        }
        Tile secondTile = tileController.getNeighbor(tileController.getTileIn(corner2), direction);
        return !(secondTile == null || secondTile.isWall());
    }

    @Override
    public void damage(int damage){
        health -= damage;
        damaged = 4;
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
        if(coolDown > 0){
            return 25;
        }
        if(damaged > 0){
            damaged--;
            return 32;
        }
        if(counter % 32 >= 16){
            return 24;
        }
        return 24;
    }


}
