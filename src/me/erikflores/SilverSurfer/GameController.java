package me.erikflores.SilverSurfer;

import me.erikflores.SilverSurfer.Entity.*;
import me.erikflores.SilverSurfer.Item.Item;
import me.erikflores.SilverSurfer.Location.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GameController extends JPanel implements ActionListener {

    private static final int DELAY = 1000 / 60; // 60 fps
    private static final int SIZE = 26;
    private static final int WIDTH = 31 * SIZE;
    private static final int HEIGHT = 28 * SIZE;

    private GameState gameState = GameState.PLAYING;

    private Timer timer = new Timer(DELAY, this);

    private static ArrayList<Entity> entities = new ArrayList<>();
    private static ArrayList<Entity> entitiesToRemove = new ArrayList<>();
    private Player player;
    private TileController tileController;

    private Image[] sprites = new Image[64];
    private Image map;

    /**
     *  Initializes the JFrame and panel
     *
     * @param args Command line arguments, not used
     */
    public static void main(String[] args){

        JFrame frm = new JFrame();

        frm.setTitle("Silver Surfer");
        frm.setContentPane(new GameController());
        frm.setSize(WIDTH, HEIGHT + 20);
        frm.setResizable(false);
        frm.setVisible(true);
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public GameController(){

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        addKeyListener(new InputListener());

        timer.setInitialDelay(DELAY);

        loadImages();

        tileController = new TileController(28, 31, SIZE);
        tileController.createTiles();

        player = new Player(new Location(200, 220), tileController);
        entities.add(player);

        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g;

        graphics.setColor(Color.BLACK);
        switch(getGameState()) {
            case MENU: break;
            case PAUSED:
            case PLAYING:
                graphics.setBackground(Color.BLACK);
                graphics.drawImage(map, 0, 0, null);
                for (Entity entity : entities) {
                    Image image = sprites[entity.getImageIndex()];
                    graphics.drawImage(image, entity.getLocation().getX(), entity.getLocation().getY(), null);
                }
                int x = WIDTH / 2 - 124;
                int y = HEIGHT - 50;
                int i = 0;
                for(Item items : player.getInventory().getItems()){
                    if(player.getInventory().getSelectedSlot() == i){ // Item border
                        graphics.drawImage(sprites[20], x, y, null); // Selected border
                    }else{
                        graphics.drawImage(sprites[21], x, y, null); // Default item border
                    }
                    if (items != null){
                        graphics.drawImage(sprites[items.getImageIndex()], x, y, null);
                        graphics.setFont(new Font("Courier", Font.BOLD, 17));
                        graphics.setColor(Color.WHITE);
                        graphics.drawString(items.getAmount() + "", x + 30, y + 40);
                    }
                    x += 48;
                    i++;
                }
            break;
        }

        graphics.setColor(Color.YELLOW);
        for(Tile tile : tileController.getTiles()){
            graphics.draw(tile.getShape());
        }
    }

    /**
     * Main game loop
     *
     * @param e Called every DELAY
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch(getGameState()){
            case MENU: break;
            case PLAYING:
                for(Entity entity : entities){
                    entity.tick();
                }
                entities.removeAll(entitiesToRemove);
            break;
        }
        repaint();
    }

    public GameState getGameState(){
        return this.gameState;
    }
    private void loadImages(){
        try{
            map = ImageIO.read(new File("map.png")).getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);
            BufferedImage spriteSheet = ImageIO.read(new File("spritesheet.png"));
            for(int c = 0; c < 8; c++){
                for(int i = 0; i < 8; i++){
                    sprites[c * 8 + i] = spriteSheet.getSubimage(i * 16, c * 16, 16, 16).getScaledInstance(48, 48, Image.SCALE_DEFAULT);
                }
            }
        }catch(IOException e){
            System.out.println("Image not found!");
            System.exit(1);
        }
    }

    public static void addEntity(Entity entity){
        entities.add(entity);
    }

    public static void removeEntity(Entity entity){
        entitiesToRemove.add(entity);
    }

    private class InputListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()){
                case KeyEvent.VK_W: player.setDirection(Direction.UP); break;
                case KeyEvent.VK_A: player.setDirection(Direction.LEFT); break;
                case KeyEvent.VK_S: player.setDirection(Direction.DOWN); break;
                case KeyEvent.VK_D: player.setDirection(Direction.RIGHT); break;
                case KeyEvent.VK_SPACE: player.attack(); break;
                case KeyEvent.VK_1: player.getInventory().setSelected(0); break;
                case KeyEvent.VK_2: player.getInventory().setSelected(1); break;
                case KeyEvent.VK_3: player.getInventory().setSelected(2); break;
                case KeyEvent.VK_4: player.getInventory().setSelected(3); break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch(e.getKeyCode()){
                case KeyEvent.VK_W: player.removeDirection(Direction.UP); break;
                case KeyEvent.VK_A: player.removeDirection(Direction.LEFT); break;
                case KeyEvent.VK_S: player.removeDirection(Direction.DOWN); break;
                case KeyEvent.VK_D: player.removeDirection(Direction.RIGHT); break;
            }
        }
    }
}
