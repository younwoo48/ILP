package uk.ac.ed.inf;

public enum Direction {
    North(90),
    NorthNorthEast(67.5),
    NorthEast(45),
    EastNorthEast(22.5),
    East(0),
    EastSouthEast(337.5),
    SouthEast(315),
    SouthSouthEast(292.5),
    South(270),
    SouthSouthWest(247.5),
    SouthWest(225),
    WestSouthWest(202.5),
    West(180),
    WestNorthWest(157.5),
    NorthWest(135),
    NorthNorthWest(112.5);

    private final double angle;

    Direction(){this(0.0);}

    Direction(double angle){this.angle = angle;}
    public double angle() {return angle;}

}
