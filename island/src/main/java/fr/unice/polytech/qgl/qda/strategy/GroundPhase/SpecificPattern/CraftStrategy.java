package fr.unice.polytech.qgl.qda.strategy.GroundPhase.SpecificPattern;

import fr.unice.polytech.qgl.qda.Game.Assignment;
import fr.unice.polytech.qgl.qda.Island.IslandMap;
import fr.unice.polytech.qgl.qda.Island.Ressource;
import fr.unice.polytech.qgl.qda.Island.Tile;
import fr.unice.polytech.qgl.qda.actions.Action;
import fr.unice.polytech.qgl.qda.actions.ground.Transform;
import fr.unice.polytech.qgl.qda.strategy.Strategy;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by Aloysius_tim on 20/03/2016.
 */
public class CraftStrategy extends Strategy {

    public CraftStrategy(IslandMap islandMap, Assignment assignment, LinkedList<JSONObject> bufferActions, LinkedList<Action> actionsHistory, int remainingBudget, HashMap<Ressource, ArrayList<Tile>> tileList) {
        super(islandMap, assignment, bufferActions, actionsHistory, remainingBudget, tileList);
    }

    public boolean isAvailable(HashMap<Ressource, Integer> finalRecipe) {
        Iterator it = finalRecipe.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Ressource, Integer> pair = (Map.Entry)it.next();

            if (this.islandMap.getCollectedRessources().containsKey(pair.getKey())) {
                if ((this.islandMap.getCollectedRessources().get(pair.getKey()) < pair.getValue())) {
                    return false;
                }
            }
            else return false;
        }
        return true;
    }

    @Override
    public JSONObject getNextMove() {
        this.bufferActions.clear();

        Iterator it = assignment.getInitialContract().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Ressource, Integer> pair = (Map.Entry)it.next();

            if (pair.getKey().getTypeRessource() == Ressource.TypeRessource.MANUFACTURED){
                HashMap<Ressource, Integer> tmp=pair.getKey().getFinalRecipe(pair.getValue());
                if (this.isAvailable(tmp)) {
                    it.remove();
                    this.bufferActions.addFirst(Transform.buildAction(tmp));
                }
            }
        }

        return super.getNextMove();
    }

    @Override
    public void interpretAcknowledgeResult(JSONObject acknowledgeResult) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        super.interpretAcknowledgeResult(acknowledgeResult);
    }

    public HashMap<Ressource, Integer> canCraft(){
        return null;
    }

    @Override
    public Strategy getNextStrategy() {
        return null;
    }
}
