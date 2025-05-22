package game.actions;

import game.Utility.Vector2F;

public enum Direction {
    N,
    NE,
    E,
    SE,
    S,
    SW,
    W,
    NW;
    public Direction opposite() {
        return values()[(this.ordinal() + 4) % 8];
    }
    public Vector2F vector() {
        return switch (this) {
            case N -> new Vector2F(0, -1);
            case NE -> new Vector2F(1, -1).normalize();
            case E -> new Vector2F(1, 0);
            case SE -> new Vector2F(1, 1).normalize();
            case S -> new Vector2F(0, 1);
            case SW -> new Vector2F(-1, 1).normalize();
            case W -> new Vector2F(-1, 0);
            case NW -> new Vector2F(-1, -1).normalize();
        };
    }
}
