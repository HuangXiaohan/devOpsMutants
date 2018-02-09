package fr.unice.polytech.qgl.qda.strategy.DronePhase.SpecificPattern;

import fr.unice.polytech.qgl.qda.Game.Assignment;
import fr.unice.polytech.qgl.qda.Island.Direction;
import fr.unice.polytech.qgl.qda.Island.IslandMap;
import fr.unice.polytech.qgl.qda.Island.Ressource;
import fr.unice.polytech.qgl.qda.Island.Tile;
import fr.unice.polytech.qgl.qda.actions.Action;
import fr.unice.polytech.qgl.qda.actions.aerial.DecisionOnAerialResultInterpretation;
import fr.unice.polytech.qgl.qda.actions.aerial.Echo;
import fr.unice.polytech.qgl.qda.actions.aerial.Fly;
import fr.unice.polytech.qgl.qda.actions.aerial.Scan;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.DroneStrategy;
import fr.unice.polytech.qgl.qda.strategy.GroundPhase.SpecificPattern.FindStrategy;
import fr.unice.polytech.qgl.qda.strategy.Strategy;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * IslandProject created on 23/11/2015 by Keynes Timothy - Aloysius_tim
 */
public class EdgeStrategy extends DroneStrategy {
    private boolean up = false;
    private boolean down = false;
    private boolean init = true;
    private boolean onLeft;
    private boolean clock;

    public EdgeStrategy(IslandMap islandMap, Assignment assignment, LinkedList<JSONObject> bufferActions, LinkedList<Action> actionsHistory, int remainingBudget, HashMap<Ressource,ArrayList<Tile>> tileList, Position position, Direction currentHeading) {
        super(islandMap, assignment, bufferActions, actionsHistory, remainingBudget, tileList, position, currentHeading);

        setCurrentHeading(currentHeading);
        if(this.getCurrentHeading() == Direction.N || this.getCurrentHeading() == Direction.S)
            this.bufferActions.add(Echo.buildAction(Direction.E));
        else
            this.bufferActions.add(Echo.buildAction(this.getCurrentHeading()));

        this.bufferActions.add(Scan.buildAction());
    }

    public void init(){

        switch(position){
            case BOTTOM_LEFT:
                if(onLeft){
                    clock = false;
                }
                else{
                    clock = true;
                }
                break;
            case BOTTOM_RIGHT:
                if(onLeft){
                    clock = false;
                }
                else{
                    clock = true;
                }
                break;
            case UPPER_LEFT:
                if(onLeft){
                    clock = true;
                }
                else{
                    clock = false;
                }
                break;
            case UPPER_RIGHT:
                if(onLeft) {
                    clock = true;
                }
                else{
                    clock = false;
                }
                break;
            default:
                clock = false;
        }
    }

    ///////////// METHODES DE PLACEMENT ////////////////

    /*
    elles permettent de se placer sur le tile supérieur ou inférieur à sa position tout en conservant le même heading
     */

    public void up(){
        turnRight();
        if (!clock)this.bufferActions.add(Fly.buildAction());
        turnRight();
        turnRight();
        if (clock)this.bufferActions.add(Fly.buildAction());
        turnRight();
    }

    public void down(){
        turnRight();
        if (clock)this.bufferActions.add(Fly.buildAction());
        turnRight();
        turnRight();
        if (!clock)this.bufferActions.add(Fly.buildAction());
        turnRight();
    }


    //////////////////////// METHODES DE CHANGEMENT DE COTE DE L'ILE////////////////////////
    /*
    Sous certaines conditions, le bot se rend compte qu'il doit avoir comme référenciel un nouveau côté  (ex: si l'île est un carré, il devra faire une rotation de 90°
    lorsque il arrivera au bout d'un côté, comme l'île n'est pas forcément carré, le headingPrevious permet de revenir au référenciel précédent
     */
    public void headingNext(){
        if(clock) {
            turnLeft();
            turnLeft();
            turnLeft();
        } else {
            turnRight();
            turnRight();
            turnRight();
        }
        this.bufferActions.add(Fly.buildAction());
    }

    public void headingPrevious(){
        if(clock) {
            turnRight();
            turnRight();
            turnRight();
        }
        else{
            turnLeft();
            turnLeft();
            turnLeft();
        }
        this.bufferActions.add(Fly.buildAction());
    }

    @Override
    public void interpretAcknowledgeResult(JSONObject acknowledgeResult) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        super.interpretAcknowledgeResult(acknowledgeResult);

        if (this.islandMap.getCreeks().size()>0) endOfStrat=true;
        else ((DecisionOnAerialResultInterpretation) this.actionsHistory.getLast()).takeDecisionOnEdgeStrat(this);
    }

    @Override
    public Strategy getNextStrategy() {
        this.bufferActions.clear();
        return new FindStrategy(this.islandMap, this.assignment, this.bufferActions, this.actionsHistory, this.remainingBudget, this.tileList);
    }

    public boolean getInit() {
        return init;
    }
    public void setInit(boolean b) {
        this.init=b;
    }

    public boolean isOnLeft() {
        return onLeft;
    }

    public void setOnLeft(boolean onLeft) {
        this.onLeft = onLeft;
    }
    public boolean getUp() {
        return up;
    }
    public void setUp(boolean up) {
        this.up = up;
    }
    public void setDown(boolean down) {
        this.down = down;
    }
    public boolean isUp() {
        return up;
    }
    public boolean isDown() {
        return down;
    }
    public boolean isClock() {
        return clock;
    }
}