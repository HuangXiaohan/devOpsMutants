package fr.unice.polytech.qgl.qda.actions.ground;

import fr.unice.polytech.qgl.qda.Island.Biome;
import fr.unice.polytech.qgl.qda.Island.Direction;
import fr.unice.polytech.qgl.qda.Island.IslandMap;
import fr.unice.polytech.qgl.qda.actions.Action;
import fr.unice.polytech.qgl.qda.strategy.GroundPhase.SpecificPattern.FindStrategy;
import fr.unice.polytech.qgl.qda.strategy.GroundPhase.SpecificPattern.HuntedBiome;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * IslandProject created on 17/12/2015 by Keynes Timothy - Aloysius_tim
 */
public class Glimpse extends Action implements IGroundStrategy{
    private final int range;
    private final JSONObject extras;
    private final JSONArray report;

    public Glimpse(IslandMap islandMap, JSONObject action, JSONObject acknowledgeResult) {
        super(islandMap, action, acknowledgeResult);
        extras = acknowledgeResult.getJSONObject("extras");
        report = extras.getJSONArray("report");
        range = extras.getInt("asked_range");
    }

    @Override
    public void findStrategy(FindStrategy strategy) {
        return;
    }

    public static JSONObject buildAction(Direction currentHeading, int range) {
        JSONObject cmd = new JSONObject();
        cmd.put("action", "glimpse");

        JSONObject parameters = new JSONObject();

        parameters.put("direction", currentHeading.getDescription());
        parameters.put("range", range);
        cmd.put("parameters", parameters);
        return cmd;
    }
}