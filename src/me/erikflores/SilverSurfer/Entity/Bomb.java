package me.erikflores.SilverSurfer.Entity;

import me.erikflores.SilverSurfer.GameController;
import me.erikflores.SilverSurfer.Location.Location;

public class Bomb extends Entity{

    private int ticksToExplode = 60;

    public Bomb(Location spawn){
        super("Bomb", spawn);
    }

    @Override
    public void tick(){
        ticksToExplode--;
        if(ticksToExplode == 0){
            detonate();
        }
    }

    private void detonate(){
        GameController.removeEntity(this);
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
