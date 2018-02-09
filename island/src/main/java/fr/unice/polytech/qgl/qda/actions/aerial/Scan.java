package fr.unice.polytech.qgl.qda.actions.aerial;

import fr.unice.polytech.qgl.qda.Game.AvailableActions;
import fr.unice.polytech.qgl.qda.Island.Biome;
import fr.unice.polytech.qgl.qda.Island.IslandMap;
import fr.unice.polytech.qgl.qda.Island.Ressource;
import fr.unice.polytech.qgl.qda.actions.Action;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.SpecificPattern.EdgeStrategy;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.SpecificPattern.SnakeStrategy;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * IslandProject created on 17/12/2015 by Keynes Timothy - Aloysius_tim
 */
public class Scan extends Action implements DecisionOnAerialResultInterpretation {
    private final JSONObject acknowledgeResult;
    private ArrayList<String> creeks = new ArrayList<>();
    private ArrayList<Biome> biomes = new ArrayList<>();

    public Scan(IslandMap islandMap, JSONObject action, JSONObject acknowledgeResult) {
        super(islandMap, action, acknowledgeResult);
        this.acknowledgeResult=acknowledgeResult;

        for (Object biome : acknowledgeResult.getJSONObject("extras").getJSONArray("biomes")) {
            biomes.add(Biome.valueOf((String) biome));
        }


        for (Object creek : acknowledgeResult.getJSONObject("extras").getJSONArray("creeks")) {
            creeks.add((String) creek);
        }


        this.islandMap.scan(biomes, creeks);
        this.actionType= AvailableActions.SCAN;
    }

    public static JSONObject buildAction() {
        JSONObject cmd = new JSONObject();
        cmd.put("action", "scan");
        return cmd;
    }

    public ArrayList<String> getCreeks() {
        return creeks;
    }

    public ArrayList<Biome> getBiomes() {
        return biomes;
    }

    /**
     * méthode la plus importante de cette classe, elle contient l'algorythme pour faire le contours de l'île
     * les variable up/down : si le drone monte la variable up passe à true et si il descend down passe true
     * permet d'éviter les boucles
     * @param strategy
     */
    @Override
    public void takeDecisionOnEdgeStrat(EdgeStrategy strategy) {
        boolean ocean = false;
        boolean ground = false;
        for( Biome biome : this.getBiomes()){
            if(biome == Biome.OCEAN){
                ocean = true;
            }
            else{
                ground = true;
            }
        }


        if (ocean && !ground){ // cas où le scan ne trouve que de l'ocean

            if(strategy.getUp()){
                strategy.getBufferActions().add(Fly.buildAction());
                strategy.setUp(false);
                strategy.setDown(false);
            }
            else {
                if(strategy.isDown()){
                    strategy.headingNext();
                    strategy.getBufferActions().add(Scan.buildAction());
                    strategy.setUp(false);
                    strategy.setDown(false);
                    return;
                }
                strategy.down();
                strategy.setDown(true);
            }

        }
        else if (!ocean && ground) { // cas où le scan ne trouve que de la terre

            if(strategy.isDown()){
                strategy.getBufferActions().add(Fly.buildAction());
                strategy.setUp(false);
                strategy.setDown(false);
            }
            else{
                if(strategy.isUp()){
                    strategy.headingPrevious();
                    strategy.getBufferActions().add(Scan.buildAction());
                    strategy.setUp(false);
                    strategy.setDown(false);
                    return;
                }
                strategy.up();
                strategy.setUp(true);
            }
        }
        else{ // cas où le scan trouve de la terre et de l'ocean

            if(strategy.isClock())
                strategy.getBufferActions().add(Echo.buildAction(strategy.getCurrentHeading().getRightDirection()));
            else
                strategy.getBufferActions().add(Echo.buildAction(strategy.getCurrentHeading().getLeftDirection()));
            strategy.setUp(false);
            strategy.setDown(false);
            return;
        }
        strategy.getBufferActions().add(Scan.buildAction());
    }

    /**méthode qui enregistre les tiles intéressant dans une liste
     * si chaque contrat possède au moins 3 tiles lié à lui, on arrête la stratégie
     * on ne récupère que les tiles qui n'ont qu'un seul biome lié à eux
     **/
    @Override
    public void takeDecisionOnSnakeStrat(SnakeStrategy snakeStrategy) {

        JSONObject extras = acknowledgeResult.getJSONObject("extras");
        JSONArray creeks = extras.getJSONArray("creeks");
        JSONArray biome = extras.getJSONArray("biomes");

        boolean multipleBiome = (biome.length()>1?true:false);
        boolean ocean = false;
        boolean ground = false;
        boolean lake = false;
        boolean enough = true;

        for (Object x : biome) {

            if (Biome.valueOf(((String) x)) == Biome.OCEAN)
                ocean = true;
            else if (Biome.valueOf(((String) x)) == Biome.LAKE)
                lake = true;
            else{
                ground = true;

                for (Ressource res : snakeStrategy.getTileList().keySet()) {
                    if (res.getTypeRessource() != Ressource.TypeRessource.MANUFACTURED) {

                        for (Biome bio : res.getAssociatedBiomes()) {
                            if (!multipleBiome && bio == Biome.valueOf((String) x)) {
                                snakeStrategy.getTileList().get(res).add(getIslandMap().getPositionActuelle());
                            }
                        }
                    }
                }

            }

        }
        for(Ressource res: snakeStrategy.getTileList().keySet()){
            if (res.getTypeRessource() != Ressource.TypeRessource.MANUFACTURED) {
                if (snakeStrategy.getTileList().get(res).size() < snakeStrategy.TILES_BY_RESOURCE) {
                    enough = false;
                }
            }
        }
        if(enough){
            snakeStrategy.setFasterEnd(true);
        }
        snakeStrategy.snakeStrat(ocean,ground,lake);
    }
}
