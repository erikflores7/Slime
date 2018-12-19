package me.erikflores.SilverSurfer.Entity;

import me.erikflores.SilverSurfer.GameController;
import me.erikflores.SilverSurfer.Item.BombItem;
import me.erikflores.SilverSurfer.Item.Inventory;
import me.erikflores.SilverSurfer.Item.SlimeItem;
import me.erikflores.SilverSurfer.Location.*;

import java.awt.*;

public class Player extends Entity{

    private TileController tileController;
    private Inventory inventory;

    private Direction direction = Direction.RIGHT;
    private Tile tile;
    private int speed = 2;
    private static final int SIZE = 48;

    private int counter = 0;
    private boolean attacking = false;

    public Player(){
        super("Player", new Location(100, 100));
    }

    public Player(Location spawn, TileController tileController){
        super("Player", spawn);
        this.tileController = tileController;
        tile = tileController.getTileIn(getLocation());
        inventory = new Inventory(4);
        getInventory().addItem(new SlimeItem(6));
        getInventory().addItem(new BombItem(2));
    }

    /**
     * Called every tick (60 times per second)
     */
    @Override
    public void tick(){
        move();
       // System.out.println(toString());
    }

    /**
     *  Moves by SPEED in direction
     */
    public void move(){
        Location newLocation = new Location(getLocation().getX(), getLocation().getY());
        newLocation.move(getDirection().getX() * getSpeed() + (SIZE / 2), // Gets center
                        getDirection().getY() * getSpeed() + (SIZE / 2));

        Tile newTile = tileController.getTileIn(newLocation);
        if (newTile == null || newTile.isWall()){ // Don't move
            return;
        }
        newLocation.move(-(SIZE / 2), -(SIZE / 2)); // Revert back
        setLocation(newLocation);
        setTile(newTile);
    }

    private int getSpeed(){
        return this.speed;
    }

    private void setSpeed(int speed){
        this.speed = speed;
    }

    /**
     * Sets player's direction equal to direction
     * @param direction Direction to set to
     */
    public void setDirection(Direction direction){
        if(direction != getDirection()) {
            this.direction = direction;
            counter = 0;
        }
    }

    /**
     * Once key is released, stop moving in that direction
     *
     * @param direction Direction to remove
     */
    public void removeDirection(Direction direction){
        if(!isIdle() && direction == getDirection()){
            switch(getDirection()){
                case UP: setDirection(Direction.IDLE_UP); break;
                case DOWN: setDirection(Direction.IDLE_DOWN); break;
                case LEFT: setDirection(Direction.IDLE_LEFT); break;
                case RIGHT: setDirection(Direction.IDLE_RIGHT); break;
            }
        }
    }

    /**
     * @return true if player is Idle
     */
    private boolean isIdle(){
        switch(getDirection()){
            case IDLE_RIGHT:
            case IDLE_LEFT:
            case IDLE_UP:
            case IDLE_DOWN: return true;
        }
        return false;
    }

    /**
     * @return image index from sprite image array to use for drawing
     */
    @Override
    public int getImageIndex(){
        int index = 0;
        switch(getDirection()){
            case UP: index = 4; break;
            case DOWN: index = 6; break;
            case LEFT: index = 2; break;
            case IDLE_RIGHT: index = 8; break;
            case IDLE_LEFT: index = 10; break;
            case IDLE_DOWN: index = 14; break;
            case IDLE_UP: index = 12; break;
        }
        if(isIdle()){
            if(counter % 48 >= 24){ // Idle should wait longer to switch sprites
                index++;
            }
        }else{
            if(attacking){
                index += 8; // Show idle animation // SPITTING
            }
            if (counter % 16 >= 8) { // Counter to have gap between animations
                index++;
            }
        }
        counter++;
        return index;
    }

    public Inventory getInventory(){
        return this.inventory;
    }

    public void attack(){

        getInventory().debug();

        if(attacking){
            return;
        }
        if(getInventory().getSelected() instanceof SlimeItem){ // Shoot slimeball
            attacking = true;
            counter = 20; // Start animation with mouth open and have delay
            GameController.addEntity(new SlimeBall(getLocation(), getDirection(), 6, 7, tileController));
            Thread t = new Thread(() -> {
                int attackDelay = 20;
                while(attackDelay > 0){
                    attackDelay--;
                    try { Thread.sleep(12); } catch(InterruptedException e) { /* we tried */}
                }
                attacking = false;
            });
            t.start();
            getInventory().removeItem(inventory.getSelected(), 1);
        }else if(getInventory().getSelected() instanceof BombItem){
            GameController.addEntity(new Bomb(new Location(getLocation().getX() - (getDirection().getX() * 10),
                                                        getLocation().getY() - (getDirection().getY() * 10))));
        }
    }

    public Direction getDirection(){
        return this.direction;
    }

    public Tile getTile(){
        return this.tile;
    }

    private void setTile(Tile tile) {
        this.tile = tile;
    }

    public Rectangle getBounds(){
        return new Rectangle();
    }

    @Override
    public String toString() {
        return super.toString() + " " + getDirection().toString() + " " + getTile();
    }

}
