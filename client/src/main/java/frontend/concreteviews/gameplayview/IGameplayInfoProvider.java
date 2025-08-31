package frontend.concreteviews.gameplayview;

import game.utility.Point2F;

public interface IGameplayInfoProvider {
    Point2F castWorldCordinatesToGameCordinates(float worldX, float worldY);

    /**
     * @return when in game it returns the player position
     */
    Point2F getCenterOfInterest();
}
