package game.engine.entities;

import game.actions.IInteraction;
import game.engine.modules.IGeometryRepresentation;
import game.gamestates.IGameState;

public interface IInteractable {
    void interact(IInteraction interaction, IEntity source);

    void endInteract(IEntity source);

    IGameState getState();

    IGeometryRepresentation getGeometryRepresentation();
}
