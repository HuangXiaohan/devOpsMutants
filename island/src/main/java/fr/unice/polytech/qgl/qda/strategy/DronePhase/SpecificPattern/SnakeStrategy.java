package fr.unice.polytech.qgl.qda.strategy.DronePhase.SpecificPattern;


import fr.unice.polytech.qgl.qda.Game.Assignment;
import fr.unice.polytech.qgl.qda.Island.*;
import fr.unice.polytech.qgl.qda.actions.Action;
import fr.unice.polytech.qgl.qda.actions.aerial.*;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.DroneStrategy;
import fr.unice.polytech.qgl.qda.strategy.GroundPhase.SpecificPattern.FindStrategy;
import fr.unice.polytech.qgl.qda.strategy.Strategy;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Stratégie qui parcours l'île en faisant un quadrillage de scan, le nombre d'occurence de scan par ligne est déterminé
 * par la variable FORWARD, le nombre de tile qu'on saute entre 2 lignes est déterminé par la variable STEP
 *
 * si un scan nous donne un biome en relation avec un de nos contrats, on l'enregistre dans une liste de tile pour la phase terrestre
 */
public class SnakeStrategy extends DroneStrategy {
    public final static int STEP = 2;
    public final static int FORWARD = 3;
    public final static int TILES_BY_FLY = 3;
    public final static int TILES_BY_RESOURCE = 3;
    public final static int TILE_LIMIT = 14;
    public final static int TILE_LIMIT_ON_SIDE = 9;

    public final int LIMIT_E;
    public final int LIMIT_W;
    public final int LIMIT_N;
    public final int LIMIT_S;

    private boolean lastStep = false;
    private boolean fasterEnd = false;
    private boolean bottom;
    private boolean leftToRight;

    public final int BUDGET_LIMIT;
    private int noGround = 0;

    public SnakeStrategy(IslandMap islandMap, Assignment assignment, LinkedList<JSONObject> bufferActions, LinkedList<Action> actionsHistory, int remainingBudget, HashMap<Ressource,ArrayList<Tile>> tileList, Position position, Direction currentHeading) {
        super(islandMap, assignment, bufferActions, actionsHistory, remainingBudget, tileList, position, currentHeading);
        bufferActions.clear();

        if(this.assignment.getBudget()>9000 && this.assignment.getBudget()<=10000){
            BUDGET_LIMIT = 8500;
        }
        else if(this.assignment.getBudget()>6000 && this.assignment.getBudget()<=7000){
            BUDGET_LIMIT = 5500;
        }
        else{
            BUDGET_LIMIT = 5000;
        }

        setCurrentHeading(this.islandMap.getDirectionActuelle());
        init();
        this.bufferActions.add(Scan.buildAction());

        LIMIT_E = (islandMap.getWidth()/2)- TILE_LIMIT_ON_SIDE;
        LIMIT_W = -(islandMap.getWidth()/2)+ TILE_LIMIT_ON_SIDE;
        LIMIT_N = (islandMap.getHeight()/2)- TILE_LIMIT;
        LIMIT_S = -(islandMap.getHeight()/2)+ TILE_LIMIT;
    }

    /**
     * on initialise le sens dans lequelle nous allong quadriller la carte
     **/
    public void init(){
        int x = islandMap.getPositionActuelle().getX();
        int y = islandMap.getPositionActuelle().getY();

        if(x>0){
            if((this.islandMap.getDirectionActuelle()) == Direction.W){
                leftToRight = false;
            } else if(getCurrentHeading() == Direction.E){
                leftToRight = true;
            } else if(getCurrentHeading() == Direction.N || this.islandMap.getDirectionActuelle() == Direction.S) {
                setHeading(Direction.W);
                leftToRight = false;
            }
        }
        else{
            if(this.islandMap.getDirectionActuelle() == Direction.W){
                leftToRight = false;
            }
            else  if(this.islandMap.getDirectionActuelle() == Direction.E){
                leftToRight = true;
            }
            else  if(this.islandMap.getDirectionActuelle() == Direction.N || this.islandMap.getDirectionActuelle() == Direction.S){
                setHeading(Direction.E);
                leftToRight = true;
            }
        }
        if(y>0)
            bottom = false;
        else
            bottom = true;



    }
    public void stepByRight(){
        turnRight();
        upAndDownSecurity();
        turnRight();
    }

