package game.engine.entities.inventory;

import utils.ISendable;

public record ResourceInfo(
        int amount,
        Resource resource) implements ISendable {
}
