package fr.unice.polytech.qgl.qda.actions.aerial;

import fr.unice.polytech.qgl.qda.Game.Assignment;
import fr.unice.polytech.qgl.qda.Island.Direction;
import fr.unice.polytech.qgl.qda.Island.IslandMap;
import fr.unice.polytech.qgl.qda.Island.Ressource;
import fr.unice.polytech.qgl.qda.Island.Tile;
import fr.unice.polytech.qgl.qda.actions.Action;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.DroneStrategy;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.SpecificPattern.EdgeStrategy;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.SpecificPattern.SnakeStrategy;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

/**
 * Fly Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>janv. 5, 2016</pre>
 */
public class FlyTest {
    private Fly fly;
    private IslandMap islandMap;
    private Assignment assignment;

    @Before
    public void before() throws Exception {
        islandMap = new IslandMap(Direction.W);

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
     * Method: buildAction()
     */
    @Test
    public void testBuildAction() throws Exception {
        JSONObject jsonObject = Fly.buildAction();
        assertEquals(jsonObject.getString("action"), "fly");
    }

    @Test
    public void testFly() {
        IslandMap map = new IslandMap(Direction.E);
        map.actualizePosition(0,0);
        new Fly(map, Fly.buildAction(), new JSONObject("{ \"cost\": 3, \"extras\": {}, \"status\": \"OK\" }"));
        assertEquals(3, map.getPositionActuelle().getX());
        assertEquals(0, map.getPositionActuelle().getY());
    }

    @Test
    public void testTakeDecisionOnEdgeStrat() {
        EdgeStrategy edgeStrategy1 = new EdgeStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), new HashMap<Ressource, ArrayList<Tile>>(), DroneStrategy.Position.UPPER_LEFT, assignment.getHeading());
        Fly fly=new Fly(islandMap, Fly.buildAction(), new JSONObject("{ \"cost\": 3, \"extras\": {}, \"status\": \"OK\" }"));
        fly.takeDecisionOnEdgeStrat(edgeStrategy1);
    }

    @Test
    public void testTakeDecisionOnSnakeStrat() {
        SnakeStrategy snakeStrategy1 = new SnakeStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), new HashMap<Ressource, ArrayList<Tile>>(), DroneStrategy.Position.BOTTOM_LEFT, assignment.getHeading());
        Fly fly=new Fly(islandMap, Fly.buildAction(), new JSONObject("{ \"cost\": 3, \"extras\": {}, \"status\": \"OK\" }"));
        fly.takeDecisionOnSnakeStrat(snakeStrategy1);
    }
}
