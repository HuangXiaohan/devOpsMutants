package fr.unice.polytech.qgl.qda.actions.aerial;

import fr.unice.polytech.qgl.qda.Game.AvailableActions;
import fr.unice.polytech.qgl.qda.Island.Direction;
import fr.unice.polytech.qgl.qda.Island.IslandMap;
import fr.unice.polytech.qgl.qda.Island.Ressource;
import fr.unice.polytech.qgl.qda.actions.Action;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.SpecificPattern.EdgeStrategy;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.SpecificPattern.SnakeStrategy;
import org.json.JSONObject;

/**
 * IslandProject created on 17/12/2015 by Keynes Timothy - Aloysius_tim
 */
public class Heading extends Action implements DecisionOnAerialResultInterpretation {
    private Direction direction;

    public Heading(IslandMap islandMap, JSONObject action, JSONObject acknowledgeResult) {
        super(islandMap, action, acknowledgeResult);
        this.direction = Direction.getHeading(action.getJSONObject("parameters").getString("direction"));
        this.islandMap.heading(direction);
        this.actionType= AvailableActions.HEADING;
    }

    public static JSONObject buildAction(Direction heading) {
        JSONObject firstCmdParameters = new JSONObject();
        firstCmdParameters.put("direction", heading.getDescription());
        JSONObject firstCmd = new JSONObject();
        firstCmd.put("action", "heading");
        firstCmd.put("parameters", firstCmdParameters);
        return firstCmd;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public void takeDecisionOnEdgeStrat(EdgeStrategy strategy) {

    }

    @Override
    public void takeDecisionOnSnakeStrat(SnakeStrategy snakeStrategy) {

    }

}
