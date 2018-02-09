package fr.unice.polytech.qgl.qda.actions.aerial;

import fr.unice.polytech.qgl.qda.Game.Assignment;
import fr.unice.polytech.qgl.qda.Island.*;
import fr.unice.polytech.qgl.qda.actions.Action;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.DroneStrategy;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.SpecificPattern.EdgeStrategy;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.SpecificPattern.SnakeStrategy;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

/**
 * Echo Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>janv. 5, 2016</pre>
 */
public class EchoTest {
    public Echo echo;
    public IslandMap islandMap;
    public Assignment assignment;

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

    /**
     * Method: buildAction(Direction currentHeading)
     */
    @Test
    public void testBuildAction() throws Exception {
        JSONObject jsonObject = Echo.buildAction(Direction.E);
        assertEquals(jsonObject.getJSONObject("parameters").getString("direction"), "E");
    }

    /**
     * Method: getDirection()
     */
    @Test
    public void testGetDirection() throws Exception {
        echo = new Echo(islandMap, new JSONObject("{ \"action\": \"echo\", \"parameters\": { \"direction\": \"E\" } }"), new JSONObject("{ \"cost\": 1, \"extras\": { \"range\": 2, \"found\": \"GROUND\" }, \"status\": \"OK\" }"));
        assertEquals(echo.getDirection(), Direction.E);
    }

    /**
     * Method: getRange()
     */
    @Test
    public void testGetRange() throws Exception {
        echo = new Echo(islandMap, new JSONObject("{ \"action\": \"echo\", \"parameters\": { \"direction\": \"E\" } }"), new JSONObject("{ \"cost\": 1, \"extras\": { \"range\": 2, \"found\": \"GROUND\" }, \"status\": \"OK\" }"));
        assertEquals(echo.getRange(), 2);
    }

    /**
     * Method: getBiome()
     */
    @Test
    public void testGetBiome() throws Exception {
        echo = new Echo(islandMap, new JSONObject("{ \"action\": \"echo\", \"parameters\": { \"direction\": \"E\" } }"), new JSONObject("{ \"cost\": 1, \"extras\": { \"range\": 2, \"found\": \"GROUND\" }, \"status\": \"OK\" }"));
        assertEquals(echo.getBiome(), Biome.GROUND);
    }

