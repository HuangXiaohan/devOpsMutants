package fr.unice.polytech.qgl.qda.Game;

import fr.unice.polytech.qgl.qda.actions.Action;
import fr.unice.polytech.qgl.qda.actions.aerial.Echo;
import fr.unice.polytech.qgl.qda.actions.aerial.Fly;
import fr.unice.polytech.qgl.qda.actions.aerial.Heading;
import fr.unice.polytech.qgl.qda.actions.aerial.Scan;
import fr.unice.polytech.qgl.qda.actions.ground.*;

/*
interface  ActionBuilder {
    Action build(...)

}

    public class FLyBuilder implements ActionBuilder {
    Action build(..) {
        return new ...
    }
*/
/**
 * IslandProject created on 17/11/2015 by Keynes Timothy - Aloysius_tim
 */
public enum AvailableActions {
    FLY(Phase.PHASE1, RangePrice.CHEAP, "the cost is almost constant", Fly.class),
    HEADING(Phase.PHASE1, RangePrice.MEDIUM, "the cost is almost constant", Heading.class),
    ECHO(Phase.PHASE1, RangePrice.CHEAP, "the cost is almost constant", Echo.class),
    SCAN(Phase.PHASE1, RangePrice.MEDIUM, "the cost is almost constant", Scan.class),
    STOP(Phase.PHASE1, RangePrice.VARIABLE, "the distance to the homeport", Stop.class),
    LAND(Phase.PHASE1, RangePrice.EXPENSIVE, "the distance to the designed creek", Land.class),
    MOVE_TO(Phase.PHASE1and2, RangePrice.VARIABLE, "number of mens, hostility of the environment (e.g., biomes, pitch)", Move_to.class),
    SCOUT(Phase.PHASE1and2, RangePrice.CHEAP, "", Scout.class),
    GLIMPSE(Phase.PHASE2, RangePrice.MEDIUM, "", Glimpse.class),
    EXPLORE(Phase.PHASE2, RangePrice.EXPENSIVE, "", Explore.class),
    EXPLOIT(Phase.PHASE2, RangePrice.EXPENSIVE, "", Exploit.class),
    TRANSFORM(Phase.PHASE2, RangePrice.MEDIUM, "", Transform.class);

    private Phase phase;
    private RangePrice cost;
    private String influenceFactor;
    private Class aClass;

    AvailableActions(Phase phase, RangePrice cost, String influenceFactor, Class flyClass) {
        this.phase=phase;
        this.cost=cost;
        this.influenceFactor=influenceFactor;
        this.aClass = flyClass;
    }

    public RangePrice getCost() {
        return cost;
    }

    public String getInfluenceFactor() {
        return influenceFactor;
    }

    public Phase getPhase() {
        return phase;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public Class<Action> getAssociatedClass() {
        return aClass;
    }

    enum RangePrice {
        CHEAP, MEDIUM, VARIABLE, EXPENSIVE
    }

    public enum Phase {
        PHASE1, PHASE2, PHASE1and2
    }
}