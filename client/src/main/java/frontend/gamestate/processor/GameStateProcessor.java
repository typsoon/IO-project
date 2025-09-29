package frontend.gamestate.processor;

import frontend.gamestate.DrawableInfo;
import frontend.gamestate.EntityVisibleState;
import frontend.gamestate.IDisplayableGameState;
import frontend.gamestate.overlays.DeathOverlayData;
import game.engine.PlayerConfig;
import game.engine.entities.EntityGroupID;
import game.engine.modules.IGeometryModule;
import game.gamestates.*;
import viewmodel.game.RenderableObjectFactory;
import viewmodel.game.RenderablePlayer;
import viewmodel.game.TimedRenderableObjectWithItem;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GameStateProcessor implements IGameStateProcessor {

    private static final float TIME_STEP = 1f / 64f;
    private float accumulatedTime = 0f;
    private final float timeThreshold = 0.5f;

    private final RenderablePlayer player;

    IGeometryModule geometryModule;
    RenderableObjectFactory renderableObjectFactory;
    IDisplayableGameState displayableGameState;
    Map<Integer, TimedRenderableObjectWithItem> entities = new HashMap<>();


    public GameStateProcessor(IGeometryModule geometryModule, RenderableObjectFactory objectFactory,
                              IDisplayableGameState displayableGameState, PlayerConfig playerConfig,
                              Collection<IGameState> initialGameStates) {
        this.geometryModule = geometryModule;
        this.renderableObjectFactory = objectFactory;
        this.displayableGameState = displayableGameState;
        this.player = objectFactory.createRenderablePlayer(playerConfig);
        this.displayableGameState.addPlayer(player);
        this.displayableGameState.addDrawable(player.getDrawableInfo());

        processGameStates(initialGameStates, 0);
    }

    @Override
    public void processGameStates(Collection<IGameState> gameStates, float deltaTime) {
        accumulatedTime += deltaTime;
        while (accumulatedTime >= TIME_STEP) {
            geometryModule.cycle();
            accumulatedTime -= TIME_STEP;
        }
        for (IGameState gameState : gameStates) {
            switch (gameState) {
                case PlayerDeathState deathState -> updateDeathState(deathState);
                case EntityState entityState -> updateEntityState(entityState);
                case PlayerState playerState -> updatePlayerState(playerState);
                case PlayerInventoryState playerInventoryState -> updatePlayerInventoryState(playerInventoryState);
                default -> {
                }
            }
        }
        cleanupEntities(deltaTime);
    }

    private void updateDeathState(PlayerDeathState deathState) {
        displayableGameState.getOverlaysData().setDeathOverlayData(Optional.of(new DeathOverlayData()));
    }

    private void updatePlayerState(PlayerState playerState) {
        if (playerState.holdingItemGroupId() != EntityGroupID.HUMAN_BASIC) {
            if (player.getItemInfo() == null) {
                DrawableInfo itemInfo = new DrawableInfo(playerState.holdingItemGroupId(), EntityVisibleState.values()[playerState.itemAction().ordinal()]);
                itemInfo.setStateTime(playerState.itemActionProgress());
                player.setItemInfo(itemInfo);
                displayableGameState.addDrawable(itemInfo);
            } else {
                player.getItemInfo().setStateTime(playerState.itemActionProgress());
                player.getItemInfo().setEntityGroupID(playerState.holdingItemGroupId());
                player.getItemInfo().setState(EntityVisibleState.values()[playerState.itemAction().ordinal()]);
            }
        } else if (player.getItemInfo() != null) {
            displayableGameState.removeDrawable(player.getItemInfo());
            player.setItemInfo(null);
        }
        player.getDrawable().setState(EntityVisibleState.values()[playerState.action().ordinal()]);
        player.getDrawable().setStateTime(playerState.actionProgress());

        player.setPosition(playerState.position());
        player.setVelocity(playerState.velocity());
        player.setRotation(playerState.rotation());

        player.setRange(playerState.sightRange());
        player.setMaxHp(playerState.maxHp());
        player.setCurrentHp(Math.max(playerState.currentHp(), 0));
    }

    private void updatePlayerInventoryState(PlayerInventoryState playerInventoryState) {
        player.setInventoryInfo(playerInventoryState.inventory());
    }

    private void updateEntityState(EntityState entityState) {
        TimedRenderableObjectWithItem renderableObject;
        if (entities.containsKey(entityState.entityId())) {
            renderableObject = entities.get(entityState.entityId());
        } else {
            renderableObject = renderableObjectFactory.createRenderableObjectWithItem(entityState);
            entities.put(entityState.entityId(), renderableObject);
            displayableGameState.addDrawable(renderableObject.getDrawable());
        }

        if (entityState.holdingItemGroupId() != EntityGroupID.HUMAN_BASIC) {
            if (renderableObject.getItemInfo() == null) {
                DrawableInfo itemInfo = new DrawableInfo(entityState.holdingItemGroupId(), EntityVisibleState.values()[entityState.itemAction().ordinal()]);
                itemInfo.setStateTime(entityState.itemActionProgress());
                renderableObject.setItemInfo(itemInfo);
                displayableGameState.addDrawable(itemInfo);
            } else {
                renderableObject.getItemInfo().setStateTime(entityState.itemActionProgress());
                renderableObject.getItemInfo().setEntityGroupID(entityState.holdingItemGroupId());
                renderableObject.getItemInfo().setState(EntityVisibleState.values()[entityState.itemAction().ordinal()]);
            }
        } else if (renderableObject.getItemInfo() != null) {
            displayableGameState.removeDrawable(renderableObject.getItemInfo());
            renderableObject.setItemInfo(null);
        }

        renderableObject.getDrawable().setState(EntityVisibleState.values()[entityState.action().ordinal()]);
        renderableObject.getDrawable().setStateTime(entityState.actionProgress());
        renderableObject.setPosition(entityState.position());
        renderableObject.setVelocity(entityState.velocity());
        renderableObject.setRotation(entityState.rotation());

        renderableObject.timeSinceUpdate = 0f;
    }

    private void cleanupEntities(float deltaTime) {
        entities.entrySet().removeIf(entry -> {
            TimedRenderableObjectWithItem renderableObject = entry.getValue();
            renderableObject.timeSinceUpdate += deltaTime;
            if (renderableObject.timeSinceUpdate >= timeThreshold) {
                displayableGameState.removeDrawable(renderableObject.getDrawable());
                if (renderableObject.getItemInfo() != null) {
                    displayableGameState.removeDrawable(renderableObject.getItemInfo());
                }
                renderableObject.dispose();
                return true;
            }
            return false;
        });
    }
}
