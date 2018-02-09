package fr.unice.polytech.qgl.qda.Game;

import fr.unice.polytech.qgl.qda.Island.Direction;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by spong on 2015/12/2.
 */
public class AssignmentTest {

    JSONObject contrat;
    Assignment a;

    JSONObject contrat2;
    Assignment b;

    @Before
    public void setUp() {
        contrat = new JSONObject();
        contrat.put("men", 12);
        contrat.put("budget", 10000);

        JSONObject wood = new JSONObject();
        wood.put("amount", 600);
        wood.put("resource", "WOOD");

        JSONObject glass = new JSONObject();
        glass.put("amount", 200);
        glass.put("resource", "GLASS");

        JSONArray ressources = new JSONArray();
        ressources.put(wood);
        ressources.put(glass);

        contrat.put("contracts", ressources);

        contrat.put("heading", "W");

        a = new Assignment(contrat);

        //-----------------------------------------

        contrat2=new JSONObject();

        contrat2.put("men",7);

        contrat2.put("budget",8888);

        contrat2.put("heading","E");

        JSONObject fish =new JSONObject();
        fish.put("amount",199);
        fish.put("resource","FISH");

        JSONObject flower=new JSONObject();
        flower.put("amount",333);
        flower.put("resource","FLOWER");

        JSONArray resource=new JSONArray();
        resource.put(flower);
        resource.put(fish);
        resource.put(wood);

        contrat2.put("contracts",resource);

        b=new Assignment(contrat2);

    }

    @Test
    public void testGetBudget() {
        assertEquals(10000, a.getBudget());
        assertEquals(8888, b.getBudget());
    }

    @Test
    public void testGetHeading() {

        assertEquals(Direction.W, a.getHeading());
        assertEquals(Direction.E, b.getHeading());
    }

    @Test
    public void testGetNbMan() {
        assertEquals(12, a.getNbMan());
        assertEquals(7, b.getNbMan());
    }
}
