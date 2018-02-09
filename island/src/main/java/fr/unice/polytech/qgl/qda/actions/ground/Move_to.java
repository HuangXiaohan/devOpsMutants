package fr.unice.polytech.qgl.qda.actions.ground;

import fr.unice.polytech.qgl.qda.Game.AvailableActions;
import fr.unice.polytech.qgl.qda.Island.Direction;
import fr.unice.polytech.qgl.qda.Island.IslandMap;
import fr.unice.polytech.qgl.qda.actions.Action;
import fr.unice.polytech.qgl.qda.strategy.GroundPhase.SpecificPattern.FindStrategy;
import org.json.JSONObject;

/**
 * IslandProject created on 17/12/2015 by Keynes Timothy - Aloysius_tim
 */
public class Move_to extends Action implements IGroundStrategy{
    private final Direction direction;

    public Move_to(IslandMap islandMap, JSONObject action, JSONObject acknowledgeResult) {
        super(islandMap, action, acknowledgeResult);
        this.direction = Direction.getHeading(action.getJSONObject("parameters").getString("direction"));

        this.islandMap.move_to(direction);

        this.actionType= AvailableActions.MOVE_TO;
    }

    @Override
    public void findStrategy(FindStrategy strategy) {
        if(strategy.getHuntedBiome().getOnBiome()) {
            strategy.actualiseList(this.islandMap.getPositionActuelle()); //on actualise nos coordonées sur notre carte
        }
    }

    public static JSONObject buildAction(Direction currentHeading) {
        JSONObject cmd = new JSONObject();
        cmd.put("action", "move_to");
        JSONObject parametersDirection = new JSONObject();
        parametersDirection.put("direction", currentHeading.getDescription());

        cmd.put("parameters", parametersDirection);
        return cmd;
    }

    public Direction getDirection() {
        return direction;
    }
}
