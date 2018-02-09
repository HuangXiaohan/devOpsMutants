package fr.unice.polytech.qgl.qda.actions.ground;

import fr.unice.polytech.qgl.qda.Game.AvailableActions;
import fr.unice.polytech.qgl.qda.Island.IslandMap;
import fr.unice.polytech.qgl.qda.actions.Action;
import fr.unice.polytech.qgl.qda.strategy.GroundPhase.SpecificPattern.FindStrategy;
import org.json.JSONObject;

/**
 * IslandProject created on 17/12/2015 by Keynes Timothy - Aloysius_tim
 */
public class Land extends Action implements IGroundStrategy{
    private String creek;
    private int peoples;

    public Land(IslandMap islandMap, JSONObject action, JSONObject acknowledgeResult) {
        super(islandMap, action, acknowledgeResult);

        this.creek = action.getJSONObject("parameters").getString("creek");
        this.peoples = action.getJSONObject("parameters").getInt("people");

        this.islandMap.land(creek, peoples);
        this.actionType= AvailableActions.LAND;
    }

    public JSONObject strategy() {
        return null;
    }

    public static JSONObject buildAction(String creekID, int people) {
        JSONObject cmd = new JSONObject();
        cmd.put("action", "land");

        JSONObject parameters = new JSONObject();
        parameters.put("creek", creekID);
        parameters.put("people", people);
        cmd.put("parameters", parameters);
        return cmd;
    }

    public String getCreek() {
        return creek;
    }

    public int getPeoples() {
        return peoples;
    }

    @Override
    public void findStrategy(FindStrategy strategy) {
        strategy.setCreek(islandMap.getPositionActuelle());
        strategy.startMove();
    }
}
