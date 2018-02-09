package fr.unice.polytech.qgl.qda.Island;

import fr.unice.polytech.qgl.qda.Game.AvailableActions;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * IslandProject created on 17/11/2015 by Keynes Timothy - Aloysius_tim
 */
public class IslandMap extends HashMap<Point, Tile> {
    private AvailableActions.Phase phase;
    private Tile positionActuelle;
    private Direction directionActuelle;

    private HashMap<Ressource, Integer> collectedRessources;

    private HashMap<String, Tile> creeks; /** une creek est une Tile à laquelle est associée un ID de type String **/


    private int width = 0;
    private int height = 0;

    /**
     * Instantiates a new Island map.
     *
     * @param directionActuelle the direction actuelle
     */
    public IslandMap(Direction directionActuelle) {
        this.collectedRessources=new HashMap<>();
        this.phase = AvailableActions.Phase.PHASE1;
        this.directionActuelle = directionActuelle;

        this.positionActuelle = createTile(new Point(0, 0));

        this.creeks = new HashMap<>();
    }

    public void updateCollectedRessource(Ressource ressource, int nb){
        if (this.collectedRessources.containsKey(ressource))
            this.collectedRessources.replace(ressource, this.collectedRessources.get(ressource)+nb);
        else this.collectedRessources.put(ressource, nb);
    }

    public Direction getDirectionActuelle() {
        return directionActuelle;
    }

    public ArrayList<String> getCreeks() {
        ArrayList<String> result = new ArrayList<>();
        for (Map.Entry<String, Tile> currentEntry : creeks.entrySet()) {
            result.add(currentEntry.getKey());
        }
        return result;
    }

    public void actualizePosition(int x, int y) {
        this.positionActuelle = createTile(new Point(x, y));
    }

