package fr.unice.polytech.qgl.qda.actions.ground;

import fr.unice.polytech.qgl.qda.Island.Direction;
import fr.unice.polytech.qgl.qda.Island.IslandMap;
import fr.unice.polytech.qgl.qda.actions.ground.Move_to;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertEquals;

/**
 * Move_to Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>janv. 5, 2016</pre>
 */
public class Move_toTest {
    IslandMap islandMap=new IslandMap(Direction.E);
    Move_to move_to;

    @Before
    public void before() throws Exception {
        move_to=new Move_to(islandMap, new JSONObject("{ \"action\": \"move_to\", \"parameters\": { \"direction\": \"N\" } }"), new JSONObject("{ \"cost\": 6, \"extras\": { }, \"status\": \"OK\" }"));
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: buildAction(Direction currentHeading)
     */
    @Test
    public void testBuildAction() throws Exception {
        JSONObject jsonObject=Move_to.buildAction(Direction.N);
        assertEquals(jsonObject.getJSONObject("parameters").getString("direction"), "N");
    }

    /**
     * Method: getDirection()
     */
    @Test
    public void testGetDirection() throws Exception {
        assertEquals(move_to.getDirection(), Direction.N);
    }


} 
