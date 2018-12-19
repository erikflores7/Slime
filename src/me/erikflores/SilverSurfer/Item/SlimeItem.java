package me.erikflores.SilverSurfer.Item;

public class SlimeItem extends Item {

    public SlimeItem(int amount){
        super("SlimeBall", amount);
    }

    @Override
    public int getImageIndex(){
        return 16;
    }

}
