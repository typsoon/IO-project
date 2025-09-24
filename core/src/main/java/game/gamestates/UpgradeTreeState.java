package game.gamestates;

import utils.FixedSizeArrayWrapper;

public record UpgradeTreeState(
        FixedSizeArrayWrapper<UpgradeNodeState> nodes
)
        implements IGameState {
}
