package fr.unice.polytech.qgl.qda.actions.both;

import fr.unice.polytech.qgl.qda.Island.Direction;
import fr.unice.polytech.qgl.qda.Island.IslandMap;
import fr.unice.polytech.qgl.qda.actions.ground.Stop;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertEquals;

/**
 * Stop Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>janv. 5, 2016</pre>
 */
public class StopTest {
    IslandMap islandMap=new IslandMap(Direction.E);
    Stop stop;

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
        stop=new Stop(islandMap, new JSONObject("{ \"action\": \"stop\" }"), new JSONObject("{ \"cost\": 3, \"extras\": {}, \"status\": \"OK\" }"));
        stop.strategy();
    }

    /**
     * Method: buildAction()
     */
    @Test
    public void testBuildAction() throws Exception {
        JSONObject jsonObject=Stop.buildAction();
        assertEquals(jsonObject.getString("action"), "stop");
    }


} 