    public void stepByLeft(){
        turnLeft();
        upAndDownSecurity();
        turnLeft();
    }

    public void nextFloor(){
        if((bottom && !leftToRight) || (!bottom && leftToRight))
            stepByRight();
        else
            stepByLeft();
        leftToRight = !leftToRight;
    }

    /*
    permet d'éviter de sortir de la carte en terme de nombre de fly maximum possible entre 2 scan
     */
    public void forwardSecurity(){
        int currentX = islandMap.getPositionActuelle().getX();
        int diff;
        if(this.getCurrentHeading() == Direction.E) {
            diff = LIMIT_E - currentX;
        }
        else{
            diff = Math.abs(LIMIT_W - currentX);
        }
        int i = FORWARD;
        if(diff<TILES_BY_FLY){
            nextFloor();
            return;
        }
        while(i>0 && (diff>0)){
            this.bufferActions.add(Fly.buildAction());
            diff = diff - TILES_BY_FLY;
            i--;
        }

    }

    /*
    permet d'éviter de sortir de la carte sur l'axe des Y
     */
    private void upAndDownSecurity() {
        int currentY = islandMap.getPositionActuelle().getY();
        int diff;
        if(this.getCurrentHeading() == Direction.N){
            diff =  LIMIT_N-currentY;
        }
        else{
            diff = Math.abs(LIMIT_S-currentY);
        }

        int i = STEP;
        while(i>0 && (diff>0)){
            this.bufferActions.add(Fly.buildAction());
            diff = diff-TILES_BY_FLY;
            i--;
        }
    }

    public void goToEnd(int x){
        bufferActions.clear();
        int tmp_x = x;
        bottom = !bottom;
        if(this.getCurrentHeading() == Direction.E){
            while(tmp_x<LIMIT_E){
                this.bufferActions.add(Fly.buildAction());
                tmp_x+=TILES_BY_FLY;
            }
        }
        else{
            while(tmp_x>LIMIT_W){
                this.bufferActions.add(Fly.buildAction());
                tmp_x-=TILES_BY_FLY;
            }

        }
        nextFloor();
        this.bufferActions.add(Echo.buildAction(getCurrentHeading()));
        lastStep = true;
    }

    /*
    méthode qui effectue la position du drone tout au long de la stratégie
     */
    public void snakeStrat(boolean ocean, boolean other, boolean lake){
        int x = islandMap.getPositionActuelle().getX();
        if (ocean){
            if(fasterEnd){
                goToEnd(x);
            } else {
                echo(this.getCurrentHeading());
            }
        }

        else {

            forwardSecurity();
            this.bufferActions.add(Scan.buildAction());
        }

    }



    @Override
    public void interpretAcknowledgeResult(JSONObject acknowledgeResult) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        super.interpretAcknowledgeResult(acknowledgeResult);

        ((DecisionOnAerialResultInterpretation)this.actionsHistory.getLast()).takeDecisionOnSnakeStrat(this);
    }

    @Override
    public Strategy getNextStrategy() {
        if (this.islandMap.getCreeks().size()>0){
            this.islandMap.clear();
            this.bufferActions.clear();
            return new FindStrategy(this.islandMap, this.assignment, this.bufferActions, this.actionsHistory, this.remainingBudget, this.tileList);
        }else{
            return new EdgeStrategy(this.islandMap, this.assignment, this.bufferActions, this.actionsHistory, this.remainingBudget, this.tileList, this.position, this.getCurrentHeading());
        }
    }

    public boolean isLastStep() {
        return lastStep;
    }
    public void setLastStep(boolean lastStep) {
        this.lastStep = lastStep;
    }
    public boolean isFasterEnd() {
        return fasterEnd;
    }
    public void setFasterEnd(boolean fasterEnd) {
        this.fasterEnd = fasterEnd;
    }
    public boolean isBottom() {
        return bottom;
    }
    public void setBottom(boolean bottom) {
        this.bottom = bottom;
    }
    public int getNoGround() {
        return noGround;
    }
    public void setNoGround(int noGround) {
        this.noGround = noGround;
    }
}