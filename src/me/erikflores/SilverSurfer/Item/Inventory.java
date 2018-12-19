package me.erikflores.SilverSurfer.Item;

public class Inventory {

    private int selected = 0;
    private int size = 0;
    private int maxSize;
    private Item[] items;

    /**
     * Creates an Inventory with size amount of slots
     * @param size Max amount of slots
     */
    public Inventory(int size){
        this.items = new Item[size];
        this.maxSize = size;
    }

    /**
     * Adds item to inventory if it doesn't exist or adds more to said item already in inventory
     * @param item Item to add
     */
    public void addItem(Item item){
        if(!contains(item)){
            if(size < maxSize){
                getItems()[size] = item;
                size++;
            }else{
                getItems()[getSelectedSlot()] = item;
            }
        }else{ // Add to the item already in inventory
            for(Item items : getItems()){
                if (items != null && items.equals(item)){
                    items.add(item.getAmount());
                    return;
                }
            }
        }
    }

    /**
     * @return Array of items in inventory
     */
    public Item[] getItems(){
        return this.items;
    }

    /**
     * @param index Slot to get
     * @return Item in slot 'index'
     */
    private Item getItem(int index){
        return getItems()[index];
    }

    /**
     * @param item Item to check if contains
     * @return true if inventory contains item
     */
    private boolean contains(Item item){
        for(Item items : getItems()){
            if (items != null && items.equals(item)){
                return true;
            }
        }
        return false;
    }

    public void removeItem(Item item){
        if(contains(item)){
            int i = 0;
            for(Item items : getItems()){
                if (items != null && items.equals(item)){
                    remove(i);
                    size--;
                    return;
                }
                i++;
            }
        }
    }

    /**
     * Removes the item in slot 'index'
     * @param index Slot to remove
     */
    public void remove(int index){
        getItems()[index] = null;
        size--;
    }

    /**
     * Removes amount from item, removes the item from inventory if it's amount is 0 or below
     * @param item Item to remove amount from
     * @param amount Amount to remove from item
     */
    public void removeItem(Item item, int amount){
        if(contains(item)){
            int i = 0;
            for(Item items : getItems()){
                if (items != null && items.equals(item)){
                    items.remove(amount);
                    if(items.getAmount() <= 0) {
                        remove(i);
                    }
                    return;
                }
                i++;
            }
        }
    }

    /**
     * @return Slot that is selected
     */
    public int getSelectedSlot(){
        return this.selected;
    }

    /**
     * @return Item that is currently selected
     */
    public Item getSelected(){
        return getItem(getSelectedSlot());
    }

    /**
     * @param selected Slot to be selected
     */
    public void setSelected(int selected){
        this.selected = selected;
    }

    /**
     * Prints out all the items in inventory and their amount
     */
    public void debug(){
        System.out.println("{");
        for(Item items : getItems()){
            if(items == null){
                System.out.println("NULL, ");
            }else{
                System.out.println(items.toString() + ", ");
            }
        }
        System.out.println("}");
    }

}
