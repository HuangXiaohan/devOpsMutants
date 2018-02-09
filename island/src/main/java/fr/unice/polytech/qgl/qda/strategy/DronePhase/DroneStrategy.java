package fr.unice.polytech.qgl.qda.strategy.DronePhase;

import fr.unice.polytech.qgl.qda.Game.Assignment;
import fr.unice.polytech.qgl.qda.Island.Direction;
import fr.unice.polytech.qgl.qda.Island.IslandMap;
import fr.unice.polytech.qgl.qda.Island.Ressource;
import fr.unice.polytech.qgl.qda.Island.Tile;
import fr.unice.polytech.qgl.qda.actions.Action;
import fr.unice.polytech.qgl.qda.actions.aerial.Echo;
import fr.unice.polytech.qgl.qda.actions.aerial.Heading;
import fr.unice.polytech.qgl.qda.strategy.Strategy;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * IslandProject created on 23/11/2015 by Keynes Timothy - Aloysius_tim
 */
public abstract class DroneStrategy extends Strategy {
    protected Position position;
    private Direction echoDir;
    private Direction currentHeading;

    public enum Position{
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        UPPER_LEFT,
        UPPER_RIGHT
    }

    public DroneStrategy(IslandMap islandMap, Assignment assignment, LinkedList<JSONObject> bufferActions, LinkedList<Action> actionsHistory, int remainingBudget, HashMap<Ressource,ArrayList<Tile>> tileList, Position position, Direction currentHeading) {
        super(islandMap, assignment, bufferActions, actionsHistory, remainingBudget, tileList);
        this.position=position;
        this.currentHeading=currentHeading;
    }

    @Override
    public abstract Strategy getNextStrategy();

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    protected void echo(Direction dir) {
        bufferActions.add(Echo.buildAction(dir));
        echoDir = dir;
    }

    protected void turnLeft() {
        currentHeading = currentHeading.getLeftDirection();
        bufferActions.add(Heading.buildAction(currentHeading));
    }

    protected void turnRight() {
        currentHeading = currentHeading.getRightDirection();
        bufferActions.add(Heading.buildAction(currentHeading));
    }

    protected void setHeading(Direction dir) {
        currentHeading = dir;
        bufferActions.add(Heading.buildAction(dir));
    }

    public Direction getCurrentHeading() {
        return currentHeading;
    }

    public void setCurrentHeading(Direction dir){
        this.currentHeading = dir;
    }
}
