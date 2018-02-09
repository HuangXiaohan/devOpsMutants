package fr.unice.polytech.qgl.qda.strategy.GroundPhase.SpecificPattern;

import fr.unice.polytech.qgl.qda.Game.Assignment;
import fr.unice.polytech.qgl.qda.Island.*;
import fr.unice.polytech.qgl.qda.actions.Action;
import fr.unice.polytech.qgl.qda.actions.ground.*;
import fr.unice.polytech.qgl.qda.strategy.Strategy;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by justin on 08/12/15.
 * stratégie qui s'occupe de toute la phase terrestre avec HuntedBiome
 */

public class FindStrategy extends Strategy {
    private HuntedBiome huntedBiome;
    protected LinkedList<Ressource> findedRessource;
    private ArrayList<Tile> tileShown = new ArrayList<>();
    private Tile creek;
    private boolean avoid = false;

    private final int RANGE = 0;

    /** ORDRE DE PRIORITE DES CONTRATS **/
    private Ressource[] interestRessource = {Ressource.FLOWER,Ressource.SUGAR_CANE,Ressource.WOOD,Ressource.FUR,Ressource.FRUITS,Ressource.FISH,Ressource.ORE,Ressource.QUARTZ};

    public FindStrategy(IslandMap islandMap, Assignment assignment, LinkedList<JSONObject> bufferActions, LinkedList<Action> actionsHistory, int remainingBudget, HashMap<Ressource,ArrayList<Tile>> tileList) {
        super(islandMap, assignment, bufferActions, actionsHistory, remainingBudget, tileList);

        this.findedRessource = new LinkedList<>();

        int moyenne=0;

        Iterator it2 = tileList.entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry pair = (Map.Entry)it2.next();
            moyenne+=((ArrayList<Tile>)pair.getValue()).size();
        }

        moyenne/=tileList.size();

        Iterator it = tileList.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            if (((ArrayList<Tile>)pair.getValue()).size()<moyenne*0.4) it.remove();
        }

        this.bufferActions.add(Land.buildAction(this.islandMap.getCreeks().get(0), 1));
    }

    /**
     * Cherche le tile intéressant le plus proche entre la creek sur lequel on est arrivé et les tiles enregistrés par la phase aérienne
     * @param fromThis
     */
    public void findNearestTile(Tile fromThis){
        Tile min = null;
        int prevDistance = Integer.MAX_VALUE;

        for (int i=0; i<interestRessource.length; i++){
            for (Ressource res : tileList.keySet()) { // Parcours le dictionnaire associant à chaque ressources, une List contenant les tiles qui possède la ressource en question
                if(res == interestRessource[i]) { // Si la ressource est au niveau de priorité courant

                    for (Tile tile : tileList.get(res)) { // Parcours la liste des tile du tableau de tile possedant la ressource
                        int distance = fromThis.findDistance(tile); // calcule la distance entre la Tile FromThis et chacune des tiles du tableau pour trouver la plus proche
                        if (distance < prevDistance) {
                            min = tile;
                            prevDistance = distance;
                        }
                    }
                    if(min != null){ // Si on à trouvé une tile

                        for(Tile t : tileList.get(res)){ // On parcours a nouveau le tableau
                            if(t.getX() == min.getX() && t.getY() == min.getY()){ // On cherche la tile la plus proche dans le tableau avec celle qu'on à trouvé précédemment
                                for(Biome biome:t.getRelatedBiomes().keySet()){ // On parcours tous les biomes existant sur la Tile (J'ai du remonter 3-4 classe pour comprendre sa....)
                                    if(res.getAssociatedBiomes().contains(biome)){ // Si la liste des biome contenant notre ressources, contient un des biome de la tile
                                        huntedBiome = new HuntedBiome(min,res,biome); // on a une nouvelle cible
                                        break;
                                    }
                                }
                                tileList.get(res).remove(t);
                                return;
                            }
                        }
                    }
                }

            }
        }
    }

    /**
     * on recherche un nouveau Tile (biome) à explorer  et on s'y rend
     */
    public void startMove(){
        bufferActions.clear();
        huntedBiome = null;
        avoid = false;
        findNearestTile(creek);
        if (huntedBiome == null) {
            //this.bufferActions.add(Stop.buildAction());
            this.setEndOfStrat(true);
        }else {
            this.bufferActions.addAll(this.islandMap.getPositionActuelle().findPath(huntedBiome.getTile()));
            this.bufferActions.add(Explore.buildAction());
        }
    }

    //permet de supprimer les tiles (dans la liste de la phase aérienne) adjacents à celui qu'on chasse pour éviter de retourner plusieurs fois sur le même
    // biome déjà visité, on ne l'utilise pas (RANGE = 0) car on passe parfois à coté d'un tile qui aurait pu nous intéresser
    public void actualiseList(Tile currentTile){
        for(Ressource res : tileList.keySet()){
            if(currentTile.getAssociatedRessources().contains(res)){
                for(Tile tile: tileList.get(res)){
                    int distance = currentTile.findDistance(tile);
                    if(distance <= RANGE ){
                        tileList.get(res).remove(tile);
                        return;
                    }
                }
            }
        }
    }

    public void notFoundRessource(){
        if (bufferActions.size() == 0) {
            if (huntedBiome.getEnd()) {
                startMove();
            } else if (huntedBiome.getReverse()) {
                this.bufferActions.addAll(this.islandMap.getPositionActuelle().findPath(huntedBiome.getTile()));
                this.bufferActions.add(Scout.buildAction(Direction.W));
            }
        }
    }

    // on actualise les ressources manquante pour effectuer les contrats
    // si une ressource d'un contrat passe à 0 ou moins, on le supprime des contrats restant
    public void updateList(int amount){
        boolean keep = false;
        if(this.assignment.containsKey(findedRessource.getFirst())) {
            //this.assignment.get(findedRessource.getFirst()).update(amount);

            this.assignment.replace(findedRessource.getFirst(), this.assignment.get(findedRessource.getFirst())-amount);

            if (this.assignment.get(findedRessource.getFirst()) <= 0) {
                this.assignment.remove(findedRessource.getFirst());
                this.tileList.remove(findedRessource.getFirst());
                for(Ressource res : tileList.keySet()){
                    for( Biome biome : res.getAssociatedBiomes()){
                        if(biome == huntedBiome.getBiome()){
                            keep = true;
                        }
                    }
                }

                if(!keep) {
                    bufferActions.clear();
                    startMove();
                }

            }
            findedRessource.removeFirst();
        }
    }

    @Override
    public void interpretAcknowledgeResult(JSONObject acknowledgeResult) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        super.interpretAcknowledgeResult(acknowledgeResult);
        ((IGroundStrategy)this.actionsHistory.getLast()).findStrategy(this);
    }

    @Override
    public JSONObject getNextMove() {
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
    public Strategy getNextStrategy() {
        return new CraftStrategy(this.islandMap, this.assignment, this.bufferActions, this.actionsHistory, this.remainingBudget, this.getTileList());
    }

    public HashMap<Ressource, ArrayList<Tile>> getTileList() {
        return tileList;
    }
    public HuntedBiome getHuntedBiome() {
        return huntedBiome;
    }
    public LinkedList<Ressource> getFindedRessource() {
        return findedRessource;
    }
    public ArrayList<Tile> getTileShown() {
        return tileShown;
    }
    public Tile getCreek() {
        return creek;
    }
    public boolean isAvoid() {
        return avoid;
    }
    public void setCreek(Tile creek) {
        this.creek = creek;
    }
    public void setAvoid(boolean avoid) {
        this.avoid = avoid;
    }
}