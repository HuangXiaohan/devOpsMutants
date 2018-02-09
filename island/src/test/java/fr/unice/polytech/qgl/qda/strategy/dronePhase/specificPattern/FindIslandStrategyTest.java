package fr.unice.polytech.qgl.qda.strategy.dronePhase.specificPattern;

import fr.unice.polytech.qgl.qda.Game.Assignment;
import fr.unice.polytech.qgl.qda.Island.Direction;
import fr.unice.polytech.qgl.qda.Island.IslandMap;
import fr.unice.polytech.qgl.qda.Island.Ressource;
import fr.unice.polytech.qgl.qda.Island.Tile;
import fr.unice.polytech.qgl.qda.actions.Action;
import fr.unice.polytech.qgl.qda.actions.aerial.Echo;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.DroneStrategy;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.SpecificPattern.FindIslandStrategy;
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
 * @author Maxime
 */
public class FindIslandStrategyTest {
    IslandMap islandMap;
    Assignment assignment;

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
    }

    @Test
    public void testInit() {
        FindIslandStrategy strategy1=new FindIslandStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), new HashMap<Ressource, ArrayList<Tile>>(), DroneStrategy.Position.BOTTOM_RIGHT, assignment.getHeading());
        strategy1.init();
        assertEquals("echo", strategy1.getBufferActions().removeFirst().get("action"));
    }

    @Test
    public void testStartStair() {
        FindIslandStrategy strategy1=new FindIslandStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), new HashMap<Ressource, ArrayList<Tile>>(), DroneStrategy.Position.BOTTOM_RIGHT, assignment.getHeading());
        strategy1.startStair(10);
        assertEquals("heading", strategy1.getBufferActions().removeFirst().get("action"));
        assertEquals("echo", strategy1.getBufferActions().removeFirst().get("action"));

        strategy1.startStair(4);
        assertEquals("heading", strategy1.getBufferActions().removeFirst().get("action"));
        assertEquals("echo", strategy1.getBufferActions().removeFirst().get("action"));
    }

    @Test
    public void testLastStep() {
        FindIslandStrategy strategy1=new FindIslandStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), new HashMap<Ressource, ArrayList<Tile>>(), DroneStrategy.Position.BOTTOM_RIGHT, assignment.getHeading());
        strategy1.lastStep(3);
        assertEquals("heading", strategy1.getBufferActions().removeFirst().get("action"));
        assertEquals("fly", strategy1.getBufferActions().removeFirst().get("action"));
    }

    @Test
    public void testFindEarth() {
        FindIslandStrategy strategy1=new FindIslandStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), new HashMap<Ressource, ArrayList<Tile>>(), DroneStrategy.Position.BOTTOM_RIGHT, assignment.getHeading());
        strategy1.findEarth(true, 3);
        assertEquals("heading", strategy1.getBufferActions().removeFirst().get("action"));
        assertEquals("fly", strategy1.getBufferActions().removeFirst().get("action"));

        FindIslandStrategy strategy2=new FindIslandStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), new HashMap<Ressource, ArrayList<Tile>>(), DroneStrategy.Position.BOTTOM_RIGHT, assignment.getHeading());
        strategy2.findEarth(false, 3);
        assertEquals("heading", strategy2.getBufferActions().removeFirst().get("action"));
        assertEquals("echo", strategy2.getBufferActions().removeFirst().get("action"));

        strategy2.findEarth(false, 3);
        assertEquals("heading", strategy2.getBufferActions().removeFirst().get("action"));
        assertEquals("echo", strategy2.getBufferActions().removeFirst().get("action"));
    }

    @Test
    public void testGetNextMove() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        FindIslandStrategy strategy1=new FindIslandStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), new HashMap<Ressource, ArrayList<Tile>>(), DroneStrategy.Position.BOTTOM_RIGHT, assignment.getHeading());
        strategy1.getBufferActions().add(new JSONObject("{ \"action\": \"echo\", \"parameters\": { \"direction\": \"E\" } }"));
        strategy1.interpretAcknowledgeResult( new JSONObject("{ \"cost\": 1, \"extras\": { \"range\": 2, \"found\": \"GROUND\" }, \"status\": \"OK\" }"));
        strategy1.getNextMove();

        strategy1.interpretAcknowledgeResult( new JSONObject("{ \"cost\": 1, \"extras\": { \"range\": 2, \"found\": \"OUT_OF_RANGE\" }, \"status\": \"OK\" }"));
        strategy1.getNextMove();

        strategy1.interpretAcknowledgeResult( new JSONObject("{ \"cost\": 1, \"extras\": { \"range\": 2, \"found\": \"GROUND\" }, \"status\": \"OK\" }"));
        strategy1.getNextMove();
    }
}
