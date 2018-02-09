package fr.unice.polytech.qgl.qda.strategy.DronePhase.SpecificPattern;

import fr.unice.polytech.qgl.qda.Game.Assignment;
import fr.unice.polytech.qgl.qda.Island.Direction;
import fr.unice.polytech.qgl.qda.Island.IslandMap;
import fr.unice.polytech.qgl.qda.Island.Ressource;
import fr.unice.polytech.qgl.qda.Island.Tile;
import fr.unice.polytech.qgl.qda.actions.Action;
import fr.unice.polytech.qgl.qda.actions.aerial.Echo;
import fr.unice.polytech.qgl.qda.actions.aerial.Fly;
import fr.unice.polytech.qgl.qda.actions.aerial.Heading;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.DroneStrategy;
import fr.unice.polytech.qgl.qda.strategy.Strategy;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by justin on 07/03/16.
 */
public class FindIslandStrategy extends DroneStrategy {
    private boolean earth = false;
    private boolean left = false;
    private boolean isInit = false;
    private boolean start = false;

    public FindIslandStrategy(IslandMap islandMap, Assignment assignment, LinkedList<JSONObject> bufferActions, LinkedList<Action> actionsHistory, int remainingBudget,HashMap<Ressource,ArrayList<Tile>> tileList, Position position, Direction currentHeading) {
        super(islandMap, assignment, bufferActions, actionsHistory, remainingBudget, tileList, position, currentHeading);
    }

    public void init(){
        isInit = true;
        this.bufferActions.add(Echo.buildAction(islandMap.getDirectionActuelle().getLeftDirection()));
    }

    public void startStair(int range){
        start = true;
        if(range>5){
            this.bufferActions.add(Heading.buildAction(islandMap.getDirectionActuelle().getLeftDirection()));
            this.bufferActions.add(Echo.buildAction(islandMap.getDirectionActuelle().getLeftDirection()));
            left = true;
        }
        else{
            this.bufferActions.add(Heading.buildAction(islandMap.getDirectionActuelle().getRightDirection()));
            this.bufferActions.add(Echo.buildAction(islandMap.getDirectionActuelle().getRightDirection()));
            left = false;
        }
    }

    public void lastStep(int range){
        range--;
        if(!left){
            this.bufferActions.add(Heading.buildAction(islandMap.getDirectionActuelle().getLeftDirection()));
            range--;
        }
        for(int i = 0;i<range;i++){
            this.bufferActions.add(Fly.buildAction());
        }
    }

    public void findEarth(boolean isEarth,int range){
        if(isEarth){
            earth = true;
            lastStep(range);
        }
        else{
            if(left){
                this.bufferActions.add(Heading.buildAction(islandMap.getDirectionActuelle().getRightDirection()));
                this.bufferActions.add(Echo.buildAction(islandMap.getDirectionActuelle()));
                left = false;
            }
            else{
                this.bufferActions.add(Heading.buildAction(islandMap.getDirectionActuelle().getLeftDirection()));
                this.bufferActions.add(Echo.buildAction(islandMap.getDirectionActuelle().getLeftDirection()));
                left = true;
            }
        }

    }

    @Override
    public JSONObject getNextMove() {

        if(!start){
            startStair(((Echo) this.actionsHistory.getLast()).getRange());
        }
        else if(!earth){
            if(this.bufferActions.size()==0 ){
                boolean findEarth = ((Echo)this.actionsHistory.getLast()).hasGround();
                findEarth(findEarth,((Echo) this.actionsHistory.getLast()).getRange());
            }
        }

        else if(bufferActions.size() == 1){
            this.endOfStrat = true;
        }


        return super.getNextMove();
    }

    @Override
    public Strategy getNextStrategy() {
        return new SnakeStrategy(islandMap, assignment, bufferActions, actionsHistory, remainingBudget,tileList, position, getCurrentHeading());
    }
}
