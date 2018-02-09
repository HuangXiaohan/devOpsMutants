package fr.unice.polytech.qgl.qda.actions;

import fr.unice.polytech.qgl.qda.Island.Biome;
import fr.unice.polytech.qgl.qda.Island.Direction;
import fr.unice.polytech.qgl.qda.Island.IslandMap;
import fr.unice.polytech.qgl.qda.actions.Action;
import fr.unice.polytech.qgl.qda.actions.aerial.Echo;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Action Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>janv. 5, 2016</pre>
 */
public class ActionTest {
    IslandMap islandMap=new IslandMap(Direction.E);
    Action action;
    Echo echo;

    @Before
    public void before() throws Exception {
        echo=new Echo(islandMap, new JSONObject("{ \"action\": \"echo\", \"parameters\": { \"direction\": \"E\" } }"), new JSONObject("{ \"cost\": 1, \"extras\": { \"range\": 2, \"found\": \"GROUND\" }, \"status\": \"OK\" }"));
        assertEquals(echo.getDirection(), Direction.E);
        assertEquals(echo.getRange(), 2);
        assertEquals(echo.getBiome(), Biome.GROUND);
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getCost()
     */
    @Test
    public void testGetCost() throws Exception {
        assertEquals(echo.getCost(), 1);
    }

    /**
     * Method: getIslandMap()
     */
    @Test
    public void testGetIslandMap() throws Exception {
        assertNotNull(echo.getIslandMap());
    }

    /**
     * Method: isStatus()
     */
    @Test
    public void testIsStatus() throws Exception {
        assertEquals(echo.isStatus(), true);
    }


} 
