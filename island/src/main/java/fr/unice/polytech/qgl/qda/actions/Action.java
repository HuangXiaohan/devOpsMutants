package fr.unice.polytech.qgl.qda.actions;

import fr.unice.polytech.qgl.qda.Game.AvailableActions;
import fr.unice.polytech.qgl.qda.Island.IslandMap;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;

/**
 * IslandProject created on 17/12/2015 by Keynes Timothy - Aloysius_tim
 */
public abstract class Action {
    protected int cost;
    protected boolean status;
    protected IslandMap islandMap;
    protected AvailableActions actionType;

    public Action(IslandMap islandMap, JSONObject action, JSONObject acknowledgeResult) {
        this.islandMap = islandMap;

        this.actionType = AvailableActions.valueOf(action.getString("action").toUpperCase());

        this.cost = acknowledgeResult.getInt("cost");

        this.status = acknowledgeResult.getString("status").equals("OK");
    }

    public int getCost() {
        return cost;
    }

    public IslandMap getIslandMap() {
        return islandMap;
    }

    public boolean isStatus() {
        return status;
    }

    public AvailableActions getActionType() {
        return actionType;
    }

    public static Action createAction(JSONObject action, JSONObject acknowledgeResult, IslandMap islandMap) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        return (Action) AvailableActions.valueOf(action.getString("action").toUpperCase()).getAssociatedClass().getConstructors()[0].newInstance(islandMap, action, acknowledgeResult);
    }
}
