package fr.unice.polytech.qgl.qda.actions.ground;

import fr.unice.polytech.qgl.qda.Game.Assignment;
import fr.unice.polytech.qgl.qda.Island.*;
import fr.unice.polytech.qgl.qda.actions.ground.Glimpse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * Glimpse Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>janv. 5, 2016</pre>
 */
public class GlimpseTest {

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
    public void testDefaultConstructor() {

        JSONObject response=new JSONObject();
        response.put("cost", 3);
        JSONObject extras=new JSONObject();
        extras.put("asked_range", 4);

        JSONArray reports = new JSONArray();

        JSONArray report1 = new JSONArray();

        JSONArray beach1=new JSONArray();
        beach1.put("BEACH");
        beach1.put(59.35);


        JSONArray ocean1=new JSONArray();
        ocean1.put("OCEAN");
        ocean1.put(40.65);

        report1.put(beach1);
        report1.put(ocean1);

        JSONArray report2=new JSONArray();

        JSONArray beach2=new JSONArray();
        beach2.put("BEACH");
        beach2.put(59.35);


        JSONArray ocean2=new JSONArray();
        ocean2.put("OCEAN");
        ocean2.put(40.65);

        report2.put(beach2);
        report2.put(ocean2);

        JSONArray report3=new JSONArray();

        JSONArray beach3=new JSONArray();
        beach3.put("BEACH");

        JSONArray ocean3=new JSONArray();
        ocean3.put("OCEAN");

        report3.put(ocean3);
        report3.put(beach3);

        JSONArray report4=new JSONArray();

        JSONArray ocean4=new JSONArray();
        ocean4.put("OCEAN");

        report4.put(ocean4);

        reports.put(report1);
        reports.put(report2);
        reports.put(report3);
        reports.put(report4);

        extras.put("report", reports);

        response.put("extras", extras);

        response.put("status", "OK");

        Glimpse glimpse = new Glimpse(islandMap, new JSONObject("{ \"action\": \"glimpse\", \"parameters\": { \"direction\": \"N\", \"range\": 2 } }"), response);
    }

    @Test
    public void testBuildAction() throws Exception {
        JSONObject jsonObject=Glimpse.buildAction(Direction.E, 2);
        assertEquals(jsonObject.getJSONObject("parameters").getString("direction"), "E");
        assertEquals(jsonObject.getJSONObject("parameters").getInt("range"), 2);
    }


} 
