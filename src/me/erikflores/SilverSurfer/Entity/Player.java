package me.erikflores.SilverSurfer.Entity;

import me.erikflores.SilverSurfer.GameController;
import me.erikflores.SilverSurfer.Item.BombItem;
import me.erikflores.SilverSurfer.Item.Inventory;
import me.erikflores.SilverSurfer.Item.Item;
import me.erikflores.SilverSurfer.Item.SlimeItem;
import me.erikflores.SilverSurfer.Location.*;

import java.awt.*;

public class Player extends Entity{

    private TileController tileController;
    private Inventory inventory;

    private Direction direction = Direction.IDLE_RIGHT;
    private double health = 100;
    private int reload = 0;
    private Tile tile;
    private int speed = 2;
    private final int SIZE = GameController.SIZE;

    private int counter = 0;
    private boolean attacking = false;

    public Player(Location spawn, TileController tileController){
        super("Player", spawn, true);
        this.tileController = tileController;
        tile = tileController.getTileIn(new Location(getLocation().getX() + SIZE / 2, getLocation().getY() + SIZE / 2));

        inventory = new Inventory(4);

        // Starting items
        getInventory().addItem(new SlimeItem(12));
        getInventory().addItem(new BombItem(2));
    }

    /**
     * Called every tick (60 times per second)
     */
    @Override
    public void tick(){
        move();
        checkCollision();
        if (++reload % 2400 == 0){ // Add 1 slime ball every 2400 ticks
            getInventory().addItem(new SlimeItem(1));
        }
    }

    /**
     *  Moves by SPEED in direction
     */
    private void move(){
        if(isIdle()){
            return;
        }
        if(checkWallCollision(tileController, getDirection(), getSpeed())) { // check if won't collide with wall
            getLocation().move(getDirection().getX() * getSpeed(), getDirection().getY() * getSpeed());
            setTile(tileController.getTileIn(new Location(getLocation().getX() + SIZE / 2, getLocation().getY() + SIZE / 2)));
        }
    }

    /**
     * Checks if colliding with any items to pick up
     */
    private void checkCollision(){
        for(Item item : GameController.getItems()){
            if (item.getBounds().intersects(getBounds())){
                if(!inventory.isFull()) {
                    getInventory().addItem(item);
                    GameController.removeItem(item);
                }
                return;
            }
        }
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
        if(direction == getDirection()){
            return;
        }
        this.direction = direction;
        counter = 0;
    }

    /**
     * Once key is released, stop moving in that direction
     *
     * @param direction Direction to remove
     */
    public void removeDirection(Direction direction){
        if(!isIdle() && direction == getDirection()){
            this.direction = getEquivalent(direction);
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

    public double getHealth(){
        return health;
    }

    @Override
    public void damage(int damage){
        health -= damage;
        if(getHealth() <= 0){

        }
        // TODO change sprite and die if dead
    }

    public void attack(){

        if(attacking){
            return;
        }
        if(getInventory().getSelected() instanceof SlimeItem){ // Shoot slimeball
            attacking = true;
            counter = 20; // Start animation with mouth open and have delay
            GameController.addEntity(new SlimeBall(getLocation(), getDirection(), getSpeed() + 4, 6, true, tileController));
            Thread t = new Thread(() -> {
                int attackDelay = 12;
                while(attackDelay > 0){
                    attackDelay--;
                    try { Thread.sleep(12); } catch(InterruptedException e) { /* we tried */}
                }
                attacking = false;
            });
            t.start();
            getInventory().removeItem(inventory.getSelected(), 1);
        }else if(getInventory().getSelected() instanceof BombItem){ // Drop bomb
            GameController.addEntity(new Bomb(new Location(getLocation().getX() - (getDirection().getX() * 10),
                                                        getLocation().getY() - (getDirection().getY() * 10)), tileController));
            getInventory().removeItem(inventory.getSelected(), 1);
        }
    }

    private Direction getDirection(){
        return this.direction;
    }

    /**
     * @param direction Direction to get
     * @return Direction counter part moving/idle of whichever direction
     */
    private Direction getEquivalent(Direction direction){
        switch(direction){
            case IDLE_RIGHT: return Direction.RIGHT;
            case IDLE_LEFT: return Direction.LEFT;
            case IDLE_UP: return Direction.UP;
            case IDLE_DOWN: return Direction.DOWN;
            case UP: return Direction.IDLE_UP;
            case DOWN: return Direction.IDLE_DOWN;
            case LEFT: return Direction.IDLE_LEFT;
            case RIGHT: return Direction.IDLE_RIGHT;
        }
        return null;
    }

    @Override
    public Tile getTile(){
        return this.tile;
    }

    private void setTile(Tile tile) {
        this.tile = tile;
    }

    /**
     * @return Returns rectangle surrounding the player's sprite, adjusts to better fit sprite
     */
    public Rectangle getBounds(){
        Location bounds = new Location(getLocation());
        int height = SIZE;
        int width = SIZE;
        switch(getDirection()){
            case RIGHT:
            case IDLE_RIGHT:
            case LEFT:
            case IDLE_LEFT:
                bounds.move(0, 4);
                height -= 5;
                break;
            case UP:
            case IDLE_UP: width -= 5;
                break;
            case DOWN:
            case IDLE_DOWN:
                bounds.move(4, 0);
                width -= 5;
                break;
        }
        return new Rectangle(bounds.getX(), bounds.getY(), width, height);
    }

    @Override
    public String toString() {
        return super.toString() + " " + getDirection().toString() + " " + getTile();
    }

}
