package fr.unice.polytech.qgl.qda.strategy;

import fr.unice.polytech.qgl.qda.Game.Assignment;
import fr.unice.polytech.qgl.qda.Island.IslandMap;
import fr.unice.polytech.qgl.qda.Island.Ressource;
import fr.unice.polytech.qgl.qda.Island.Tile;
import fr.unice.polytech.qgl.qda.actions.Action;
import fr.unice.polytech.qgl.qda.actions.ground.Stop;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * IslandProject created on 23/11/2015 by Keynes Timothy - Aloysius_tim
 */
public abstract class Strategy {
    protected IslandMap islandMap;
    protected Assignment assignment;
    protected LinkedList<JSONObject> bufferActions;
    protected LinkedList<Action> actionsHistory;
    protected final HashMap<Ressource,ArrayList<Tile>> tileList;
    protected final int BUDGET_LIMIT = 200;

    protected int remainingBudget;

    protected boolean endOfStrat;

    public Strategy(IslandMap islandMap, Assignment assignment, LinkedList<JSONObject> bufferActions, LinkedList<Action> actionsHistory, int remainingBudget, HashMap<Ressource,ArrayList<Tile>> tileList) {
        this.islandMap = islandMap;
        this.assignment = assignment;
        this.bufferActions = bufferActions;
        this.actionsHistory = actionsHistory;
        this.remainingBudget = remainingBudget;
        this.tileList = tileList;

        this.endOfStrat = false;
    }

    public void interpretAcknowledgeResult(JSONObject acknowledgeResult) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        if (!this.bufferActions.isEmpty()){
            this.actionsHistory.addLast(Action.createAction(bufferActions.removeFirst(), acknowledgeResult, islandMap));
            this.remainingBudget-=this.actionsHistory.getLast().getCost();
        }
    }

    public JSONObject getNextMove() {
        if(this.remainingBudget < BUDGET_LIMIT || this.bufferActions.isEmpty()){
            return Stop.buildAction();
        }
        return bufferActions.getFirst();
    }

    public boolean isEndOfStrat() {
        return endOfStrat;
    }

    public abstract Strategy getNextStrategy();

    public LinkedList<JSONObject> getBufferActions() {
        return bufferActions;
    }

    public void setEndOfStrat(boolean endOfStrat) {
        this.endOfStrat = endOfStrat;
    }

    public int getRemainingBudget() {
        return remainingBudget;
    }

    public HashMap<Ressource, ArrayList<Tile>> getTileList() {
        return tileList;
    }

    public Assignment getAssignment() {
        return assignment;
    }
}