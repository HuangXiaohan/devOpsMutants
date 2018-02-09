package fr.unice.polytech.qgl.qda.strategy.groundPhase.specificPattern;

import fr.unice.polytech.qgl.qda.Game.Assignment;
import fr.unice.polytech.qgl.qda.Island.*;
import fr.unice.polytech.qgl.qda.actions.Action;
import fr.unice.polytech.qgl.qda.strategy.GroundPhase.SpecificPattern.FindStrategy;
import fr.unice.polytech.qgl.qda.strategy.GroundPhase.SpecificPattern.HuntedBiome;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * @author Maxime
 */
public class HuntedBiomeTest {

    IslandMap islandMap;
    Assignment assignment;
    HashMap<Ressource, ArrayList<Tile>> tileList;

    @Before
    public void before() throws Exception {
        islandMap=new IslandMap(Direction.E);

        // Creation d'un nouveau contexte : 12 men, 10.000 PA, 600 Wood, 200 Glass, Heading West
        JSONObject context = new JSONObject();
        context.put("men", 12);
        context.put("budget", 10000);

        JSONObject wood = new JSONObject();
        wood.put("amount", 600);
        wood.put("resource", "WOOD");

        JSONObject glass = new JSONObject();
        glass.put("amount", 200);
        glass.put("resource", "GLASS");

        JSONArray ressources = new JSONArray();
        ressources.put(wood);
        ressources.put(glass);

        context.put("contracts", ressources);

        context.put("heading", "W");

        assignment = new Assignment(context);

        tileList = new HashMap<>();

        ArrayList<Tile> QuartzTiles = new ArrayList<>();
        for (int y = 1; y <= 5; y++) {
            for (int x = 1; x <= 5; x++) {
                islandMap.actualizePosition(x, y);
                ArrayList<Biome> quartzbiome = new ArrayList<>();
                quartzbiome.add(Biome.TEMPERATE_DESERT);
                islandMap.scan(quartzbiome, new ArrayList<>());
                QuartzTiles.add(islandMap.createTile(new Point(x, y)));
            }
        }

        ArrayList<Tile> WoodTiles = new ArrayList<>();
        for (int y = 1; y <= 5; y++) {
            for (int x = -1; x >= -5; x--) {
                islandMap.actualizePosition(x, y);
                ArrayList<Biome> woodBiomes = new ArrayList<>();
                woodBiomes.add(Biome.MANGROVE);
                islandMap.scan(woodBiomes, new ArrayList<>());
                WoodTiles.add(islandMap.createTile(new Point(x, y)));
            }
        }

        tileList.put(Ressource.QUARTZ, QuartzTiles);
        tileList.put(Ressource.WOOD, WoodTiles);

        islandMap.actualizePosition(0,0);
    }

    @Test
    public void testNextStep() {
        ArrayList<String> creeks = new ArrayList<>();
        creeks.add("ID");
        islandMap.scan(new ArrayList<>(), creeks);
        FindStrategy findStategy1 = new FindStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), tileList);
        findStategy1.findNearestTile(islandMap.getPositionActuelle());
        HuntedBiome strategy1 = findStategy1.getHuntedBiome();
        strategy1.nextStep();

        islandMap.scan(new ArrayList<>(), creeks);
        FindStrategy findStategy2 = new FindStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), tileList);
        findStategy2.findNearestTile(islandMap.getPositionActuelle());
        HuntedBiome strategy2 = findStategy2.getHuntedBiome();
        strategy2.setReverse(true);
        strategy2.nextStep();
    }

    @Test
    public void testRunWest() {
        ArrayList<String> creeks = new ArrayList<>();
        creeks.add("ID");
        islandMap.scan(new ArrayList<>(), creeks);
        FindStrategy findStategy1 = new FindStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), tileList);
        findStategy1.findNearestTile(islandMap.getPositionActuelle());
        HuntedBiome strategy1 = findStategy1.getHuntedBiome();
        strategy1.run(Direction.W);
        assertEquals(Direction.W, strategy1.getDirection());
    }

    @Test
    public void testRunNorth() {
        ArrayList<String> creeks = new ArrayList<>();
        creeks.add("ID");
        islandMap.scan(new ArrayList<>(), creeks);
        FindStrategy findStategy1 = new FindStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), tileList);
        findStategy1.findNearestTile(islandMap.getPositionActuelle());
        HuntedBiome strategy1 = findStategy1.getHuntedBiome();
        strategy1.run(Direction.N);
        assertEquals(Direction.E, strategy1.getDirection());
    }

    @Test
    public void testChange() {
        ArrayList<String> creeks = new ArrayList<>();
        creeks.add("ID");
        islandMap.scan(new ArrayList<>(), creeks);
        FindStrategy findStategy1 = new FindStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), tileList);
        findStategy1.findNearestTile(islandMap.getPositionActuelle());
        HuntedBiome strategy1 = findStategy1.getHuntedBiome();
        strategy1.change(Direction.W);
        strategy1.change(Direction.W);
        strategy1.change(Direction.W);
        strategy1.change(Direction.W);
        strategy1.change(Direction.W);
        strategy1.change(Direction.W);
    }
}
