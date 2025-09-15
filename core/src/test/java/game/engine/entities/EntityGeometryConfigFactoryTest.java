package game.engine.entities;

import game.engine.entities.geometry.EntityGeometryConfig;
import game.engine.entities.geometry.EntityGeometryConfigFactory;
import game.engine.entities.geometry.GeometryConfigID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class EntityGeometryConfigFactoryTest {

    @Test
    void testCreateEntityGeometryConfigReturnsNonNull() {
        for (GeometryConfigID id : GeometryConfigID.values()) {
            EntityGeometryConfig config = EntityGeometryConfigFactory.createEntityGeometryConfig(id);
            assertNotNull(config);
        }
    }
}