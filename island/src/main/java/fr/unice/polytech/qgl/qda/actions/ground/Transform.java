package fr.unice.polytech.qgl.qda.actions.ground;

import fr.unice.polytech.qgl.qda.Game.AvailableActions;
import fr.unice.polytech.qgl.qda.Island.IslandMap;
import fr.unice.polytech.qgl.qda.Island.Ressource;
import fr.unice.polytech.qgl.qda.actions.Action;
import fr.unice.polytech.qgl.qda.strategy.GroundPhase.SpecificPattern.FindStrategy;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * IslandProject created on 17/12/2015 by Keynes Timothy - Aloysius_tim
 */
public class Transform extends Action implements IGroundStrategy{
    public Transform(IslandMap islandMap, JSONObject action, JSONObject acknowledgeResult) {
        super(islandMap, action, acknowledgeResult);

        this.actionType= AvailableActions.TRANSFORM;
    }

    @Override
    public void findStrategy(FindStrategy strategy) {
    }

    public static JSONObject buildAction(HashMap<Ressource, Integer> ressourceToCraft) {
        JSONObject cmd = new JSONObject();
        cmd.put("action", "transform");
        JSONObject parameters = new JSONObject();

        Iterator it = ressourceToCraft.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Ressource, Integer> pair = (Map.Entry)it.next();
            parameters.put(pair.getKey().name(), pair.getValue());
        }

        cmd.put("parameters", parameters);

        return cmd;
    }
}
