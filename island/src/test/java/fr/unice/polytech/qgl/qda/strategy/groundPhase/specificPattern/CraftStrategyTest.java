package fr.unice.polytech.qgl.qda.strategy.groundPhase.specificPattern;

import fr.unice.polytech.qgl.qda.Game.Assignment;
import fr.unice.polytech.qgl.qda.Island.*;
import fr.unice.polytech.qgl.qda.actions.Action;
import fr.unice.polytech.qgl.qda.strategy.GroundPhase.SpecificPattern.CraftStrategy;
import fr.unice.polytech.qgl.qda.strategy.GroundPhase.SpecificPattern.FindStrategy;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Created by spong on 2016/3/24.
 */
public class CraftStrategyTest {
    IslandMap islandMap;
    Assignment assignment;
    HashMap<Ressource, ArrayList<Tile>> tileList;

    @Before
    public void before() throws Exception {
        islandMap=new IslandMap(Direction.E);

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

        islandMap.actualizePosition(0,0);

        islandMap.getCollectedRessources().put(Ressource.QUARTZ, 2500);
        islandMap.getCollectedRessources().put(Ressource.WOOD,1500);
    }


    @Test
    public void ConstructorTest() {
        CraftStrategy strategy1 = new CraftStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), tileList);
    }

    @Test
    public void IsavaibleTest(){

        CraftStrategy strategy1 = new CraftStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), tileList);
        HashMap<Ressource,Integer> recipe=new HashMap<>();
        recipe.put(Ressource.WOOD,1000);
        recipe.put(Ressource.QUARTZ,2000);

        boolean result=strategy1.isAvailable(recipe);
        assertEquals(true,result);
    }

    @Test
    public void GetNextMoveTest(){

        CraftStrategy strategy1 = new CraftStrategy(islandMap, assignment, new LinkedList<JSONObject>(), new LinkedList<Action>(), assignment.getBudget(), tileList);
        JSONObject action1=strategy1.getNextMove();
        assertEquals("transform", action1.get("action"));

    }

}
