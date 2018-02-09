package fr.unice.polytech.qgl.qda.actions.aerial;

import fr.unice.polytech.qgl.qda.Game.AvailableActions;
import fr.unice.polytech.qgl.qda.Island.IslandMap;
import fr.unice.polytech.qgl.qda.actions.Action;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.SpecificPattern.EdgeStrategy;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.SpecificPattern.SnakeStrategy;
import org.json.JSONObject;

/**
 * IslandProject created on 17/12/2015 by Keynes Timothy - Aloysius_tim
 */
public class Fly extends Action implements DecisionOnAerialResultInterpretation {
    public Fly(IslandMap islandMap, JSONObject action, JSONObject acknowledgeResult) {
        super(islandMap, action, acknowledgeResult);
        this.islandMap.fly();
        this.actionType= AvailableActions.FLY;
    }

    public static JSONObject buildAction() {
        JSONObject cmd = new JSONObject();
        cmd.put("action", "fly");
        return cmd;
    }

    @Override
    public void takeDecisionOnEdgeStrat(EdgeStrategy strategy) {

    }

    @Override
    public void takeDecisionOnSnakeStrat(SnakeStrategy snakeStrategy) {

    }
}