    public AvailableActions.Phase changePhase() {
        this.phase = AvailableActions.Phase.PHASE2;
        return this.phase;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * ********************************************************************************************************************************************************
     * ********************************************************************************************************************************************************
     * ************************************      ACTIONS DE L'ILE      ****************************************************************************************
     * ********************************************************************************************************************************************************
     * ********************************************************************************************************************************************************
     */
    /**
     * CreateTile.
     * Crée une Tile selon si existante ou non.
     *
     * @param point correspond à la position que l'on cherche
     * @return la Tile correspondante qui soit a été crée ou était existante
     */
    public Tile createTile(Point point) {
        if (this.containsKey(point))
            return this.get(point);
        else {
            this.put(point, new Tile(point.x, point.y));
            return this.get(point);
        }
    }

    /**
     * Fly.
     * This action move the drone forward, following its current heading. In the following picture, the drone is heading East, initially
     * flying over the blue square. When asked to fly, it moves forward according to its current heading. You cannot fly outside of the map,
     * i.e., reaching the boundary of the map will lost your radio connection with the homeport, making you unable to control the drone anymore.
     * <p>
     * Request example:
     * { "action": "fly" }
     * Response example:
     * { "cost": 2, "extras": {}, "status": "OK" }
     */
    public void fly() {
        positionActuelle = this.createTile(new Point(positionActuelle.getX() + directionActuelle.getMovX(), positionActuelle.getY() + directionActuelle.getMovY()));
        positionActuelle = this.createTile(new Point(positionActuelle.getX() + directionActuelle.getMovX(), positionActuelle.getY() + directionActuelle.getMovY()));
        positionActuelle = this.createTile(new Point(positionActuelle.getX() + directionActuelle.getMovX(), positionActuelle.getY() + directionActuelle.getMovY()));
    }

    /**
     * Heading.
     * This action changes the current heading of the drone. Like for flying, reaching the boundary of the map will lose your radio connection.
     * In the following picture, the drone is initially heading East. When asked to go North, it change its heading and as it moves during the
     * process (it's a plane, not a helicopter), it ends in the upper position (yellowish). When asked to go South, it ends in the lower position
     * (greenish). Like for flying, you cannot go outside of the map without losing the radio connection. Considering that you are controlling a plane,
     * you cannot change heading to do a U-turn: with an initial heading going East, you can only change your heading to North or South.
     * <p>
     * Request example:
     * { "action": "heading", "parameters": { "direction": "N" } }
     * { "action": "heading", "parameters": { "direction": "S" } }
     * Response example:
     * { "cost": 4, "extras": {}, "status": "OK" }
     *
     * @param direction the direction
     */
    public void heading(Direction direction) {
        fly();
        directionActuelle = direction;
        fly();
    }

    /**
     * Echo.
     * The radar is used to check the presence of ground in a given direction. The radar can output two kind of information: in the best case it hits the
     * island ground, and in the worst case it hits the boundary of the map. The radar analyser also output the range (i.e., the number of 3x3 squares) to
     * cross to reach the detected element. One can only scan one direction at a time. The radar is not available at the rear of the plane, i.e, if the
     * current heading is East, you can only use the radar with directions East, North or South. In the following picture, the land is depicted with the big
     * orange unknown shape. Echoing to the East will answer 2, echoing to the South will answer 0. Echoing to the North will indicate that it reaches the
     * out of range limit, within 1 square.
     * <p>
     * Request example:
     * { "action": "echo", "parameters": { "direction": "E" } }
     * Response example:
     * { "cost": 1, "extras": { "range": 2, "found": "GROUND" }, "status": "OK" }
     * { "cost": 1, "extras": { "range": 0, "found": "OUT_OF_RANGE" }, "status": "OK" }
     *
     * @param direction the direction
     * @param range     the range
     * @param typeBiome the ground
     */
    public void echo(Direction direction, int range, Biome typeBiome) {
        Tile tmp = positionActuelle;
        if (range == 0) {
            this.positionActuelle.addRelatedBiomes(typeBiome);
        } else {
            this.positionActuelle.addRelatedBiomes(Biome.OCEAN);
        }
        if (typeBiome == Biome.GROUND) {
            for (int i = 1; i <= range; i++) {
                switch (direction) {
                    case N:
                        if (i == range) {
                            tmp = createTile(tmp.getNorth());
                            tmp.addRelatedBiomes(Biome.GROUND);
                        } else {
                            tmp = createTile(tmp.getNorth());
                            tmp.addRelatedBiomes(Biome.OCEAN);
                        }
                        break;
                    case W:
                        if (i == range) {
                            tmp = createTile(tmp.getWest());
                            tmp.addRelatedBiomes(Biome.GROUND);
                        } else {
                            tmp = createTile(tmp.getWest());
                            tmp.addRelatedBiomes(Biome.OCEAN);
                        }
                        break;
                    case S:
                        if (i == range) {
                            tmp = createTile(tmp.getSouth());
                            tmp.addRelatedBiomes(Biome.GROUND);
                        } else {
                            tmp = createTile(tmp.getSouth());
                            tmp.addRelatedBiomes(Biome.OCEAN);
                        }
                        break;
                    case E:
                        if (i == range) {
                            tmp = createTile(tmp.getEst());
                            tmp.addRelatedBiomes(Biome.GROUND);
                        } else {
                            tmp = createTile(tmp.getEst());
                            tmp.addRelatedBiomes(Biome.OCEAN);
                        }
                        break;
                }
            }
        } else if (typeBiome == Biome.OUT_OF_RANGE) {
            for (int i = 1; i <= range; i++) {
                switch (direction) {
                    case N:
                        if (i == range) {
                            tmp = createTile(tmp.getNorth());
                            tmp.addRelatedBiomes(Biome.OUT_OF_RANGE);
                        } else {
                            tmp = createTile(tmp.getNorth());
                            tmp.addRelatedBiomes(Biome.OCEAN);
                        }
                        break;
                    case W:
                        if (i == range) {
                            tmp = createTile(tmp.getWest());
                            tmp.addRelatedBiomes(Biome.OUT_OF_RANGE);
                        } else {
                            tmp = createTile(tmp.getWest());
                            tmp.addRelatedBiomes(Biome.OCEAN);
                        }
                        break;
                    case S:
                        if (i == range) {
                            tmp = createTile(tmp.getSouth());
                            tmp.addRelatedBiomes(Biome.OUT_OF_RANGE);
                        } else {
                            tmp = createTile(tmp.getSouth());
                            tmp.addRelatedBiomes(Biome.OCEAN);
                        }
                        break;
                    case E:
                        if (i == range) {
                            tmp = createTile(tmp.getEst());
                            tmp.addRelatedBiomes(Biome.OUT_OF_RANGE);
                        } else {
                            tmp = createTile(tmp.getEst());
                            tmp.addRelatedBiomes(Biome.OCEAN);
                        }
                        break;
                }
            }
        }
    }

    public void scan(ArrayList<Biome> associatedBiomes, ArrayList<String> creeks) {
        for (String creek : creeks) {
            this.creeks.put(creek, positionActuelle);
        }

        this.positionActuelle.addRelatedBiomes(associatedBiomes);

        this.createTile(this.positionActuelle.getNorth()).addRelatedBiomes(associatedBiomes);
        this.createTile(this.positionActuelle.getSouth()).addRelatedBiomes(associatedBiomes);
        this.createTile(this.positionActuelle.getEst()).addRelatedBiomes(associatedBiomes);
        this.createTile(this.positionActuelle.getWest()).addRelatedBiomes(associatedBiomes);
        this.createTile(this.createTile(this.positionActuelle.getNorth()).getEst()).addRelatedBiomes(associatedBiomes);
        this.createTile(this.createTile(this.positionActuelle.getNorth()).getWest()).addRelatedBiomes(associatedBiomes);
        this.createTile(this.createTile(this.positionActuelle.getSouth()).getEst()).addRelatedBiomes(associatedBiomes);
        this.createTile(this.createTile(this.positionActuelle.getSouth()).getWest()).addRelatedBiomes(associatedBiomes);
    }

    public Tile getPositionActuelle() {
        return positionActuelle;
    }

    public void land(String creekId, int peoples) {
        changePhase();
        this.positionActuelle = creeks.get(creekId);
    }

    public void move_to(Direction direction) {
        positionActuelle = this.createTile(new Point(positionActuelle.getX() + direction.getMovX(), positionActuelle.getY() + direction.getMovY()));
    }

    public void scout(Direction direction, int altitude, ArrayList<Ressource> ressources) {
        if (!this.containsKey(positionActuelle.getAdjacent(direction))) {
            Tile cible = createTile(positionActuelle.getAdjacent(direction));
            cible.setAltitude(altitude);
            cible.setScouted(true);
            cible.getAssociatedRessources().addAll(ressources);
        }
    }

    public void glimpse(Direction direction, int range, ArrayList<HashMap<Biome, Double>> myReport) {
        /*
        this.positionActuelle.addRelatedBiomes(myReport.get(0));

        Tile tmp = this.positionActuelle;

        for (int i = 1; i < myReport.size(); i++) {
            if(!this.containsKey(positionActuelle.getAdjacent(direction))) {
                tmp = createTile(tmp.getAdjacent(direction));
                tmp.addRelatedBiomes(myReport.get(i));
            }
        }
        */
    }

    public void explore(ArrayList<Ressource> ressources) {
        this.positionActuelle.addAssociatedRessource(ressources);
        this.positionActuelle.setVisited(true);
    }

    @Override
    public String toString() {
        return "IslandMap{" +
                "creeks=" + creeks +
                ", phase=" + phase +
                ", positionActuelle=" + positionActuelle +
                ", directionActuelle=" + directionActuelle +
                '}';
    }

    public HashMap<Ressource, Integer> getCollectedRessources() {
        return collectedRessources;
    }
}