package fr.unice.polytech.qgl.qda.actions.aerial;

import fr.unice.polytech.qgl.qda.Game.Assignment;
import fr.unice.polytech.qgl.qda.Island.*;
import fr.unice.polytech.qgl.qda.actions.Action;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.DroneStrategy;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.SpecificPattern.EdgeStrategy;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.SpecificPattern.SnakeStrategy;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

/**
 * Scan Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>janv. 5, 2016</pre>
 */
public class ScanTest {
    IslandMap islandMap;
    Assignment assignment;
    Scan scan;

    @Before
    public void before() throws Exception {
        islandMap = new IslandMap(Direction.E);

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
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: buildAction()
     */
    @Test
    public void testBuildAction() throws Exception {
        JSONObject jsonObject = Scan.buildAction();
        assertEquals(jsonObject.getString("action"), "scan");
    }

    /**
     * Method: getBiomes()
     */
    @Test
    public void testGetBiomes() throws Exception {
        scan = new Scan(islandMap, new JSONObject("{ \"action\": \"scan\" }"), new JSONObject("{\"cost\": 2, \"extras\": { \"biomes\": [\"GLACIER\", \"ALPINE\"], \"creeks\": []}, \"status\": \"OK\"}"));

        assertEquals(scan.getBiomes(), new ArrayList<Biome>() {{
            add(Biome.GLACIER);
            add(Biome.ALPINE);
        }});
    }

    /**
     * Method: getCreeks()
     */
    @Test
    public void testGetCreeks() throws Exception {
        scan = new Scan(islandMap, new JSONObject("{ \"action\": \"scan\" }"), new JSONObject("{\"cost\": 2, \"extras\": { \"biomes\": [\"BEACH\"], \"creeks\": [\"id\"]}, \"status\": \"OK\"}\n"));

        assertEquals(scan.getCreeks(), new ArrayList<String>() {{
            add("id");
        }});
    }

    @Test
    public void testTakeDecisionOnEdgeStrat() {
        EdgeStrategy edgeStrategy1 = new EdgeStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), new HashMap<Ressource, ArrayList<Tile>>(), DroneStrategy.Position.UPPER_LEFT, assignment.getHeading());
        Scan s1=new Scan(islandMap, new JSONObject("{ \"action\": \"scan\" }"), new JSONObject("{\"cost\": 2, \"extras\": { \"biomes\": [\"OCEAN\"], \"creeks\": []}, \"status\": \"OK\"}"));
        s1.takeDecisionOnEdgeStrat(edgeStrategy1);
        assertEquals(true, edgeStrategy1.isDown());
        s1.takeDecisionOnEdgeStrat(edgeStrategy1);
        assertEquals(false, edgeStrategy1.isDown());
        assertEquals(false, edgeStrategy1.isUp());

        EdgeStrategy edgeStrategy2 = new EdgeStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), new HashMap<Ressource, ArrayList<Tile>>(), DroneStrategy.Position.UPPER_LEFT, assignment.getHeading());
        Scan s2=new Scan(islandMap, new JSONObject("{ \"action\": \"scan\" }"), new JSONObject("{\"cost\": 2, \"extras\": { \"biomes\": [\"OCEAN\"], \"creeks\": []}, \"status\": \"OK\"}"));
        s2.takeDecisionOnEdgeStrat(edgeStrategy2);
        assertEquals(true, edgeStrategy2.isDown());
        Scan s3=new Scan(islandMap, new JSONObject("{ \"action\": \"scan\" }"), new JSONObject("{\"cost\": 2, \"extras\": { \"biomes\": [\"BEACH\"], \"creeks\": []}, \"status\": \"OK\"}"));
        s3.takeDecisionOnEdgeStrat(edgeStrategy2);
        assertEquals(false, edgeStrategy2.isDown());
        assertEquals(false, edgeStrategy2.isUp());
        s3.takeDecisionOnEdgeStrat(edgeStrategy2);
        assertEquals(false, edgeStrategy2.isDown());
        assertEquals(true, edgeStrategy2.isUp());
        s3.takeDecisionOnEdgeStrat(edgeStrategy2);
        assertEquals(false, edgeStrategy2.isDown());
        assertEquals(false, edgeStrategy2.isUp());

        EdgeStrategy edgeStrategy3 = new EdgeStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), new HashMap<Ressource, ArrayList<Tile>>(), DroneStrategy.Position.UPPER_LEFT, assignment.getHeading());
        Scan s4=new Scan(islandMap, new JSONObject("{ \"action\": \"scan\" }"), new JSONObject("{\"cost\": 2, \"extras\": { \"biomes\": [\"OCEAN\", \"BEACH\"], \"creeks\": []}, \"status\": \"OK\"}"));
        s4.takeDecisionOnEdgeStrat(edgeStrategy3);
        assertEquals("echo", edgeStrategy3.getBufferActions().removeLast().getString("action"));
        assertEquals(false, edgeStrategy2.isDown());
        assertEquals(false, edgeStrategy2.isUp());
    }

    @Test
    public void testTakeDecisionOnSnakeStrat() {
        SnakeStrategy snakeStrategy1 = new SnakeStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), new HashMap<Ressource, ArrayList<Tile>>(), DroneStrategy.Position.BOTTOM_LEFT, assignment.getHeading());
        Scan s1=new Scan(islandMap, new JSONObject("{ \"action\": \"scan\" }"), new JSONObject("{\"cost\": 2, \"extras\": { \"biomes\": [\"OCEAN\"], \"creeks\": []}, \"status\": \"OK\"}"));
        s1.takeDecisionOnSnakeStrat(snakeStrategy1);
        Scan s2=new Scan(islandMap, new JSONObject("{ \"action\": \"scan\" }"), new JSONObject("{\"cost\": 2, \"extras\": { \"biomes\": [\"LAKE\" ,\"BEACH\"], \"creeks\": []}, \"status\": \"OK\"}"));
        s2.takeDecisionOnSnakeStrat(snakeStrategy1);
        Scan s3=new Scan(islandMap, new JSONObject("{ \"action\": \"scan\" }"), new JSONObject("{\"cost\": 2, \"extras\": { \"biomes\": [\"GLACIER\"], \"creeks\": []}, \"status\": \"OK\"}"));
        s3.takeDecisionOnSnakeStrat(snakeStrategy1);
    }
} 
