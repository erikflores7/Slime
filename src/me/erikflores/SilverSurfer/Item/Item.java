package me.erikflores.SilverSurfer.Item;

import me.erikflores.SilverSurfer.GameController;
import me.erikflores.SilverSurfer.Location.Location;

import java.awt.*;

public class Item {

    private int amount;
    private String name;
    private boolean alive = false;
    private int ticks = 0;
    private boolean goingUp = true;
    private Location location;

    private Rectangle bounds;

    public Item(String name, int amount){
        this.name = name;
        this.amount = amount;
    }

    public String getName(){
        return name;
    }

    public int getAmount(){
        return this.amount;
    }

    public void setAmount(int amount){
        this.amount = amount;
        if(getAmount() > 99){
            this.amount = 99;
        }
    }

    public void remove(int amount){
        setAmount(getAmount() - amount);
    }

    public void add(int amount){
        setAmount(getAmount() + amount);
    }

    public int getImageIndex(){
        return 0;
    }

    public void spawn(Location location){
        alive = true;
        GameController.addItem(this);
        this.location = location;
        bounds = new Rectangle(getLocation().getX() + 12, getLocation().getY() + 12, 14, 14);
    }

    public Location getLocation(){
        return location;
    }

    public void tick(){
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
