package game.engine.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntityGeometryConfigFactoryTest {

    @Test
    void testCreateEntityGeometryConfigReturnsNonNull() {
        for (GeometryConfigID id : GeometryConfigID.values()) {
            EntityGeometryConfig config = EntityGeometryConfigFactory.createEntityGeometryConfig(id);
            assertNotNull(config);
        }
    }
}