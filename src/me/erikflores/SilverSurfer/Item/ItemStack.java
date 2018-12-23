package me.erikflores.SilverSurfer.Item;

import me.erikflores.SilverSurfer.GameController;
import me.erikflores.SilverSurfer.Location.Location;

import java.awt.*;

public class ItemStack {

    private int amount;
    private String name;
    private int imageIndex, maxStack;
    private Item item;

    private int price;
    private boolean alive = false;
    private int ticks = 0;
    private boolean goingUp = true;
    private Location location;

    private Rectangle bounds;

    public ItemStack(Item item, int amount){
        this.name = item.getName();
        this.price = item.getPrice();
        this.maxStack = item.getMaxStack();
        this.imageIndex = item.getImageIndex();
        this.item = item;
        setAmount(amount);
    }

    public String getName(){
        return name;
    }

    public Item getType(){
        return this.item;
    }

    public int getAmount(){
        return this.amount;
    }

    private void setAmount(int amount){
        this.amount = amount;
        if(getAmount() > maxStack){
            this.amount = maxStack;
        }
    }

    public void remove(int amount){
        setAmount(getAmount() - amount);
    }

    public void add(int amount){
        setAmount(getAmount() + amount);
    }

    public int getPrice(){
        return getAmount() * price;
    }

    public int getImageIndex(){
        return imageIndex;
    }

    /**
     * Spawns the item at location that can be picked up by the player
     * @param location Location to spawn item at
     */
    public void spawn(Location location){
        alive = true;
        GameController.addItem(this);
        this.location = location;
        bounds = new Rectangle(getLocation().getX() + 10, getLocation().getY() + 10, 18, 18);
    }

    public Location getLocation(){
        return location;
    }

    /**
     * Makes item bounce up and down if item is spawned
     */
    public void tick(){ // TODO add a max amount of ticks before the item disappears
        if(alive){ // Bounce up and down
            int modifier = 1;
            if(!goingUp){
                modifier = -1;
            }
            if(ticks % 10 == 0) {
                location.move(0, modifier);
            }
            ticks += modifier;
            if(ticks == 0){
                goingUp = true;
            }else if(ticks == 100){
                goingUp = false;
            }
        }
    }

    public Rectangle getBounds(){
        return bounds;
    }

    @Override
    public String toString(){
        return name + ": " + getAmount();
    }

}
