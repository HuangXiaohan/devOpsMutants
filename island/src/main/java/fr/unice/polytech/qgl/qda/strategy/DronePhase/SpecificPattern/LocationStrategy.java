package fr.unice.polytech.qgl.qda.strategy.DronePhase.SpecificPattern;

import fr.unice.polytech.qgl.qda.Game.Assignment;
import fr.unice.polytech.qgl.qda.Island.Direction;
import fr.unice.polytech.qgl.qda.Island.IslandMap;
import fr.unice.polytech.qgl.qda.Island.Ressource;
import fr.unice.polytech.qgl.qda.actions.aerial.Echo;
import fr.unice.polytech.qgl.qda.actions.aerial.Fly;
import fr.unice.polytech.qgl.qda.actions.aerial.Heading;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.DroneStrategy;
import fr.unice.polytech.qgl.qda.strategy.Strategy;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by justin on 08/02/16.
 */
public class LocationStrategy extends DroneStrategy {
    private Integer top;
    private int rangeToCorner;
    private boolean gotCloserEdgeStep1 = false;
    private boolean gotCloserEdgeStep2 = false;
    private boolean isInit = false;
    private boolean left = false;


    public LocationStrategy(JSONObject assignment) {
        super(new IslandMap(Direction.getHeading(assignment.getString("heading"))), new Assignment(assignment), new LinkedList<>(), new LinkedList<>(), assignment.getInt("budget"), new HashMap<>(), null, Direction.getHeading(assignment.getString("heading")));

        for(Ressource res : this.assignment.keySet()){
            this.tileList.put(res, new ArrayList<>());
        }
    }

    public Position findPosition(){
        switch(assignment.getHeading()){
            case N: return (left? Position.BOTTOM_LEFT:Position.BOTTOM_RIGHT);
            case E: return (left? Position.UPPER_LEFT:Position.BOTTOM_LEFT);
            case S: return (left? Position.UPPER_RIGHT:Position.UPPER_LEFT);
            case W: return (left? Position.BOTTOM_RIGHT:Position.UPPER_RIGHT);
            default:  return null;
        }
    }

    public void init(){
        this.bufferActions.add(Echo.buildAction(this.islandMap.getDirectionActuelle().getLeftDirection()));
        this.bufferActions.add(Echo.buildAction((this.islandMap.getDirectionActuelle().getRightDirection())));
        isInit = true;
    }

    public void lastStep(){

        int BORDERCONTROL = 3;
        if(rangeToCorner>=BORDERCONTROL){
            this.bufferActions.add(Heading.buildAction(this.assignment.getHeading()));
        }
        this.bufferActions.add(Echo.buildAction(this.assignment.getHeading()));
    }

    public void goToCloserEdge(){
        int BORDERCONTROL = 3;
        if(rangeToCorner<BORDERCONTROL)return;

        if(rangeToCorner == top){
            this.bufferActions.add(Heading.buildAction(this.islandMap.getDirectionActuelle().getLeftDirection()));
        }
        else {
            this.bufferActions.add(Heading.buildAction(this.islandMap.getDirectionActuelle().getRightDirection()));
        }

        for(int i = 0;i<rangeToCorner-(BORDERCONTROL-1);i++){
            this.bufferActions.add(Fly.buildAction());
        }
    }

    public int getCloserEdge(int range){
        int BORDERCONTROL = 3;
        if(top==null){
            gotCloserEdgeStep1 = true;
            top = range;
            left = true;
            return top;
        }
        else{

            gotCloserEdgeStep2 = true;

            if(this.assignment.getHeading() == Direction.E || this.assignment.getHeading() == Direction.W){
                this.islandMap.setHeight((top+range)*3);
            }
            else{
                this.islandMap.setWidth((top+range)*3);
            }

            if(top<=range){return top;}
            else{
                left = false;
                return range;
            }
        }
    }

    public void completeMapSize(int range){
        int BORDERCONTROL = 3;
        if(rangeToCorner>=BORDERCONTROL)range += BORDERCONTROL-1;
        if(this.assignment.getHeading() == Direction.E || this.assignment.getHeading() == Direction.W){
            this.islandMap.setWidth(range*3);
        }
        else{
            this.islandMap.setHeight(range*3);
        }
    }

    private void actualizePosition(){
        switch(position){
            case UPPER_LEFT:
                this.islandMap.actualizePosition(-this.islandMap.getWidth()/2,this.islandMap.getHeight()/2);
                break;
            case UPPER_RIGHT:
                this.islandMap.actualizePosition(this.islandMap.getWidth() / 2,this.islandMap.getHeight() / 2);
                break;
            case BOTTOM_LEFT:
                this.islandMap.actualizePosition(-this.islandMap.getWidth() / 2, -this.islandMap.getHeight() / 2);
                break;
            case BOTTOM_RIGHT:
                this.islandMap.actualizePosition(this.islandMap.getWidth() / 2, -this.islandMap.getHeight() / 2);
                break;
        }
    }

    @Override
    public JSONObject getNextMove() {
        if(!isInit){
            init();
        }
        else if(!gotCloserEdgeStep1) {
            rangeToCorner = getCloserEdge(((Echo) this.actionsHistory.getLast()).getRange());
        }
        else if(!gotCloserEdgeStep2){
            rangeToCorner = getCloserEdge(((Echo) this.actionsHistory.getLast()).getRange());
            position = findPosition();
            goToCloserEdge();
            lastStep();
        }

        else if(this.bufferActions.size()==0){
            completeMapSize(((Echo) this.actionsHistory.getLast()).getRange());
            actualizePosition();
            this.bufferActions.add(Echo.buildAction(islandMap.getDirectionActuelle().getLeftDirection()));
            this.endOfStrat = true;

        }

        return super.getNextMove();
    }

    @Override
    public Strategy getNextStrategy() {
        return new FindIslandStrategy(islandMap, assignment, bufferActions, actionsHistory,remainingBudget,tileList, position, getCurrentHeading());
    }
}