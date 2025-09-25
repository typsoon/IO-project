package game.engine.entities;

import game.engine.PlayerConfig;
import game.engine.entities.concreteentities.Player;
import game.engine.modules.IGeometryFactory;
import game.engine.modules.IManagingGeometryRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EntityFactoryTest {


    private EntityFactory factory;
    @SuppressWarnings("FieldCanBeLocal")
    private IGeometryFactory mockGeometryFactory;
    @SuppressWarnings("FieldCanBeLocal")
    private Consumer<IEntity> entityCallback;
    @SuppressWarnings("FieldCanBeLocal")
    private Consumer<IAIEntity> entityAICallback;
    @SuppressWarnings("FieldCanBeLocal")
    private Consumer<DeathData> onDeath;

    private AtomicInteger callbackCounter;

    @BeforeEach
    void setUp() {
        mockGeometryFactory = mock(IGeometryFactory.class);

        when(mockGeometryFactory.createGeometryRepresentation(any(), anyFloat(), anyFloat()))
                .thenAnswer(inv -> mock(IManagingGeometryRepresentation.class));

        factory = new EntityFactory(mockGeometryFactory);

        callbackCounter = new AtomicInteger(0);

        entityCallback = e -> callbackCounter.incrementAndGet();
        entityAICallback = e -> callbackCounter.incrementAndGet();
        onDeath = e -> callbackCounter.incrementAndGet();

        factory.setCallbacks(entityCallback, entityAICallback, onDeath);
    }

    @Test
    void testPlayerCallbackAndId() {
        PlayerConfig config = mock(PlayerConfig.class);

        Player player1 = factory.createPlayer(config, 10f, 20f);
        Player player2 = factory.createPlayer(config, 30f, 40f);

        assertEquals(0, player1.getEntityState().entityId());
        assertEquals(1, player2.getEntityState().entityId());

        assertEquals(4, callbackCounter.get());
    }

    @Test
    void testChickenCallbackAndId() {
        IAIEntity chicken1 = factory.createChicken(5f, 5f);
        IAIEntity chicken2 = factory.createChicken(15f, 25f);

        assertEquals(0, chicken1.getEntityState().entityId());
        assertEquals(1, chicken2.getEntityState().entityId());

        assertEquals(4, callbackCounter.get());
    }

    @Test
    void testMixedEntitiesIdIncrement() {
        PlayerConfig config = mock(PlayerConfig.class);

        Player player = factory.createPlayer(config, 0f, 0f);
        IAIEntity chicken = factory.createChicken(0f, 0f);

        assertEquals(0, player.getEntityState().entityId());
        assertEquals(1, chicken.getEntityState().entityId());
    }
}
