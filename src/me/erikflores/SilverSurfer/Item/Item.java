package me.erikflores.SilverSurfer.Item;

public class Item {

    private int amount;
    private String name;

    public Item(String name, int amount){
        this.name = name;
        this.amount = amount;
    }

    public int getAmount(){
        return this.amount;
    }

    public void setAmount(int amount){
        this.amount = amount;
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

    public void spawn(){}

    @Override
    public String toString(){
        return name + ": " + getAmount();
    }

}
