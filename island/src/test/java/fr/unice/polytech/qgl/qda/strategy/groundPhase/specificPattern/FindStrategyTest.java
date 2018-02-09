package fr.unice.polytech.qgl.qda.strategy.groundPhase.specificPattern;

import fr.unice.polytech.qgl.qda.Game.Assignment;
import fr.unice.polytech.qgl.qda.Island.*;
import fr.unice.polytech.qgl.qda.actions.Action;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.DroneStrategy;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.SpecificPattern.FindIslandStrategy;
import fr.unice.polytech.qgl.qda.strategy.GroundPhase.SpecificPattern.FindStrategy;
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
public class FindStrategyTest {

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
    public void testFindStrategyConstructor() {
        ArrayList<String> creeks = new ArrayList<>();
        creeks.add("ID");
        islandMap.scan(new ArrayList<>(), creeks);
        FindStrategy strategy1 = new FindStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(),tileList);
        assertEquals("land", strategy1.getBufferActions().removeFirst().get("action"));
    }

    @Test
    public void testFindNearestTile() {
        ArrayList<String> creeks = new ArrayList<>();
        creeks.add("ID");
        islandMap.scan(new ArrayList<>(), creeks);
        FindStrategy strategy1 = new FindStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), tileList);
        strategy1.findNearestTile(islandMap.getPositionActuelle());
        assertEquals(islandMap.createTile(new Point(-1, 1)), strategy1.getHuntedBiome().getTile());
    }

    @Test
    public void testStartMove() {
        ArrayList<String> creeks = new ArrayList<>();
        creeks.add("ID");
        islandMap.scan(new ArrayList<>(), creeks);
        FindStrategy strategy1 = new FindStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), tileList);
        strategy1.setCreek(islandMap.getPositionActuelle());
        strategy1.findNearestTile(islandMap.getPositionActuelle());
        strategy1.startMove();
        assertEquals("move_to", strategy1.getBufferActions().removeFirst().get("action"));
        assertEquals("move_to", strategy1.getBufferActions().removeFirst().get("action"));
        assertEquals("move_to", strategy1.getBufferActions().removeFirst().get("action"));
        assertEquals("explore", strategy1.getBufferActions().removeFirst().get("action"));

    }

    @Test
    public void testUpdateList() {
        ArrayList<String> creeks = new ArrayList<>();
        creeks.add("ID");
        islandMap.scan(new ArrayList<>(), creeks);
        FindStrategy strategy1 = new FindStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), tileList);
        strategy1.setCreek(islandMap.getPositionActuelle());
        strategy1.findNearestTile(islandMap.getPositionActuelle());
        strategy1.getFindedRessource().add(Ressource.WOOD);
        strategy1.updateList(2000);
        assertEquals(0, strategy1.getFindedRessource().size());
    }

    @Test
    public void testActualizeList() {
        ArrayList<String> creeks = new ArrayList<>();
        creeks.add("ID");
        islandMap.scan(new ArrayList<>(), creeks);
        FindStrategy strategy1 = new FindStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), tileList);
        strategy1.setCreek(islandMap.getPositionActuelle());

        islandMap.actualizePosition(-1, 1);
        ArrayList<Ressource> woodRessource = new ArrayList<>();
        woodRessource.add(Ressource.WOOD);
        islandMap.createTile(new Point(-1,1)).addAssociatedRessource(woodRessource);
        strategy1.actualiseList(islandMap.getPositionActuelle());

    }

    @Test
    public void testNotFoundRessource() {
        ArrayList<String> creeks = new ArrayList<>();
        creeks.add("ID");
        islandMap.scan(new ArrayList<>(), creeks);
        FindStrategy strategy1 = new FindStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), tileList);
        strategy1.findNearestTile(islandMap.getPositionActuelle());
        strategy1.getBufferActions().clear();
        strategy1.getHuntedBiome().setReverse(true);
        strategy1.notFoundRessource();
        assertEquals("move_to", strategy1.getBufferActions().removeFirst().get("action"));
        assertEquals("move_to", strategy1.getBufferActions().removeFirst().get("action"));
        assertEquals("scout", strategy1.getBufferActions().removeFirst().get("action"));
    }
}
