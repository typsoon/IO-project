package frontend.gamestate.processor;

import game.engine.PlayerConfig;
import game.engine.modules.IGeometryModule;
import game.gamestates.EntityState;
import game.gamestates.PlayerState;
import game.utility.Vector2F;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import viewmodel.game.RenderableObjectFactory;
import viewmodel.game.RenderablePlayer;
import viewmodel.game.TimedRenderableObject;
import frontend.gamestate.IDisplayableGameState;

import java.util.List;

import static org.mockito.Mockito.*;

class GameStateProcessorTest {

    IGeometryModule geometryModule;
    RenderableObjectFactory objectFactory;
    IDisplayableGameState displayableGameState;
    PlayerConfig playerConfig;
    RenderablePlayer player;

    @BeforeEach
    void setup() {
        geometryModule = mock(IGeometryModule.class);
        objectFactory = mock(RenderableObjectFactory.class);
        displayableGameState = mock(IDisplayableGameState.class);
        playerConfig = mock(PlayerConfig.class);
        player = mock(RenderablePlayer.class);

        when(objectFactory.createRenderablePlayer(playerConfig)).thenReturn(player);
    }

    @Test
    void testPlayerIsAddedOnConstruction() {
        new GameStateProcessor(geometryModule, objectFactory, displayableGameState, playerConfig, List.of());

        verify(displayableGameState).addPlayer(player);
        verify(displayableGameState).addDrawable(player.getDrawableInfo());
    }

    @Test
    void testProcessPlayerStateUpdatesPlayer() {
        GameStateProcessor processor = new GameStateProcessor(geometryModule, objectFactory, displayableGameState, playerConfig, List.of());

        PlayerState ps = mock(PlayerState.class);
        when(ps.position()).thenReturn(null);
        when(ps.velocity()).thenReturn(null);
        when(ps.rotation()).thenReturn(1f);
        when(ps.sightRange()).thenReturn(new Vector2F(10f,10f));

        processor.processGameStates(List.of(ps), 0f);

        verify(player).setPosition(null);
        verify(player).setVelocity(null);
        verify(player).setRotation(1f);
        verify(player).setRange(new Vector2F(10f,10f));
    }

    @Test
    void testProcessEntityStateCreatesRenderableObject() {
        GameStateProcessor processor = new GameStateProcessor(geometryModule, objectFactory, displayableGameState, playerConfig, List.of());

        EntityState es = mock(EntityState.class);
        when(es.entityId()).thenReturn(1);
        TimedRenderableObject tro = mock(TimedRenderableObject.class);
        when(objectFactory.createRenderableObject(es)).thenReturn(tro);

        clearInvocations(displayableGameState);
        processor.processGameStates(List.of(es), 0f);

        verify(displayableGameState).addDrawable(tro.getDrawable());
    }

    @Test
    void testCleanupRemovesOldEntities() {
        GameStateProcessor processor = new GameStateProcessor(geometryModule, objectFactory, displayableGameState, playerConfig, List.of());

        EntityState es = mock(EntityState.class);
        when(es.entityId()).thenReturn(1);
        TimedRenderableObject tro = mock(TimedRenderableObject.class);
        when(objectFactory.createRenderableObject(es)).thenReturn(tro);

        processor.processGameStates(List.of(es), 1f); // deltaTime > 0.5f threshold

        verify(tro).dispose();
        verify(displayableGameState).removeDrawable(tro.getDrawable());
    }

    @Test
    void testGeometryModuleCycleIsCalledAccordingToDeltaTime() {
        GameStateProcessor processor = new GameStateProcessor(geometryModule, objectFactory, displayableGameState, playerConfig, List.of());

        processor.processGameStates(List.of(), 1f);

        verify(geometryModule, atLeast(1)).cycle();
    }
}
