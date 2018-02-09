package fr.unice.polytech.qgl.qda.strategy.dronePhase.specificPattern;

import fr.unice.polytech.qgl.qda.Game.Assignment;
import fr.unice.polytech.qgl.qda.Island.Direction;
import fr.unice.polytech.qgl.qda.Island.IslandMap;
import fr.unice.polytech.qgl.qda.Island.Ressource;
import fr.unice.polytech.qgl.qda.Island.Tile;
import fr.unice.polytech.qgl.qda.actions.Action;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.DroneStrategy;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.SpecificPattern.EdgeStrategy;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

/**
 * @author Maxime
 */
public class EdgeStrategyTest {
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
        EdgeStrategy edgeStrategy1 = new EdgeStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), new HashMap<Ressource, ArrayList<Tile>>(), DroneStrategy.Position.BOTTOM_RIGHT, assignment.getHeading());
        edgeStrategy1.init();
        assertEquals(true, edgeStrategy1.isClock());
        edgeStrategy1.headingNext();
        assertEquals("fly", edgeStrategy1.getBufferActions().removeLast().get("action"));
        assertEquals("heading", edgeStrategy1.getBufferActions().removeLast().get("action"));
        assertEquals("heading", edgeStrategy1.getBufferActions().removeLast().get("action"));
        assertEquals("heading", edgeStrategy1.getBufferActions().removeLast().get("action"));

        EdgeStrategy edgeStrategy2 = new EdgeStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), new HashMap<Ressource, ArrayList<Tile>>(), DroneStrategy.Position.UPPER_RIGHT, assignment.getHeading());
        edgeStrategy2.setOnLeft(true);
        edgeStrategy2.init();
        assertEquals(true, edgeStrategy2.isClock());
        edgeStrategy2.headingPrevious();
        assertEquals("fly", edgeStrategy2.getBufferActions().removeLast().get("action"));
        assertEquals("heading", edgeStrategy2.getBufferActions().removeLast().get("action"));
        assertEquals("heading", edgeStrategy2.getBufferActions().removeLast().get("action"));
        assertEquals("heading", edgeStrategy2.getBufferActions().removeLast().get("action"));

    }
}
