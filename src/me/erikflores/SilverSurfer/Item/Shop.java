package me.erikflores.SilverSurfer.Item;

import me.erikflores.SilverSurfer.Entity.Player;
import me.erikflores.SilverSurfer.GameController;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Shop implements MouseListener{

    private Inventory inventory = new Inventory(24);
    private Inventory playerInventory;
    private Player player;
    private boolean shopOpen = false;

    private int initX = 220;
    private int initY = 190;

    private Rectangle shopBounds = new Rectangle(initX, initY, 5 * GameController.SIZE, 4 * GameController.SIZE);
    private Rectangle buyBounds = new Rectangle(initX + 215, initY + 110, 80, 30);

    public Shop(Inventory playerInventory, Player player){  this.playerInventory = playerInventory; this.player = player; }

    /**
     * Opens the shop and loads items into the inventory
     */
    public void open(){
        shopOpen = true;
        loadItems();
    }

    /**
     * Closes the shop and won't allow it to be opened
     */
    public void close(){
        shopOpen = false;
    }

    /**
     * Draws the shop on the screen and shows selected item's description
     * @param graphics Graphics to draw on
     * @param spriteSheet Sprite sheet to display item and inventory slots
     */
    public void show(Graphics2D graphics, Image[] spriteSheet){
        if(!shopOpen){ // Don't draw if not open
            return;
        }
        int i = 0;
        for(int r = 0; r < 4; r++){ // Draw all slots
            for(int c = 0; c < 5; c++){
                if(inventory.getSelectedSlot() == i){ // Draw right slot
                    graphics.drawImage(spriteSheet[20], initX + c * GameController.SIZE, initY + r * GameController.SIZE, null);
                }else {
                    graphics.drawImage(spriteSheet[21], initX + c * GameController.SIZE, initY + r * GameController.SIZE, null);
                }
                if(inventory.getItems().length > i && inventory.getItems()[i] != null){ // Draw item if it exists in there
                    graphics.drawImage(spriteSheet[inventory.getItems()[i].getImageIndex()], initX + c * GameController.SIZE,
                                                initY + r * GameController.SIZE, null);
                }
                i++;
            }
        }
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.fill(new Rectangle(initX + 200, initY, 120, 160)); // Draw Description rect
        graphics.setColor(Color.GRAY);
        graphics.draw(new Rectangle(initX + 200, initY, 120, 160)); // Draw frame
        if(inventory.getSelected() != null){ // Draw description of item selected
            ItemStack selected = inventory.getSelected();
            graphics.drawImage(spriteSheet[selected.getImageIndex()], initX + 240, initY + 10, null);
            graphics.setColor(Color.WHITE);
            graphics.setFont(new Font("Courier", Font.PLAIN, 13));
            graphics.drawString(selected.getName(), initX + 215, initY + 60);
            graphics.drawString("Price: $" + selected.getPrice(), initX + 205, initY + 80);
            graphics.drawString("Amount: " + selected.getAmount(), initX + 205, initY + 100);
            graphics.drawString("Purchase", initX + 220, initY + 130);
            graphics.draw(buyBounds);
        }
    }

    /**
     * @return true if shop is currently open
     */
    public boolean isOpen(){
        return this.shopOpen;
    }

    /**
     * Loads proper items into shop inventory depending on round on
     */
    private void loadItems(){
        switch(GameController.ROUND){
            case 10:
            case 5: inventory.removeItem(new ItemStack(Item.SMALL_BOMB, 1));
                inventory.addItem(new ItemStack(Item.SMALL_BOMB, 1)); break;
            case 2:
                inventory.removeItem(new ItemStack(Item.SLIME_BALL, 10));
            inventory.addItem(new ItemStack(Item.SLIME_BALL, 10));
                inventory.removeItem(new ItemStack(Item.HEALTH_PACK, 1));
                inventory.addItem(new ItemStack(Item.HEALTH_PACK, 1)); break;
        }
    }

    /**
     * Checks if clicking inside shop menu and handles it
     * @param e Mouse Click event
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if(shopOpen && shopBounds.contains(e.getPoint())){ // Clicked in slots
            int x = e.getX() - initX;
            int y = e.getY() - initY;
            int column = x / GameController.SIZE;
            int row = y / GameController.SIZE;
            inventory.setSelected(row * 5 + column); // set selected slot to once clicked
        }else if(shopOpen && buyBounds.contains(e.getPoint())){ // Clicked on Buy button
            if(inventory.getSelected() == null){ // Don't do anything if selected item is null
                return;
            }

            if(GameController.buyItem(inventory.getSelected())){ // Add item to inventory
                if(inventory.getSelected().getType() == Item.HEALTH_PACK){ // Don't add Health pack to inventory, apply health
                    player.setHealth(player.getHealth() + 20);
                    return;
                }
                playerInventory.addItem(inventory.getSelected());
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

}
