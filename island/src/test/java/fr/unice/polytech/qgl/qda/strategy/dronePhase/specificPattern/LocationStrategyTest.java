package fr.unice.polytech.qgl.qda.strategy.dronePhase.specificPattern;

import fr.unice.polytech.qgl.qda.strategy.DronePhase.DroneStrategy;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.SpecificPattern.LocationStrategy;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;

/**
 * @author Maxime
 */
public class LocationStrategyTest {

    JSONObject context;

    @Before
    public void before() throws Exception {

        // Creation d'un nouveau contexte : 12 men, 10.000 PA, 600 Wood, 200 Glass, Heading West
        context = new JSONObject();
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
    }

    @Test
    public void testInit() {
        LocationStrategy strategy1 = new LocationStrategy(context);
        strategy1.init();
        JSONObject firstAction=strategy1.getBufferActions().removeFirst();
        assertEquals("echo", firstAction.get("action"));
        assertEquals("S", firstAction.getJSONObject("parameters").get("direction"));
        JSONObject secondAction=strategy1.getBufferActions().removeFirst();
        assertEquals("echo", secondAction.get("action"));
        assertEquals("N", secondAction.getJSONObject("parameters").get("direction"));
    }

    @Test
    public void testGetNextMove() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        LocationStrategy strategy1 = new LocationStrategy(context);
        strategy1.init();
        strategy1.interpretAcknowledgeResult( new JSONObject("{ \"cost\": 1, \"extras\": { \"range\": 10, \"found\": \"GROUND\" }, \"status\": \"OK\" }"));
        strategy1.getNextMove();
        strategy1.interpretAcknowledgeResult( new JSONObject("{ \"cost\": 1, \"extras\": { \"range\": 20, \"found\": \"GROUND\" }, \"status\": \"OK\" }"));
        strategy1.getNextMove();
        assertEquals(DroneStrategy.Position.BOTTOM_RIGHT, strategy1.getPosition());

        LocationStrategy strategy2 = new LocationStrategy(context);
        strategy2.init();
        strategy2.interpretAcknowledgeResult( new JSONObject("{ \"cost\": 1, \"extras\": { \"range\": 10, \"found\": \"GROUND\" }, \"status\": \"OK\" }"));
        strategy2.getNextMove();
        strategy2.interpretAcknowledgeResult( new JSONObject("{ \"cost\": 1, \"extras\": { \"range\": 10, \"found\": \"GROUND\" }, \"status\": \"OK\" }"));
        strategy2.getNextMove();
        strategy2.interpretAcknowledgeResult( new JSONObject("{ \"cost\": 1, \"extras\": { \"range\": 20, \"found\": \"GROUND\" }, \"status\": \"OK\" }"));

        strategy2.getBufferActions().clear();
        strategy2.getBufferActions().add(new JSONObject("{ \"action\": \"echo\", \"parameters\": { \"direction\": \"E\" } }"));
        strategy2.getNextMove();
        strategy2.interpretAcknowledgeResult( new JSONObject("{ \"cost\": 1, \"extras\": { \"range\": 20, \"found\": \"GROUND\" }, \"status\": \"OK\" }"));
        strategy2.getBufferActions().clear();
        strategy2.getNextMove();

        assertEquals("echo", strategy2.getBufferActions().getFirst().getString("action"));


    }

    @Test
    public void testCompleteMapSize() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        LocationStrategy strategy1 = new LocationStrategy(context);
        strategy1.init();
        strategy1.interpretAcknowledgeResult( new JSONObject("{ \"cost\": 1, \"extras\": { \"range\": 10, \"found\": \"GROUND\" }, \"status\": \"OK\" }"));
        strategy1.getNextMove();
        strategy1.interpretAcknowledgeResult( new JSONObject("{ \"cost\": 1, \"extras\": { \"range\": 20, \"found\": \"GROUND\" }, \"status\": \"OK\" }"));
        strategy1.getNextMove();
        strategy1.completeMapSize(10);
    }

}
