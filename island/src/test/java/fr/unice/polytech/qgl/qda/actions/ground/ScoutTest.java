package fr.unice.polytech.qgl.qda.actions.ground;

import fr.unice.polytech.qgl.qda.Game.Assignment;
import fr.unice.polytech.qgl.qda.Island.Direction;
import fr.unice.polytech.qgl.qda.Island.IslandMap;
import fr.unice.polytech.qgl.qda.Island.Ressource;
import fr.unice.polytech.qgl.qda.actions.aerial.Echo;
import fr.unice.polytech.qgl.qda.actions.ground.Scout;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Scout Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>janv. 5, 2016</pre>
 */
public class ScoutTest {
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

    @Test
    public void testConstructor() {
        new Scout(islandMap, new JSONObject("{ \"action\": \"scout\", \"parameters\": { \"direction\": \"N\" } }"), new JSONObject("{ \"cost\": 5, \"extras\": { \"altitude\": 1, \"resources\": [\"FUR\", \"WOOD\"] }, \"status\": \"OK\" }"));
    }


    /**
     * Method: buildAction(Direction currentHeading)
     */
    @Test
    public void testBuildAction() throws Exception {
        JSONObject jsonObject= Scout.buildAction(Direction.E);
        assertEquals(jsonObject.getJSONObject("parameters").getString("direction"), "E");
    }
} 
