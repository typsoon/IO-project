package frontend.concreteviews.gameplayview.impl;

import java.util.function.Supplier;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import frontend.concreteviews.gameplayview.IGameplayInfoProvider;
import game.utility.Point2F;

public class GameplayInfoProviderImpl implements IGameplayInfoProvider {
    private final Viewport viewport;
    private final Supplier<Point2F> centerOfInterestGetter;

    public GameplayInfoProviderImpl(final Viewport viewport, final Supplier<Point2F> centerOfInterestGetter) {
        this.viewport = viewport;
        this.centerOfInterestGetter = centerOfInterestGetter;
    }

    @Override
    public final Point2F castScreenCordinatesToGameCordinates(final float worldX, final float worldY) {
        var projectionRes = viewport.unproject(new Vector2(worldX, worldY));
        // var logger = Logger.getGlobal();
        // logger.info("%f %f world %s game".formatted(worldX, worldY, projectionRes));

        return new Point2F(projectionRes.x, projectionRes.y);
    }

    @Override
    public final Point2F getCenterOfInterest() {
        return centerOfInterestGetter.get();
    }
}
