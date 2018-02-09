package fr.unice.polytech.qgl.qda.actions.ground;

import fr.unice.polytech.qgl.qda.Game.AvailableActions;
import fr.unice.polytech.qgl.qda.Island.Biome;
import fr.unice.polytech.qgl.qda.Island.Direction;
import fr.unice.polytech.qgl.qda.Island.IslandMap;
import fr.unice.polytech.qgl.qda.Island.Ressource;
import fr.unice.polytech.qgl.qda.actions.Action;
import fr.unice.polytech.qgl.qda.strategy.GroundPhase.SpecificPattern.FindStrategy;
import fr.unice.polytech.qgl.qda.strategy.GroundPhase.SpecificPattern.HuntedBiome;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * IslandProject created on 17/12/2015 by Keynes Timothy - Aloysius_tim
 */
public class Scout extends Action implements IGroundStrategy{
    private final Direction direction;
    private final ArrayList<Ressource> ressources;
    private final int altitude;

    public Scout(IslandMap islandMap, JSONObject action, JSONObject acknowledgeResult) {
        super(islandMap, action, acknowledgeResult);

        this.direction = Direction.getHeading(action.getJSONObject("parameters").getString("direction"));
        this.altitude = acknowledgeResult.getJSONObject("extras").getInt("altitude");
        this.ressources = new ArrayList<>();

        for (Object ressource : acknowledgeResult.getJSONObject("extras").getJSONArray("resources")) {
            ressources.add(Ressource.valueOf((String) ressource));
        }

        this.islandMap.scout(direction, altitude, ressources);

        this.actionType= AvailableActions.SCOUT;
    }

    @Override
    public void findStrategy(FindStrategy strategy) {

        strategy.getBufferActions().clear();
        boolean[] response = this.gotInterestingResource(strategy.getHuntedBiome());
        boolean other = response[0];
        boolean interest = response[1];


        if(interest){ // le résultat du scout nous intéresse
            /**@TODO !!! Très strange, par défaut dans l'ancienne version on assigne direct nextAction (bufferAction now) par une nouvelle arrayListe / donc on la vide
             * nextAction = huntedBiome.run(huntedBiome.getDirection());
             **/
            strategy.getBufferActions().clear();
            strategy.getBufferActions().addAll(strategy.getHuntedBiome().run(strategy.getHuntedBiome().getDirection()));
        }
        else{ // le résultat n'est pas intéressant
            if(strategy.isAvoid()){ // si on arrive sur un biome est qu'on a pas encore explore, avoid est sur false, à partir du moment où l'on a déjà trouvé une ressource, il est sur true
                if(!strategy.getHuntedBiome().getReverse()){ //on parcours le biome dans l'autre sens
                    strategy.getHuntedBiome().setReverse(true);
                    this.islandMap.getPositionActuelle().findPath(strategy.getHuntedBiome().getTile());
                    strategy.getBufferActions().add(Scout.buildAction(Direction.W));
                }
                else{ // on a parcours le biome dans un sens puis dans l'autre, donc on a terminé sur ce biome là, on passe au suivant
                    strategy.startMove();
                }
            }
            else { // on a pas encore trouvé de ressource sur ce biome
                strategy.getBufferActions().clear();
                strategy.getBufferActions().addAll(strategy.getHuntedBiome().change(strategy.getHuntedBiome().getDirection()));
                strategy.notFoundRessource();
            }
        }
    }

    /**
     * détermine si la ressource qu'on a scout est intéressante
     * */
    private boolean[] gotInterestingResource(HuntedBiome huntedBiome) {
        boolean interest = false;
        boolean water = false;
        boolean[] response = {water,interest};

        for (Ressource current : this.getRessources()){
            for (Biome ResearchedBiome : current.getAssociatedBiomes()) {

                if (ResearchedBiome == huntedBiome.getBiome() ||current == huntedBiome.getResource() ) {
                    response[1] = true;
                }
            }
        }

        return response;
    }

    public static JSONObject buildAction(Direction currentHeading) {
        JSONObject cmd = new JSONObject();
        cmd.put("action", "scout");

        JSONObject parametersDirection = new JSONObject();
        parametersDirection.put("direction", currentHeading.getDescription());

        cmd.put("parameters", parametersDirection);
        return cmd;
    }

    public Direction getDirection() {
        return direction;
    }

    public ArrayList<Ressource> getRessources() {
        return ressources;
    }

    public int getAltitude() {
        return altitude;
    }
}
