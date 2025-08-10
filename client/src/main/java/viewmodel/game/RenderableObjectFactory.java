package viewmodel.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.engine.PlayerConfig;
import game.engine.modules.IGeometryFactory;
import game.gamestates.EntityState;

public class RenderableObjectFactory {
    private final IGeometryFactory geometryFactory;
    private final ISpriteFactory spriteFactory;

    public RenderableObjectFactory(IGeometryFactory geometryFactory, ISpriteFactory spriteFactory) {
        this.geometryFactory = geometryFactory;
        this.spriteFactory = spriteFactory;
    }

    public TimedRenderableObject createRenderableObject(EntityState entityState) {
        var geometryConfig = EntityGeometryConfigFactory.createEntityGeometryConfig(entityState.geometryConfigID());
        var geometryRepresentation = geometryFactory.createGeometryRepresentation(
                geometryConfig,
                entityState.position().x(),
                entityState.position().y()
        );
        Sprite sprite = spriteFactory.createSprite(entityState.spriteID());
        TimedRenderableObject renderableObject = new TimedRenderableObject(geometryRepresentation, sprite);
        renderableObject.setPosition(entityState.position());
//        renderableObject.setRotation(entityState.rotation()); TODO: complete EntityState content
        renderableObject.setVelocity(entityState.velocity());
        return renderableObject;
    }

    public RenderablePlayer createRenderablePlayer(PlayerConfig playerConfig) {
        var geometryConfig = EntityGeometryConfigFactory.createEntityGeometryConfig(playerConfig.geometryConfigID());
        var geometryRepresentation = geometryFactory.createGeometryRepresentation(
                geometryConfig,
                0, 0
        );
        Sprite sprite = spriteFactory.createSprite(playerConfig.spriteID());
        return new RenderablePlayer(playerConfig, geometryRepresentation, sprite);
    }
}
