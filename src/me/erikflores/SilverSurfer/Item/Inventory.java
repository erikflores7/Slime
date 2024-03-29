package me.erikflores.SilverSurfer.Item;

public class Inventory {

    private int selected = 0;
    private int size = 0;
    private int maxSize;
    private ItemStack[] items;

    /**
     * Creates an Inventory with size amount of slots
     * @param size Max amount of slots
     */
    public Inventory(int size){
        this.items = new ItemStack[size];
        this.maxSize = size;
    }

    /**
     * Adds item to inventory if it doesn't exist or adds more to said item already in inventory
     * @param item Item to add
     */
    public void addItem(ItemStack item){
        if(!contains(item)){
            if(!isFull()){
                getItems()[getFirstEmpty()] = new ItemStack(item.getType(), item.getAmount());
                size++;
            }else{
                getItems()[getSelectedSlot()] = new ItemStack(item.getType(), item.getAmount());
            }
        }else{ // Add to the item already in inventory
            for(ItemStack items : getItems()){
                if (items != null && items.getName().equals(item.getName())){
                    items.add(item.getAmount());
                    return;
                }
            }
        }
    }

    /**
     * @return Array of items in inventory
     */
    public ItemStack[] getItems(){
        return this.items;
    }

    /**
     * @param index Slot to get
     * @return Item in slot 'index'
     */
    private ItemStack getItem(int index){
        return getItems()[index];
    }

    /**
     * @param item Item to check if contains
     * @return true if inventory contains item
     */
    private boolean contains(ItemStack item){
        for(ItemStack items : getItems()){
            if (items != null && items.getName().equals(item.getName())){
                return true;
            }
        }
        return false;
    }

    public boolean isFull(){
        return size == maxSize;
    }

    public void removeItem(ItemStack item){
        if(contains(item)){
            int i = 0;
            for(ItemStack items : getItems()){
                if (items != null && items.getName().equals(item.getName())){
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
    public void removeItem(ItemStack item, int amount){
        if(contains(item)){
            int i = 0;
            for(ItemStack items : getItems()){
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
    public ItemStack getSelected(){
        return getItem(getSelectedSlot());
    }

    /**
     * @param selected Slot to be selected
     */
    public void setSelected(int selected){
        this.selected = selected;
    }

    public int getFirstEmpty(){
        if(!isFull()){
            int i = 0;
            for(ItemStack item : getItems()){
                if(item == null){
                    return i;
                }
                i++;
            }
        }
        return -1;
    }

    public void clear(){
        items = new ItemStack[maxSize];
    }

    /**
     * Prints out all the items in inventory and their amount
     */
    public void debug(){
        System.out.println("{");
        for(ItemStack items : getItems()){
            if(items == null){
                System.out.println("NULL, ");
            }else{
                System.out.println(items.toString() + ", ");
            }
        }
        System.out.println("}");
    }

}
