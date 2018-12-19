package me.erikflores.SilverSurfer.Item;

public class BombItem extends Item {

    public BombItem(int amount){
        super("Bomb", amount);
    }

    @Override
    public int getImageIndex(){
        return 17;
    }

    @Override
    public void spawn(){

    }
}
