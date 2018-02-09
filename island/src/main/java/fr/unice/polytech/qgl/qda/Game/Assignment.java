package fr.unice.polytech.qgl.qda.Game;

import fr.unice.polytech.qgl.qda.Island.Direction;
import fr.unice.polytech.qgl.qda.Island.Ressource;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Stream;

/**
 * IslandProject created on 17/11/2015 by Keynes Timothy - Aloysius_tim
 */
public class Assignment extends HashMap<Ressource, Integer> {
    private int nbMan;
    private int budget;
    private Direction heading;
    private HashMap<Ressource, Integer> initialContract;

    public Assignment(JSONObject contracts) {
        this.initialContract=new HashMap<>();

        this.budget = contracts.getInt("budget");
        this.heading = Direction.getHeading(contracts.getString("heading"));

        this.nbMan = contracts.getInt("men");

        for(Object json : contracts.getJSONArray("contracts")){
            JSONObject tmp = (JSONObject) json;
            Ressource ressource=Ressource.valueOf(tmp.getString("resource"));
            int requestedAmmount=tmp.getInt("amount");

            initialContract.put(ressource, requestedAmmount);

            if (ressource.getTypeRessource() == Ressource.TypeRessource.PRIMARY){
                if (this.containsKey(ressource))
                    this.replace(ressource, this.get(ressource)+requestedAmmount);
                else
                    this.put(ressource, requestedAmmount);
            }else{
                Iterator it = ressource.getRecipe().entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    if (this.containsKey(pair.getKey())){
                        this.replace((Ressource) pair.getKey(), (int) (this.get(pair.getKey()) +requestedAmmount*((Double)pair.getValue())));
                    }else{
                        this.put((Ressource) pair.getKey(), (int) (requestedAmmount*((double)pair.getValue())));
                    }
                }
            }

        }
        //Debug.println(this.toString());
    }

    public <K, V extends Comparable<? super V>> Map<Ressource, Integer> sort() {
        Map<Ressource, Integer> result = new LinkedHashMap<>();
        Stream<Entry<Ressource, Integer>> st = this.entrySet().stream();

        st.sorted(Comparator.comparing(e -> e.getValue())).forEachOrdered(e ->result.put(e.getKey(),e.getValue()));

        return result;
    }

    public int getBudget() {
        return budget;
    }

    public Direction getHeading() {
        return heading;
    }

    public int getNbMan() {
        return nbMan;
    }

    public HashMap<Ressource, Integer> getInitialContract() {
        return initialContract;
    }
}