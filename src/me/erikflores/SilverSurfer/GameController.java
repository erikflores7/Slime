package me.erikflores.SilverSurfer;

import me.erikflores.SilverSurfer.Entity.*;
import me.erikflores.SilverSurfer.Item.BombItem;
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

    public static final int SIZE = 40;

    private static final int TILE_SIZE = 26;
    private static final int WIDTH = 31 * TILE_SIZE;
    private static final int HEIGHT = 28 * TILE_SIZE;

    public static int ROUND = 1;
    private static int enemiesLeft = 6;
    private static int money = 10;
    private int enemyTimer = 0;
    private int spawnTimer = 2000;

    private GameState gameState = GameState.PLAYING;

    private Timer timer = new Timer(DELAY, this);

    // List of entities/items to display on screens
    private static ArrayList<Entity> entities = new ArrayList<>();
    private static ArrayList<Entity> entitiesToRemove = new ArrayList<>();
    private static ArrayList<Item> items = new ArrayList<>();
    private static ArrayList<Item> itemsToRemove = new ArrayList<>();

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
        frm.setSize(WIDTH + 13, HEIGHT + 33);
        frm.setResizable(false);
        frm.setVisible(true);
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public GameController(){

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        addKeyListener(new InputListener());

        timer.setInitialDelay(1000);

        loadImages();

        tileController = new TileController(29, 32, TILE_SIZE);
        tileController.createTiles();

        player = new Player(new Location(200, 240), tileController);
        getEntities().add(player);

        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g;

        graphics.setColor(Color.BLACK);
        boolean paused = false;
        switch(getGameState()) {
            case MENU: break;
            case PAUSED: paused = true;
            case PLAYING:
                graphics.drawImage(map, 13, 13, null);

                graphics.setColor(Color.YELLOW);
                for(Tile tile : tileController.getTiles()){
                    graphics.draw(tile.getBounds());
                }

                for (Entity entity : getEntities()) {
                    Image image = sprites[entity.getImageIndex()];
                    graphics.drawImage(image, entity.getLocation().getX(), entity.getLocation().getY(), null);
                    graphics.setColor(Color.RED);
                    graphics.draw(entity.getBounds());
                }
                for (Item item : getItems()) {
                    Image image = sprites[item.getImageIndex()];
                    graphics.drawImage(image, item.getLocation().getX(), item.getLocation().getY(), null);
                }
                drawHUD(graphics);
                if(paused){
                    graphics.setColor(new Color(100, 100, 100, 210));
                    graphics.fill(new Rectangle(0, 0, WIDTH + 13, HEIGHT + 13));
                }

                break;
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
                for(Entity entity : getEntities()){ // Tick for every entity
                    entity.tick();
                }
                getEntities().removeAll(entitiesToRemove); // Remove any entity that died
                for(Item item : getItems()){ // Tick every item
                    item.tick();
                }
                getItems().removeAll(itemsToRemove); // Remove any item that got picked up
                if(enemiesLeft <= 0){ // Go to next round
                    ROUND++;
                    enemiesLeft = 4 + ROUND * 2;
                    enemyTimer = 0;
                }
                enemyTimer++;
                spawnTimer--;
                if(enemyTimer % 180 == 0 && (enemyTimer / 90) <= (ROUND * 2 + 4)){ // Make sure not all enemies have been spawned for this wave
                    getEntities().add(new Rick(new Location(WIDTH / 2 - 30, 20), 1, player, tileController));
                    getEntities().add(new Rick(new Location(WIDTH / 2 - 30, HEIGHT - 30), 1, player, tileController));
                }
                // Spawn an item on a random tile for player to be able to pick up
                if(spawnTimer == 0){
                    Location loc = tileController.getRandTile().getLocation();
                    BombItem test = new BombItem(1);
                    test.spawn(new Location(loc.getX() - 10, loc.getY() - 10));
                    getItems().add(test);
                    spawnTimer = 2000;
                }
            break;
        }
        repaint();
    }

    public static void addKill(){
        enemiesLeft--;
        money += 100;
    }

    private void togglePause(){
        if(getGameState() == GameState.PAUSED){
            this.gameState = GameState.PLAYING;
        }else{
            this.gameState = GameState.PAUSED;
        }
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
                    sprites[c * 8 + i] = spriteSheet.getSubimage(i * 16, c * 16, 16, 16).getScaledInstance(40, 40, Image.SCALE_DEFAULT);
                }
            }
        }catch(IOException e){
            System.out.println("Image not found!");
            System.exit(1);
        }
    }

    public static void addEntity(Entity entity){
        getEntities().add(entity);
    }

    public static void removeEntity(Entity entity){
        entitiesToRemove.add(entity);
    }

    public static ArrayList<Entity> getEntities(){
        return entities;
    }

    public static void addItem(Item item){ getItems().add(item);}
    public static void removeItem(Item item){ itemsToRemove.add(item); }
    public static ArrayList<Item> getItems(){ return items; }

    private void drawHUD(Graphics2D graphics){
        int x = WIDTH / 2 - 90;
        int y = HEIGHT - 50;
        graphics.setColor(Color.GRAY);
        graphics.draw(new Rectangle(x, y - 10, 160, 5));
        graphics.setColor(Color.GREEN);
        graphics.fill(new Rectangle(x, y - 10, (int) (player.getHealth() * 1.6), 5));
        int i = 0;
        for(Item items : player.getInventory().getItems()){
            if(player.getInventory().getSelectedSlot() == i){ // Item border
                graphics.drawImage(sprites[20], x, y, null); // Selected border
            }else{
                graphics.drawImage(sprites[21], x, y, null); // Default item border
            }
            if (items != null){
                graphics.drawImage(sprites[items.getImageIndex()], x, y, null);
                graphics.setFont(new Font("Courier", Font.BOLD, 15));
                graphics.setColor(Color.WHITE);
                graphics.drawString(items.getAmount() + "", x + 22, y + 34);
            }
            x += SIZE;
            i++;
        }
        graphics.setColor(Color.BLACK);
        graphics.drawString("Round:" + ROUND, WIDTH / 2 - 129, 23);
        graphics.drawString("$" + money, WIDTH / 2 - 129, 37);
    }

    private class InputListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            if(getGameState() == GameState.PLAYING){
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
                case KeyEvent.VK_ESCAPE: togglePause(); break;
                }
            }else if(getGameState() == GameState.PAUSED && e.getKeyCode() == KeyEvent.VK_ESCAPE){
                togglePause();
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
