package game.actions;

import game.utility.Vector2F;

public enum Direction {
    N(new Vector2F(0, 1)),
    NE(new Vector2F(1, 1).normalize()),
    E(new Vector2F(1, 0)),
    SE(new Vector2F(1, -1).normalize()),
    S(new Vector2F(0, -1)),
    SW(new Vector2F(-1, -1).normalize()),
    W(new Vector2F(-1, 0)),
    NW(new Vector2F(-1, 1).normalize()),
    NONE(new Vector2F(0, 0));

    public Direction opposite() {
        return values()[(this.ordinal() + 4) % 8];
    }

    private final Vector2F vector;

    public Vector2F vector() {
        return vector;
    }

    Direction(Vector2F vector) {
        this.vector = vector;
    }
}
