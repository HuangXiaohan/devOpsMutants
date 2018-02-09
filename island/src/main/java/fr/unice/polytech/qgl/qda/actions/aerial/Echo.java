package fr.unice.polytech.qgl.qda.actions.aerial;

import fr.unice.polytech.qgl.qda.Game.AvailableActions;
import fr.unice.polytech.qgl.qda.Island.Biome;
import fr.unice.polytech.qgl.qda.Island.Direction;
import fr.unice.polytech.qgl.qda.Island.IslandMap;
import fr.unice.polytech.qgl.qda.actions.Action;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.DroneStrategy;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.SpecificPattern.EdgeStrategy;
import fr.unice.polytech.qgl.qda.strategy.DronePhase.SpecificPattern.SnakeStrategy;
import org.json.JSONObject;

/**
 * IslandProject created on 17/12/2015 by Keynes Timothy - Aloysius_tim
 */
public class Echo extends Action implements DecisionOnAerialResultInterpretation {
    private final JSONObject acknowledgeResult;
    private Direction direction;
    private int range;
    private Biome biome;
    private boolean ground;

    public Echo(IslandMap islandMap, JSONObject action, JSONObject acknowledgeResult) {
        super(islandMap, action, acknowledgeResult);

        this.acknowledgeResult=acknowledgeResult;

        this.direction = Direction.getHeading(action.getJSONObject("parameters").getString("direction"));
        this.range = acknowledgeResult.getJSONObject("extras").getInt("range");
        this.biome = Biome.valueOf(acknowledgeResult.getJSONObject("extras").getString("found"));

        this.ground = acknowledgeResult.getJSONObject("extras").getString("found").equals("GROUND");


        this.islandMap.echo(direction, range, biome);

        this.actionType= AvailableActions.ECHO;

        /*this.ground = !(biome == Biome.OCEAN);*/
    }

    public static JSONObject buildAction(Direction currentHeading) {
        JSONObject cmd = new JSONObject();
        cmd.put("action", "echo");

        JSONObject parametersDirection = new JSONObject();
        parametersDirection.put("direction", currentHeading.getDescription());

        cmd.put("parameters", parametersDirection);
        return cmd;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getRange() {
        return range;
    }

    public Biome getBiome() {
        return biome;
    }

    public boolean hasGround() { return ground; }

    /**
     * méthode lié aux résultats des échos, le premier écho fait sert à déterminer si le drone est sur le côté gauche ou droit de l'île, les suivants serviront à changer de référenciel
     * ou à continuer sur le même
     * @param strategy
     */
    @Override
    public void takeDecisionOnEdgeStrat(EdgeStrategy strategy) {
        if(strategy.getInit()){
            strategy.setInit(false);
            if(ground){
                if(this.getDirection() == Direction.E)
                    strategy.setOnLeft(true);
                else
                    strategy.setOnLeft(false);
            }
            else{
                if(this.getDirection() == Direction.E)
                    strategy.setOnLeft(false);
                else
                    strategy.setOnLeft(true);
            }
            strategy.init();
        }
        else {
            if (ground) strategy.getBufferActions().add(Fly.buildAction());
            else strategy.headingNext();
            strategy.getBufferActions().add(Scan.buildAction());
        }
    }

    /**
     * méthode qui détermine le nombre de fly limit avant de sortire de la carte ainsi que le nombre de fly afin d'atteindre la terre
     * c'est cette méthode qui arrête la stratégie pour passer à la suivante
     * @param snakeStrategy
     */
    @Override
    public void takeDecisionOnSnakeStrat(SnakeStrategy snakeStrategy) {
        JSONObject extras = acknowledgeResult.getJSONObject("extras");
        int range = extras.getInt("range");
        boolean ground;
        ground = extras.getString("found").equals("GROUND");
        if(ground) {
            snakeStrategy.setNoGround(0);
            for (int i = 0; i <= range; i++) {
                snakeStrategy.getBufferActions().add(Fly.buildAction());
            }
            if(snakeStrategy.isLastStep()){
                snakeStrategy.setEndOfStrat(true);
            }
            else {
                snakeStrategy.getBufferActions().add(Scan.buildAction());
            }
        }
        else{
            int y = getIslandMap().getPositionActuelle().getY();
            snakeStrategy.setNoGround(snakeStrategy.getNoGround()+1);

            if(snakeStrategy.getNoGround()>1){

                if(snakeStrategy.isBottom() && y>0) {
                    snakeStrategy.getBufferActions().clear();
                    snakeStrategy.setEndOfStrat(true);
                }
                else if(!snakeStrategy.isBottom() && y<0){
                    snakeStrategy.getBufferActions().clear();
                    snakeStrategy.setEndOfStrat(true);
                }

                else{
                    forwardLimit(snakeStrategy);
                    snakeStrategy.nextFloor();
                    snakeStrategy.getBufferActions().add(Echo.buildAction(snakeStrategy.getCurrentHeading()));
                }
                snakeStrategy.setNoGround(0);
            }
            else {



                if((snakeStrategy.getPosition() == DroneStrategy.Position.UPPER_LEFT || snakeStrategy.getPosition() == DroneStrategy.Position.UPPER_RIGHT)&& y<snakeStrategy.LIMIT_S){
                    snakeStrategy.getBufferActions().clear();
                    snakeStrategy.setEndOfStrat(true);
                }
                else if((snakeStrategy.getPosition() == DroneStrategy.Position.BOTTOM_LEFT || snakeStrategy.getPosition() == DroneStrategy.Position.BOTTOM_RIGHT) && y>snakeStrategy.LIMIT_N){
                    snakeStrategy.getBufferActions().clear();
                    snakeStrategy.setEndOfStrat(true);
                }

                else {
                    if(snakeStrategy.getRemainingBudget()<snakeStrategy.BUDGET_LIMIT){
                        snakeStrategy.getBufferActions().clear();

                        snakeStrategy.setBottom(!snakeStrategy.isBottom());
                        forwardLimit(snakeStrategy);
                        snakeStrategy.nextFloor();
                        snakeStrategy.getBufferActions().add(Echo.buildAction(snakeStrategy.getCurrentHeading()));
                        snakeStrategy.setLastStep(true);
                    } else{
                        forwardLimit(snakeStrategy);
                        snakeStrategy.nextFloor();
                        snakeStrategy.getBufferActions().add(Echo.buildAction(snakeStrategy.getCurrentHeading()));
                    }
                }
            }

        }
    }

    /**
     * permet d'éviter de sortir de la carte en terme de coordonnée critique
     */
    public void forwardLimit(SnakeStrategy snakeStrategy){
        int currentX = getIslandMap().getPositionActuelle().getX();
        int diff;
        if(snakeStrategy.getCurrentHeading() == Direction.E) {
            diff = snakeStrategy.LIMIT_E - currentX;
        }
        else{
            diff = Math.abs(snakeStrategy.LIMIT_W - currentX);
        }
         while(diff>0){
            snakeStrategy.getBufferActions().add(Fly.buildAction());
            diff = diff - snakeStrategy.TILES_BY_FLY;
        }
    }
}