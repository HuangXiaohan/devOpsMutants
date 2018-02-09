package fr.unice.polytech.qgl.qda.actions.both;

import fr.unice.polytech.qgl.qda.Island.Direction;
import fr.unice.polytech.qgl.qda.Island.IslandMap;
import fr.unice.polytech.qgl.qda.actions.ground.Land;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Land Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>janv. 5, 2016</pre>
 */
public class LandTest {

    IslandMap islandMap=new IslandMap(Direction.E);
    Land land;

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: strategy()
     */
    @Test
    public void testStrategy() throws Exception {
        land=new Land(islandMap, new JSONObject("{ \"action\": \"land\", \"parameters\": { \"creek\": \"id\", \"people\": 42 }}"), new JSONObject("{ \"cost\": 15, \"extras\": { }, \"status\": \"OK\" }"));
        assertEquals(land.getCreek(), "id");
        assertEquals(land.getPeoples(), 42);
    }

    /**
     * Method: buildAction(String creekID, int people)
     */
    @Test
    public void testBuildAction() throws Exception {
        JSONObject jsonObject=Land.buildAction("id", 42);
        assertEquals(jsonObject.getJSONObject("parameters").get("creek"), "id");
        assertEquals(jsonObject.getJSONObject("parameters").get("people"), 42);
    }

    /**
     * Method: getCreek()
     */
    @Test
    public void testGetCreek() throws Exception {
        land=new Land(islandMap, new JSONObject("{ \"action\": \"land\", \"parameters\": { \"creek\": \"id\", \"people\": 42 }}"), new JSONObject("{ \"cost\": 15, \"extras\": { }, \"status\": \"OK\" }"));
        assertEquals(land.getCreek(), "id");

        land.strategy();
    }

    /**
     * Method: getPeoples()
     */
    @Test
    public void testGetPeoples() throws Exception {
        land=new Land(islandMap, new JSONObject("{ \"action\": \"land\", \"parameters\": { \"creek\": \"id\", \"people\": 42 }}"), new JSONObject("{ \"cost\": 15, \"extras\": { }, \"status\": \"OK\" }"));
        assertEquals(land.getPeoples(), 42);
    }


} 
