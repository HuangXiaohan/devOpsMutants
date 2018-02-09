package fr.unice.polytech.qgl.qda.actions.aerial;

import fr.unice.polytech.qgl.qda.Island.Direction;
import fr.unice.polytech.qgl.qda.Island.IslandMap;
import fr.unice.polytech.qgl.qda.actions.aerial.Heading;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertEquals;

/**
 * Heading Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>janv. 5, 2016</pre>
 */
public class HeadingTest {
    IslandMap islandMap=new IslandMap(Direction.E);
    Heading heading;

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: buildAction(Direction heading)
     */
    @Test
    public void testBuildAction() throws Exception {
        JSONObject jsonObject=Heading.buildAction(Direction.E);
        assertEquals(jsonObject.getString("action"), "heading");
        assertEquals(jsonObject.getJSONObject("parameters").getString("direction"), "E");
    }

    /**
     * Method: getDirection()
     */
    @Test
    public void testGetDirection() throws Exception {
        heading=new Heading(islandMap, new JSONObject("{ \"action\": \"heading\", \"parameters\": { \"direction\": \"N\" } }"), new JSONObject("{ \"cost\": 4, \"extras\": {}, \"status\": \"OK\" }"));
        assertEquals(heading.getDirection(), Direction.N);
    }


} 
