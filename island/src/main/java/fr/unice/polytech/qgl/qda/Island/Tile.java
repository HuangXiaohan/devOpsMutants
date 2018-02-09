package fr.unice.polytech.qgl.qda.Island;

import fr.unice.polytech.qgl.qda.actions.ground.Move_to;
import org.json.JSONObject;
import java.awt.*;
import java.util.*;

/**
 * IslandProject created on 17/11/2015 by Keynes Timothy - Aloysius_tim
 */
public class Tile {
    private HashMap<Biome, Double> relatedBiomes;
    private HashSet<Ressource> associatedRessources;
    private int altitude;

    private int x;
    private int y;

    //flags
    private boolean unreachable;
    private boolean visited;
    private boolean scouted;
    private boolean glimpsed;

    public Tile(int x, int y) {
        this.relatedBiomes = new HashMap<>();
        this.associatedRessources = new HashSet<>();
        this.x=x;
        this.y=y;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
    public void setScouted(boolean scouted) {
        this.scouted = scouted;
    }

    public HashMap<Biome, Double> getRelatedBiomes() {
        return relatedBiomes;
    }

    public Biome getOnlyBiome() {
        Biome dominantBiome=null;
        if (relatedBiomes.size() == 1) {
            for (Map.Entry<Biome, Double> entry : relatedBiomes.entrySet()) {
                dominantBiome = entry.getKey();
            }
            return dominantBiome;
        }
        return null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point getNorth() {
        return new Point(x + Direction.N.getMovX(), y + Direction.N.getMovY());
    }

    public Point getSouth() {
        return new Point(x + Direction.S.getMovX(), y + Direction.S.getMovY());
    }

    public Point getEst() {
        return new Point(x + Direction.E.getMovX(), y + Direction.E.getMovY());
    }

    public Point getWest() {
        return new Point(x + Direction.W.getMovX(), y + Direction.W.getMovY());
    }

    public Point getAdjacent(Direction direction) {
        switch (direction) {
            case N:
                return getNorth();
            case W:
                return getWest();
            case S:
                return getSouth();
            case E:
                return getEst();
            default:
                return getEst();
        }
    }

    public void addRelatedBiomes(ArrayList<Biome> relatedBiomes) {
        for (Biome biome : relatedBiomes) {
            this.relatedBiomes.put(biome, null);
        }
    }

    public int findDistance(Tile b){
        // retourne la distance entre 2 tiles
        return (int) Math.sqrt(Math.pow((this.getX() - b.getX()), 2) + Math.pow((this.getY() - b.getY()), 2));
    }

    /**
     * détermine le nombre de move_to sur l'axe des X à faire, pour rejoindre le huntedBiome
     * @param futurTile
     * @return
     */
    private LinkedList<JSONObject> xPath(Tile futurTile){
        LinkedList<JSONObject> actions=new LinkedList<>();

        if (this.getX()< futurTile.getX()) {
            for (int i = this.getX(); i < futurTile.getX(); i++) {
                actions.add(Move_to.buildAction(Direction.E));
            }
        } else if (this.getX() > futurTile.getX()) {
            for (int i = this.getX(); i > futurTile.getX(); i--) {
                actions.add(Move_to.buildAction(Direction.W));
            }
        }
        return actions;
    }

    /**
     * détermine le nombre de move_to sur l'axe des Y à faire, pour rejoindre le huntedBiome
     * @param futurTile
     * @return
     */
    private LinkedList<JSONObject> yPath(Tile futurTile){
        LinkedList<JSONObject> actions=new LinkedList<>();

        if (this.getY()< futurTile.getY()) {
            for (int i = this.getY(); i < futurTile.getY(); i++) {
                actions.add(Move_to.buildAction(Direction.N));
            }
        } else if (this.getY() > futurTile.getY()) {
            for (int i = this.getY(); i > futurTile.getY(); i--) {
                actions.add(Move_to.buildAction(Direction.S));
            }
        }
        return actions;
    }

    /**
     *  détermine si il vaut mieux se déplacer sur l'axe des X en premier ou Y en premier pour rejoindre le HuntedBiome (afin d'éviter la sortie de l'ile
     *  et de devoir nager sur une partie du trajet)
     */
    public LinkedList<JSONObject> findPath(Tile futurTile){
        LinkedList<JSONObject> actions=new LinkedList<>();

        boolean Xfirst;
        if(this.getX() > futurTile.getX()){
            if(this.getX()>0){ Xfirst = true;}
            else { Xfirst = false;}
        }
        else{
            if(this.getX()<0){ Xfirst = true;}
            else { Xfirst = false;}
        }

        if(Xfirst){
            actions.addAll(xPath(futurTile));
            actions.addAll(yPath(futurTile));
        }
        else{
            actions.addAll(yPath(futurTile));
            actions.addAll(xPath(futurTile));
        }
        return actions;
    }

    public void addRelatedBiomes(Biome biome) {
        this.relatedBiomes.put(biome, null);
    }

    public void addAssociatedRessource(ArrayList<Ressource> ressources) {
        this.associatedRessources.addAll(ressources);
    }

    public void addAssociatedRessource(Ressource ressource) {
        this.associatedRessources.add(ressource);
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public HashSet<Ressource> getAssociatedRessources() {
        return associatedRessources;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "altitude=" + altitude +
                ", relatedBiomes=" + relatedBiomes +
                ", associatedRessources=" + associatedRessources +
                ", x=" + x +
                ", y=" + y +
                ", unreachable=" + unreachable +
                ", visited=" + visited +
                ", scouted=" + scouted +
                ", glimpsed=" + glimpsed +
                '}';
    }
}
