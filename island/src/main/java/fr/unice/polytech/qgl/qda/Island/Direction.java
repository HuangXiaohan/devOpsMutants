package fr.unice.polytech.qgl.qda.Island;

/**
 * IslandProject created on 18/11/2015 by Keynes Timothy - Aloysius_tim
 */
public enum Direction {
    E("E", 1, 0),
    W("W", -1, 0),
    N("N", 0, 1),
    S("S", 0, -1);

    private String description;
    private int movX;
    private int movY;
    Direction(String description, int movX, int movY) {
        this.description=description;
        this.movX=movX;
        this.movY=movY;
    }

    public String getDescription() {
        return description;
    }

    public int getMovX() {
        return movX;
    }

    public int getMovY() {
        return movY;
    }

    public Direction getReverse(){
        switch (this){
            case N : return S;
            case S : return N;
            case E : return W;
            case W : return E;
            default: return null;
        }
    }

    public Direction getRightDirection(){
        switch (this){
            case N : return E;
            case S : return W;
            case E : return S;
            case W : return N;
            default: return null;
        }
    }

    public Direction getLeftDirection(){
        switch (this){
            case N : return W;
            case S : return E;
            case E : return N;
            case W : return S;
            default: return null;
        }
    }

    public static Direction getHeading(String heading){
        switch(heading){
            case "N":return Direction.N;
            case "E":return Direction.E;
            case "S":return Direction.S;
            case "W":return Direction.W;
            default:return null;
        }
    }

    public boolean isReverse(Direction direction2){
        return getReverse().equals(direction2);
    }
}
