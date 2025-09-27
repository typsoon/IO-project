package game.gamestates;

import game.engine.entities.inventory.InventoryInfo;

public record PlayerInventoryState(
        InventoryInfo inventory
) implements IGameState {
}
