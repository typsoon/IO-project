package game.engine.entities;

import game.actions.*;
import game.utility.Point2F;
import game.utility.Vector2F;


//setters could be added and getters(or protected field if same package as Player)
public class MoveSet {
    public PlayerMove move;
    public PlayerSlotUse slotUse;
    public PlayerInteraction interaction;

    public MoveSet() {
        move = new PlayerMove(Direction.NONE);
        slotUse = new PlayerSlotUse(UsageType.NONE, new Vector2F(0, 0), 0);
        interaction = new PlayerInteraction(new Point2F(0, 0), InteractionType.NONE);
    }

    public MoveSet(PlayerMove move, PlayerSlotUse slotUse, PlayerInteraction interaction) {
        this.move = move;
        this.slotUse = slotUse;
        this.interaction = interaction;
    }
}
