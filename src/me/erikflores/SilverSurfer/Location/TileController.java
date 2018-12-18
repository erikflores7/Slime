package me.erikflores.SilverSurfer.Location;

import java.util.ArrayList;

public class TileController {

    private ArrayList<Tile> tiles = new ArrayList<>();
    private int rows, cols, size;

    public TileController(int rows, int cols, int size){
        this.rows = rows;
        this.cols = cols;
        this.size = size;
    }

    public void createTiles(){
        tiles.clear();
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                tiles.add(new Tile(r, c));
            }
        }
    }

    public Tile getNeighbor(Tile tile, Direction direction){
        int row = tile.getRow() + direction.getY();
        int col = tile.getColumn() + direction.getX();
        return getTileIn(col, row);
    }

    public Tile getTileIn(int col, int row){
        int index = row * cols + col;
        if (index < 0 || index >= getTiles().size()){
            return null;
        }
        return getTiles().get(index);
    }

    public Tile getTileIn(Location location){
        int x = location.getX();
        int y = location.getY();
        if(x < 0 || y < 0 || cols * size <= x || rows * size <= location.getY()){ // out of bounds
            return null;
        }
        int col = location.getX() / size;
        int row = location.getY() / size;
        return getTileIn(col, row);
    }

    public ArrayList<Tile> getTiles(){
        return this.tiles;
    }

}