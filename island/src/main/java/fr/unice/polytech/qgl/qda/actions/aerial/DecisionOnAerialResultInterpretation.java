package fr.unice.polytech.qgl.qda.actions.aerial;

import fr.unice.polytech.qgl.qda.strategy.DronePhase.SpecificPattern.EdgeStrategy;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.SpecificPattern.SnakeStrategy;
import fr.unice.polytech.qgl.qda.strategy.Strategy;

/**
 * This interface is aimed to be implemented by every aerial actions class so that they can
 * bridge their result obtained from the information return by the engine toward correct srategy
 */
public interface DecisionOnAerialResultInterpretation {
    void takeDecisionOnEdgeStrat(EdgeStrategy strategy);
    void takeDecisionOnSnakeStrat(SnakeStrategy snakeStrategy);
}