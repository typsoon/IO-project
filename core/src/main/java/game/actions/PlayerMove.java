package game.actions;

public record PlayerMove(
    Direction direction
) implements Action {}
