package viewmodel.game;

import frontend.gamestate.DrawableInfo;
import frontend.gamestate.EntityVisibleState;
import game.engine.PlayerConfig;
import game.engine.entities.EntityGeometryConfigFactory;
import game.engine.entities.EntityGroupID;
import game.engine.modules.IGeometryFactory;
import game.gamestates.EntityState;

public class RenderableObjectFactory {
    private final IGeometryFactory geometryFactory;

    public RenderableObjectFactory(IGeometryFactory geometryFactory) {
        this.geometryFactory = geometryFactory;
    }

    public TimedRenderableObject createRenderableObject(EntityState entityState) {
        var geometryConfig = EntityGeometryConfigFactory.createEntityGeometryConfig(entityState.geometryConfigID());
        var geometryRepresentation = geometryFactory.createGeometryRepresentation(
                geometryConfig,
                entityState.position().x(),
                entityState.position().y()
        );
        DrawableInfo drawableInfo = new DrawableInfo(entityState.entityGroupId(),EntityVisibleState.Standing);
        TimedRenderableObject renderableObject = new TimedRenderableObject(geometryRepresentation, drawableInfo);
        renderableObject.setPosition(entityState.position());
        renderableObject.setRotation(entityState.rotation());
        renderableObject.setVelocity(entityState.velocity());
        return renderableObject;
    }

    public RenderablePlayer createRenderablePlayer(PlayerConfig playerConfig) {
        var geometryConfig = EntityGeometryConfigFactory.createEntityGeometryConfig(playerConfig.geometryConfigID());
        var geometryRepresentation = geometryFactory.createGeometryRepresentation(
                geometryConfig,
                0, 0
        );
        DrawableInfo drawableInfo = new DrawableInfo(playerConfig.entityGroupID(), EntityVisibleState.Standing);
        drawableInfo.setEntityGroupID(EntityGroupID.HUMAN_BASIC);
        return new RenderablePlayer(playerConfig, geometryRepresentation, drawableInfo);
    }
}
