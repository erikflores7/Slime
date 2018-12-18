package me.erikflores.SilverSurfer;

import me.erikflores.SilverSurfer.Entity.*;
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
    private static final int WIDTH = 600;
    private static final int HEIGHT = 640;

    private GameState gameState = GameState.PLAYING;

    private Timer timer = new Timer(DELAY, this);

    private ArrayList<Entity> entities = new ArrayList<>();
    private Player player;
    private TileController tileController;

    private Image[] sprites = new Image[64];

    /**
     *  Initializes the JFrame and panel
     *
     * @param args Command line arguments, not used
     */
    public static void main(String[] args){

        JFrame frm = new JFrame();

        frm.setTitle("Silver Surfer");
        frm.setContentPane(new GameController());
        frm.setSize(WIDTH, HEIGHT);
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

        tileController = new TileController(36, 36, 20);
        tileController.createTiles();

        player = new Player(new Location(200, 200), tileController);
        entities.add(player);

        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g;

        graphics.setColor(Color.YELLOW);
        for(Tile tile : tileController.getTiles()){
            graphics.draw(tile.getShape());
        }

        graphics.setColor(Color.BLACK);
        switch(getGameState()) {
            case MENU: break;
            case PAUSED:
            case PLAYING:
                for (Entity entity : entities) {
                    Image image = sprites[player.getImageIndex()];
                    graphics.drawImage(image, entity.getLocation().getX(), entity.getLocation().getY(), null);
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
                for(Entity entity : entities){
                    entity.tick();
                }
            break;
        }
        repaint();
    }

    public GameState getGameState(){
        return this.gameState;
    }
    private void loadImages(){
        try{
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
