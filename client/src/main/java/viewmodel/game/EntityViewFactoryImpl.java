package viewmodel.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import game.engine.modules.GeometryFactory;
import game.gamestates.EntityState;

// Implementation of EntityViewFactory that creates EntityView instances.
// This is closely tied to the ClientGameManager and as such requires its GeometryFactory.
public class EntityViewFactoryImpl implements EntityViewFactory {
    private final GeometryFactory geometryFactory;
    private final TextureAtlas atlas;

    public EntityViewFactoryImpl(GeometryFactory geometryFactory, TextureAtlas atlas) {
        this.geometryFactory = geometryFactory;
        this.atlas = atlas;
    }

    public EntityView createEntityView(EntityState entityState) {
        var geometryConfig = EntityGeometryConfigFactory.createEntityGeometryConfig(entityState.geometryConfigId());
        var geometryRepresentation = geometryFactory.createGeometryRepresentation(
                geometryConfig,
                entityState.position().x(),
                entityState.position().y()
        );
        var regionName = atlas.findRegion(""); // TODO: EntityGeometryConfig or something needs region name
        Sprite sprite = new Sprite(regionName);
        EntityView entityView = new EntityView(geometryRepresentation, sprite);
        entityView.setPosition(entityState.position());
        // entityView.setRotation(entityState.; // TODO: Add rotation to EntityState
        entityView.setVelocity(entityState.velocity());
        return entityView;
    }
}
