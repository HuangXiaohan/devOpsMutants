package fr.unice.polytech.qgl.qda.strategy.dronePhase.specificPattern;

import fr.unice.polytech.qgl.qda.Game.Assignment;
import fr.unice.polytech.qgl.qda.Island.*;
import fr.unice.polytech.qgl.qda.actions.Action;
import fr.unice.polytech.qgl.qda.actions.aerial.Echo;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.DroneStrategy;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.SpecificPattern.SnakeStrategy;
import fr.unice.polytech.qgl.qda.strategy.GroundPhase.SpecificPattern.FindStrategy;
import fr.unice.polytech.qgl.qda.strategy.Strategy;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

/**
 * Created by spong on 2016/3/24.
 */
public class SnakeStrategyTset {
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
    public void TestGetNextStrategy(){
        SnakeStrategy snakeStrategy1 = new SnakeStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), tileList, DroneStrategy.Position.BOTTOM_LEFT, assignment.getHeading());
        ArrayList<String> creeks = new ArrayList<>();
        creeks.add("ID");
        islandMap.scan(new ArrayList<>(), creeks);
        Strategy strategy1=snakeStrategy1.getNextStrategy();
        assertEquals(FindStrategy.class.toGenericString(),strategy1.getClass().toGenericString());
    }
}
