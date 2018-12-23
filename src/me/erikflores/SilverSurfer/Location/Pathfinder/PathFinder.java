package me.erikflores.SilverSurfer.Location.Pathfinder;

import me.erikflores.SilverSurfer.Location.Tile;
import me.erikflores.SilverSurfer.Location.TileController;

import java.util.ArrayList;

public class PathFinder {

    private TileController tileController;
    private Tile target;
    private Tile currentTile;
    private Tile endTile;

    public PathFinder(TileController tileController, Tile targetTile, Tile currentTile){
        this.tileController = tileController;
        this.target = targetTile;
        this.currentTile = currentTile;
    }

    /**
     *  Start at current tile and checks neighboring tiles to find best tile to move to, repeating after each until
     *      target tile is found
     */
    private void findPath(){
        ArrayList<Tile> openTiles = new ArrayList<>();
        ArrayList<Tile> closedTiles = new ArrayList<>();

        currentTile.setParent(null); // In case there already was a parent before
        openTiles.add(currentTile);

        while(openTiles.size() > 0){ // Keep going
            currentTile = openTiles.get(0); // Reset to start

            for (Tile openTile : openTiles) { // Check through open tiles until finding best tile
                if (openTile.getFCost() < currentTile.getFCost() ||
                        (openTile.getFCost() == currentTile.getFCost() && openTile.getH() < currentTile.getH())) {
                    currentTile = openTile;
                }
            }

            openTiles.remove(currentTile);
            closedTiles.add(currentTile);

            for(Tile neighbor : tileController.getNeighbors(currentTile)){ // add neighbors and set info
                if(neighbor == null || neighbor.isWall() || closedTiles.contains(neighbor)){
                    continue;
                }

                if(!openTiles.contains(neighbor)){ // Update costs, and set parent
                    openTiles.add(neighbor);
                    neighbor.updateH(target);
                    neighbor.updateGCost(currentTile.getG() + 1);
                    neighbor.setParent(currentTile);
                }else{ // Check if g cost is less now, if it is update parent and cost
                    int newGCost = currentTile.getG() + 1;
                    if (newGCost < neighbor.getG()) {
                        neighbor.updateGCost(newGCost);
                        neighbor.setParent(currentTile);
                    }
                }
                if(neighbor.equals(target)){ // Found the end
                    endTile = neighbor;
                    currentTile = openTiles.get(0);
                    return;
                }
            }
        }
    }

    /**
     * @return Array of tiles from end to start
     */
    public ArrayList<Tile> getPath(){
        findPath();
        ArrayList<Tile> path = new ArrayList<>();
        Tile tile = endTile;
        while(tile != null){
            path.add(tile.getParent());
            tile = tile.getParent();
        }
        return path;
    }

}
