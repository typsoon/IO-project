package frontend.gamestate.processor;

import frontend.gamestate.DrawableInfo;
import frontend.gamestate.EntityVisibleState;
import frontend.gamestate.IDisplayableGameState;
import game.engine.PlayerConfig;
import game.engine.entities.EntityAction;
import game.engine.entities.EntityGroupID;
import game.engine.entities.inventory.InventoryInfo;
import game.engine.modules.IGeometryModule;
import game.gamestates.EntityState;
import game.gamestates.PlayerInventoryState;
import game.gamestates.PlayerState;
import game.utility.Point2F;
import game.utility.Vector2F;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import viewmodel.game.RenderableObjectFactory;
import viewmodel.game.RenderablePlayer;
import viewmodel.game.TimedRenderableObjectWithItem;

import java.util.List;

import static org.mockito.Mockito.*;

class GameStateProcessorTest {

    IGeometryModule geometryModule;
    RenderableObjectFactory objectFactory;
    IDisplayableGameState displayableGameState;
    PlayerConfig playerConfig;
    RenderablePlayer player;
    DrawableInfo playerDrawable;

    @BeforeEach
    void setup() {
        geometryModule = mock(IGeometryModule.class);
        objectFactory = mock(RenderableObjectFactory.class);
        displayableGameState = mock(IDisplayableGameState.class);
        playerConfig = mock(PlayerConfig.class);
        player = mock(RenderablePlayer.class);
        playerDrawable = mock(DrawableInfo.class);
        when(player.getDrawableInfo()).thenReturn(playerDrawable);
        when(player.getDrawable()).thenReturn(playerDrawable);

        when(objectFactory.createRenderablePlayer(playerConfig)).thenReturn(player);
    }

    @Test
    void testPlayerIsAddedOnConstruction() {
        new GameStateProcessor(geometryModule, objectFactory, displayableGameState, playerConfig, List.of());

        verify(displayableGameState).addPlayer(player);
        verify(displayableGameState).addDrawable(playerDrawable);
    }

    @Test
    void testProcessPlayerStateUpdatesPlayer() {
        GameStateProcessor processor = new GameStateProcessor(geometryModule, objectFactory, displayableGameState, playerConfig, List.of());

        PlayerState ps = mock(PlayerState.class);
        when(ps.position()).thenReturn(new Point2F(1, 2));
        when(ps.velocity()).thenReturn(new Vector2F(3, 4));
        when(ps.rotation()).thenReturn(1f);
        when(ps.action()).thenReturn(EntityAction.NONE);
        when(ps.actionProgress()).thenReturn(0.25f);
        when(ps.sightRange()).thenReturn(new Vector2F(10f, 10f));
        when(ps.maxHp()).thenReturn(100);
        when(ps.currentHp()).thenReturn(75);

        // 👉 nowe stuby dla obsługi itemów
        when(ps.holdingItemGroupId()).thenReturn(EntityGroupID.HUMAN_BASIC); // czyli brak itemu
        when(ps.itemAction()).thenReturn(EntityAction.NONE);
        when(ps.itemActionProgress()).thenReturn(0f);

        processor.processGameStates(List.of(ps), 0f);

        verify(playerDrawable).setState(EntityVisibleState.values()[EntityAction.NONE.ordinal()]);
        verify(playerDrawable).setStateTime(0.25f);

        verify(player).setPosition(new Point2F(1, 2));
        verify(player).setVelocity(new Vector2F(3, 4));
        verify(player).setRotation(1f);
        verify(player).setRange(new Vector2F(10f, 10f));
        verify(player).setMaxHp(100);
        verify(player).setCurrentHp(75);
    }

    @Test
    void testProcessPlayerInventoryStateUpdatesInventory() {
        GameStateProcessor processor = new GameStateProcessor(geometryModule, objectFactory, displayableGameState, playerConfig, List.of());

        PlayerInventoryState invState = mock(PlayerInventoryState.class);
        InventoryInfo inventory = mock(InventoryInfo.class);
        when(invState.inventory()).thenReturn(inventory);

        processor.processGameStates(List.of(invState), 0f);

        verify(player).setInventoryInfo(inventory);
    }

    @Test
    void testProcessEntityStateCreatesRenderableObject() {
        GameStateProcessor processor = new GameStateProcessor(geometryModule, objectFactory, displayableGameState, playerConfig, List.of());

        EntityState es = mock(EntityState.class);
        when(es.entityId()).thenReturn(1);
        when(es.action()).thenReturn(EntityAction.NONE);
        when(es.actionProgress()).thenReturn(0.1f);
        when(es.holdingItemGroupId()).thenReturn(EntityGroupID.HUMAN_BASIC); // no item
        when(es.itemAction()).thenReturn(EntityAction.NONE);
        when(es.itemActionProgress()).thenReturn(0f);

        TimedRenderableObjectWithItem tro = mock(TimedRenderableObjectWithItem.class);
        DrawableInfo drawable = mock(DrawableInfo.class);
        when(tro.getDrawable()).thenReturn(drawable);
        when(objectFactory.createRenderableObjectWithItem(es)).thenReturn(tro);

        processor.processGameStates(List.of(es), 0f);

        verify(displayableGameState).addDrawable(drawable);
        verify(drawable).setState(EntityVisibleState.values()[EntityAction.NONE.ordinal()]);
        verify(drawable).setStateTime(0.1f);
    }

    @Test
    void testCleanupRemovesOldEntities() {
        GameStateProcessor processor = new GameStateProcessor(geometryModule, objectFactory, displayableGameState, playerConfig, List.of());

        EntityState es = mock(EntityState.class);
        when(es.entityId()).thenReturn(1);
        when(es.action()).thenReturn(EntityAction.NONE);
        when(es.actionProgress()).thenReturn(0f);
        when(es.holdingItemGroupId()).thenReturn(EntityGroupID.HUMAN_BASIC); // no item
        when(es.itemAction()).thenReturn(EntityAction.NONE);
        when(es.itemActionProgress()).thenReturn(0f);

        TimedRenderableObjectWithItem tro = mock(TimedRenderableObjectWithItem.class);
        DrawableInfo drawable = mock(DrawableInfo.class);
        when(tro.getDrawable()).thenReturn(drawable);
        when(objectFactory.createRenderableObjectWithItem(es)).thenReturn(tro);

        // Add entity
        processor.processGameStates(List.of(es), 0f);

        // Simulate time passing
        processor.processGameStates(List.of(), 1f);

        verify(displayableGameState).removeDrawable(drawable);
        verify(tro).dispose();
    }


    @Test
    void testGeometryModuleCycleIsCalledAccordingToDeltaTime() {
        GameStateProcessor processor = new GameStateProcessor(geometryModule, objectFactory, displayableGameState, playerConfig, List.of());

        processor.processGameStates(List.of(), 1f);

        verify(geometryModule, atLeastOnce()).cycle();
    }
}
