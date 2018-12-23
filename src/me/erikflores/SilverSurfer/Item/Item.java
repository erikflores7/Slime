package me.erikflores.SilverSurfer.Item;

public enum Item {

    SLIME_BALL("SlimeBall", 16, 99, 20), SMALL_BOMB("Small Bomb", 17, 99, 250), HEALTH_PACK("+20 Health", 22, 1, 250);

    private String name;
    private int imageIndex;
    private int maxStack;
    private int price;

    Item(String name, int imageIndex, int maxStack, int price){
        this.name = name;
        this.imageIndex = imageIndex;
        this.maxStack = maxStack;
        this.price = price;
    }

    public String getName(){
        return this.name;
    }

    public int getImageIndex(){
        return this.imageIndex;
    }

    public int getMaxStack(){
        return this.maxStack;
    }

    public int getPrice(){
        return this.price;
    }

}
