package fr.unice.polytech.qgl.qda.actions.ground;

import fr.unice.polytech.qgl.qda.Island.Direction;
import fr.unice.polytech.qgl.qda.Island.IslandMap;
import fr.unice.polytech.qgl.qda.Island.Ressource;
import fr.unice.polytech.qgl.qda.actions.ground.Explore;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Explore Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>janv. 5, 2016</pre>
 */
public class ExploreTest {
    IslandMap islandMap=new IslandMap(Direction.E);
    Explore explore;

    @Before
    public void before() throws Exception {
        explore=new Explore(islandMap, new JSONObject("{ \"action\": \"explore\" }"), new JSONObject("{\"cost\": 6,\n" +
                "    \"extras\": {\n" +
                "      \"resources\": [{\n" +
                "        \"amount\": \"HIGH\",\n" +
                "        \"resource\": \"WOOD\",\n" +
                "        \"cond\": \"FAIR\"\n" +
                "      }],\n" +
                "      \"pois\": []\n" +
                "    },\n" +
                "    \"status\": \"OK\"}"));
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: buildAction()
     */
    @Test
    public void testBuildAction() throws Exception {
        JSONObject jsonObject=Explore.buildAction();
        assertEquals(jsonObject.getString("action"), "explore");
    }

    /**
     * Method: getRessources()
     */
    @Test
    public void testGetRessources() throws Exception {
        assertEquals(explore.getRessources(), new ArrayList<Ressource>(){{add(Ressource.WOOD);}});
    }


} 
