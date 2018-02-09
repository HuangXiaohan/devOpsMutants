package fr.unice.polytech.qgl.qda;

import eu.ace_design.island.bot.IExplorerRaid;
import fr.unice.polytech.qgl.qda.actions.ground.Stop;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.SpecificPattern.LocationStrategy;
import fr.unice.polytech.qgl.qda.strategy.Strategy;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * IslandProject created on 23/11/2015 by Keynes Timothy - Aloysius_tim
 */
public class Explorer implements IExplorerRaid {
    private fr.unice.polytech.qgl.qda.strategy.Strategy strategy;
    private boolean stop;
    private Strategy previousStrategy;

    public Explorer() {
        this.stop=false;
    }

    @Override
    public void initialize(String s) {
        this.strategy = new LocationStrategy(new JSONObject(s));
    }

    @Override
    public String takeDecision() {
        //Debug.println("Remaining"+this.strategy.getRemainingBudget());
        try {
            if (this.strategy.isEndOfStrat()) {
                this.strategy = strategy.getNextStrategy();
            }
            if (this.strategy==null || this.strategy.getRemainingBudget()<=200) {
                stop=true;
                return Stop.buildAction().toString();
            }

            return strategy.getNextMove().toString();
        }catch (Exception e){
            stop=true;
            //Debug.println(Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
            return Stop.buildAction().toString();
        }
    }

    @Override
    public void acknowledgeResults(String s) {
        if (!stop)
            try {
                strategy.interpretAcknowledgeResult(new JSONObject(s));
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                //Debug.println(Arrays.toString(e.getStackTrace()));
                e.printStackTrace();
            }
    }
}