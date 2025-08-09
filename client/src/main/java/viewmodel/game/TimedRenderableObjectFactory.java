package viewmodel.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import game.engine.modules.IGeometryFactory;
import game.gamestates.EntityState;

public class TimedRenderableObjectFactory {
    private final IGeometryFactory geometryFactory;
    private final ISpriteFactory spriteFactory;

    public TimedRenderableObjectFactory(IGeometryFactory geometryFactory, ISpriteFactory spriteFactory) {
        this.geometryFactory = geometryFactory;
        this.spriteFactory = spriteFactory;
    }

    public TimedRenderableObject createRenderableObject(EntityState entityState) {
        var geometryConfig = EntityGeometryConfigFactory.createEntityGeometryConfig(entityState.geometryConfigId());
        var geometryRepresentation = geometryFactory.createGeometryRepresentation(
                geometryConfig,
                entityState.position().x(),
                entityState.position().y()
        );
        Sprite sprite = spriteFactory.createSprite(entityState.spriteConfigId());
        TimedRenderableObject renderableObject = new TimedRenderableObject(geometryRepresentation, sprite);
        renderableObject.setPosition(entityState.position());
//        renderableObject.setRotation(entityState.rotation()); TODO: complete EntityState content
        renderableObject.setVelocity(entityState.velocity());
        return renderableObject;
    }
}