    @Test
    public void testEdgeStrat() {
        EdgeStrategy edgeStrategy1 = new EdgeStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), new HashMap<Ressource, ArrayList<Tile>>(), DroneStrategy.Position.UPPER_LEFT, assignment.getHeading());
        Echo e1 = new Echo(islandMap, new JSONObject("{ \"action\": \"echo\", \"parameters\": { \"direction\": \"E\" } }"), new JSONObject("{ \"cost\": 1, \"extras\": { \"range\": 2, \"found\": \"GROUND\" }, \"status\": \"OK\" }"));
        e1.takeDecisionOnEdgeStrat(edgeStrategy1);
        assertEquals(true, edgeStrategy1.isOnLeft());
        assertEquals(true, edgeStrategy1.isClock());

        EdgeStrategy edgeStrategy2 = new EdgeStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), new HashMap<Ressource, ArrayList<Tile>>(), DroneStrategy.Position.BOTTOM_LEFT, assignment.getHeading());
        Echo e2 = new Echo(islandMap, new JSONObject("{ \"action\": \"echo\", \"parameters\": { \"direction\": \"W\" } }"), new JSONObject("{ \"cost\": 1, \"extras\": { \"range\": 2, \"found\": \"GROUND\" }, \"status\": \"OK\" }"));
        e2.takeDecisionOnEdgeStrat(edgeStrategy2);
        assertEquals(false, edgeStrategy2.isOnLeft());
        assertEquals(true, edgeStrategy2.isClock());

        EdgeStrategy edgeStrategy3 = new EdgeStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), new HashMap<Ressource, ArrayList<Tile>>(), DroneStrategy.Position.UPPER_LEFT, assignment.getHeading());
        Echo e3 = new Echo(islandMap, new JSONObject("{ \"action\": \"echo\", \"parameters\": { \"direction\": \"E\" } }"), new JSONObject("{ \"cost\": 1, \"extras\": { \"range\": 2, \"found\": \"OUT_OF_RANGE\" }, \"status\": \"OK\" }"));
        e3.takeDecisionOnEdgeStrat(edgeStrategy3);
        assertEquals(false, edgeStrategy3.isOnLeft());
        assertEquals(false, edgeStrategy3.isClock());

        EdgeStrategy edgeStrategy4 = new EdgeStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), new HashMap<Ressource, ArrayList<Tile>>(), DroneStrategy.Position.UPPER_LEFT, assignment.getHeading());
        Echo e4 = new Echo(islandMap, new JSONObject("{ \"action\": \"echo\", \"parameters\": { \"direction\": \"W\" } }"), new JSONObject("{ \"cost\": 1, \"extras\": { \"range\": 2, \"found\": \"OUT_OF_RANGE\" }, \"status\": \"OK\" }"));
        e4.takeDecisionOnEdgeStrat(edgeStrategy4);
        assertEquals(true, edgeStrategy4.isOnLeft());
        assertEquals(true, edgeStrategy4.isClock());

        EdgeStrategy edgeStrategy5 = new EdgeStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), new HashMap<Ressource, ArrayList<Tile>>(), DroneStrategy.Position.UPPER_LEFT, assignment.getHeading());
        edgeStrategy5.setInit(false);
        Echo e5 = new Echo(islandMap, new JSONObject("{ \"action\": \"echo\", \"parameters\": { \"direction\": \"W\" } }"), new JSONObject("{ \"cost\": 1, \"extras\": { \"range\": 2, \"found\": \"GROUND\" }, \"status\": \"OK\" }"));
        e5.takeDecisionOnEdgeStrat(edgeStrategy5);
        assertEquals("scan", edgeStrategy5.getBufferActions().removeLast().get("action"));
        assertEquals("fly", edgeStrategy5.getBufferActions().removeLast().get("action"));

        EdgeStrategy edgeStrategy6 = new EdgeStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), new HashMap<Ressource, ArrayList<Tile>>(), DroneStrategy.Position.UPPER_LEFT, assignment.getHeading());
        edgeStrategy6.setInit(false);
        Echo e6 = new Echo(islandMap, new JSONObject("{ \"action\": \"echo\", \"parameters\": { \"direction\": \"W\" } }"), new JSONObject("{ \"cost\": 1, \"extras\": { \"range\": 2, \"found\": \"OUT_OF_RANGE\" }, \"status\": \"OK\" }"));
        e6.takeDecisionOnEdgeStrat(edgeStrategy6);
        assertEquals("scan", edgeStrategy6.getBufferActions().removeLast().get("action"));
        assertEquals("fly", edgeStrategy6.getBufferActions().removeLast().get("action"));
        assertEquals("heading", edgeStrategy6.getBufferActions().removeLast().get("action"));
        assertEquals("heading", edgeStrategy6.getBufferActions().removeLast().get("action"));
        assertEquals("heading", edgeStrategy6.getBufferActions().removeLast().get("action"));
    }

    @Test
    public void testSnakeStrat() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        SnakeStrategy snakeStrategy1 = new SnakeStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), new HashMap<Ressource, ArrayList<Tile>>(), DroneStrategy.Position.BOTTOM_LEFT, assignment.getHeading());
        Echo e1 = new Echo(islandMap, new JSONObject("{ \"action\": \"echo\", \"parameters\": { \"direction\": \"E\" } }"), new JSONObject("{ \"cost\": 1, \"extras\": { \"range\": 2, \"found\": \"GROUND\" }, \"status\": \"OK\" }"));
        e1.takeDecisionOnSnakeStrat(snakeStrategy1);
        assertEquals("scan", snakeStrategy1.getBufferActions().removeLast().get("action"));
        assertEquals("fly", snakeStrategy1.getBufferActions().removeLast().get("action"));
        assertEquals("fly", snakeStrategy1.getBufferActions().removeLast().get("action"));
        assertEquals("fly", snakeStrategy1.getBufferActions().removeLast().get("action"));

        SnakeStrategy snakeStrategy2 = new SnakeStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), new HashMap<Ressource, ArrayList<Tile>>(), DroneStrategy.Position.UPPER_LEFT, assignment.getHeading());
        Echo e2 = new Echo(islandMap, new JSONObject("{ \"action\": \"echo\", \"parameters\": { \"direction\": \"E\" } }"), new JSONObject("{ \"cost\": 1, \"extras\": { \"range\": 2, \"found\": \"OUT_OF_RANGE\" }, \"status\": \"OK\" }"));
        e2.takeDecisionOnSnakeStrat(snakeStrategy2);
        assertEquals(true, snakeStrategy2.isEndOfStrat());

        SnakeStrategy snakeStrategy3 = new SnakeStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), new HashMap<Ressource, ArrayList<Tile>>(), null, assignment.getHeading());
        Echo e3 = new Echo(islandMap, new JSONObject("{ \"action\": \"echo\", \"parameters\": { \"direction\": \"E\" } }"), new JSONObject("{ \"cost\": 1, \"extras\": { \"range\": 2, \"found\": \"OUT_OF_RANGE\" }, \"status\": \"OK\" }"));
        e3.takeDecisionOnSnakeStrat(snakeStrategy3);
        assertEquals("echo", snakeStrategy3.getBufferActions().removeLast().getString("action"));
        assertEquals("heading", snakeStrategy3.getBufferActions().removeLast().getString("action"));
        assertEquals("heading", snakeStrategy3.getBufferActions().removeLast().getString("action"));

        SnakeStrategy snakeStrategy4 = new SnakeStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), new HashMap<Ressource, ArrayList<Tile>>(), DroneStrategy.Position.UPPER_LEFT, assignment.getHeading());
        Echo e4 = new Echo(islandMap, new JSONObject("{ \"action\": \"echo\", \"parameters\": { \"direction\": \"E\" } }"), new JSONObject("{ \"cost\": 1, \"extras\": { \"range\": 2, \"found\": \"OUT_OF_RANGE\" }, \"status\": \"OK\" }"));
        e4.takeDecisionOnSnakeStrat(snakeStrategy4);
        e4.takeDecisionOnSnakeStrat(snakeStrategy4);
        assertEquals("echo", snakeStrategy4.getBufferActions().removeLast().getString("action"));
        assertEquals("heading", snakeStrategy4.getBufferActions().removeLast().getString("action"));
        assertEquals("heading", snakeStrategy4.getBufferActions().removeLast().getString("action"));

        SnakeStrategy snakeStrategy5 = new SnakeStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), new HashMap<Ressource, ArrayList<Tile>>(), null, assignment.getHeading());
        snakeStrategy5.getBufferActions().clear();
        snakeStrategy5.getBufferActions().add(new JSONObject("{ \"action\": \"echo\", \"parameters\": { \"direction\": \"E\" } }"));
        snakeStrategy5.interpretAcknowledgeResult(new JSONObject("{ \"cost\": 3000, \"extras\": { \"range\": 2, \"found\": \"OUT_OF_RANGE\" }, \"status\": \"OK\" }"));
        Echo e5 = new Echo(islandMap, new JSONObject("{ \"action\": \"echo\", \"parameters\": { \"direction\": \"E\" } }"), new JSONObject("{ \"cost\": 1, \"extras\": { \"range\": 2, \"found\": \"OUT_OF_RANGE\" }, \"status\": \"OK\" }"));
        e5.takeDecisionOnSnakeStrat(snakeStrategy5);
        e5.takeDecisionOnSnakeStrat(snakeStrategy5);
        assertEquals(true, snakeStrategy5.isLastStep());
    }
} 
