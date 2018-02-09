package fr.unice.polytech.qgl.qda.actions.ground;

import fr.unice.polytech.qgl.qda.Game.AvailableActions;
import fr.unice.polytech.qgl.qda.Island.IslandMap;
import fr.unice.polytech.qgl.qda.Island.Ressource;
import fr.unice.polytech.qgl.qda.Island.Tile;
import fr.unice.polytech.qgl.qda.actions.Action;
import fr.unice.polytech.qgl.qda.strategy.GroundPhase.SpecificPattern.FindStrategy;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * IslandProject created on 17/12/2015 by Keynes Timothy - Aloysius_tim
 */
public class Explore extends Action implements IGroundStrategy{
    private final JSONArray resources;
    private ArrayList<Ressource> ressources = new ArrayList<>();

    public Explore(IslandMap islandMap, JSONObject action, JSONObject acknowledgeResult) {
        super(islandMap, action, acknowledgeResult);
        for (Object ressource : acknowledgeResult.getJSONObject("extras").getJSONArray("resources")) {
            ressources.add(Ressource.valueOf(((JSONObject) ressource).getString("resource")));
        }

        this.islandMap.explore(ressources);
        this.actionType= AvailableActions.EXPLORE;

        JSONObject extras = acknowledgeResult.getJSONObject("extras");
        resources = extras.getJSONArray("resources");
        JSONArray creeks = extras.getJSONArray("pois");
    }

    @Override
    public void findStrategy(FindStrategy strategy) {
        strategy.getBufferActions().clear();
        if(strategy.getHuntedBiome().getOnBiome() && strategy.getTileShown().contains(this.islandMap.getPositionActuelle())){
            strategy.startMove();
            return;
        }
        if(strategy.getHuntedBiome().getOnBiome()) {
            strategy.getTileShown().add(this.islandMap.getPositionActuelle());
        }

        ArrayList<Ressource> alreadyExploit;
        alreadyExploit = gotInterestingExplore(resources, strategy.getTileList());
        if(!alreadyExploit.isEmpty()){
            for(Ressource resource : alreadyExploit){
                strategy.getFindedRessource().add(resource);
                strategy.getBufferActions().add(Exploit.buildAction(resource));
            }
        }
        else {
            if(!strategy.isAvoid()) {
                strategy.setAvoid(true);
                strategy.getBufferActions().addAll(strategy.getHuntedBiome().change(strategy.getHuntedBiome().getDirection()));
            }
            else {
                strategy.getBufferActions().add(Scout.buildAction(strategy.getHuntedBiome().getDirection()));
            }
        }
    }

    public ArrayList<Ressource> gotInterestingExplore(JSONArray resources, HashMap<Ressource,ArrayList<Tile>> tileList){
        ArrayList<Ressource> interest = new ArrayList<>();
        for(Object resourceObject : resources){
            JSONObject resourceJSON = (JSONObject) resourceObject;

            Ressource resource = Ressource.valueOf(resourceJSON.getString("resource"));
            String amount = resourceJSON.getString("amount");
            String cond = resourceJSON.getString("cond");

            for (Ressource key : tileList.keySet()) {
                if (key == resource ) {
                    if((!cond.equals("HARSH"))){
                        interest.add(key);
                    }
                }
            }
        }
        return interest;
    }

    public static JSONObject buildAction() {
        JSONObject cmd = new JSONObject();
        cmd.put("action", "explore");
        return cmd;
    }

    public ArrayList<Ressource> getRessources() {
        return ressources;
    }
}
