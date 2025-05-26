package viewmodel.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import game.engine.modules.IGeometryFactory;
import game.gamestates.EntityState;

import java.util.function.Consumer;

// Implementation of EntityViewFactory that creates EntityView instances.
// This is closely tied to the ClientGameManager and as such requires its GeometryFactory.
public class EntityViewFactory implements IEntityViewFactory {
    private final IGeometryFactory geometryFactory;
    private final TextureAtlas atlas;
    private final Consumer<Sprite> spriteConsumer;

    public EntityViewFactory(IGeometryFactory geometryFactory, TextureAtlas atlas, Consumer<Sprite> addSprite) {
        this.geometryFactory = geometryFactory;
        this.atlas = atlas;
        this.spriteConsumer = addSprite;
    }

    public IEntityView createEntityView(EntityState entityState) {
        var geometryConfig = EntityGeometryConfigFactory.createEntityGeometryConfig(entityState.geometryConfigId());
        var geometryRepresentation = geometryFactory.createGeometryRepresentation(
                geometryConfig,
                entityState.position().x(),
                entityState.position().y()
        );
        var regionName = atlas.findRegion(""); // TODO: EntityGeometryConfig or something needs region name
        Sprite sprite = new Sprite(regionName);
        spriteConsumer.accept(sprite);
        IEntityView entityView = new EntityView(geometryRepresentation, sprite);
        entityView.setPosition(entityState.position());
        // entityView.setRotation(entityState.; // TODO: Add rotation to EntityState
        entityView.setVelocity(entityState.velocity());
        return entityView;
    }
}
