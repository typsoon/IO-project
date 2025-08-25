package game.engine.entities;

import game.actions.Direction;
import game.actions.PlayerMove;
import game.actions.PlayerSlotUse;
import game.actions.UsageType;
import game.utility.Vector2F;


//setters could be added and getters(or protected field if same package as Player)
public class MoveSet {
    public PlayerMove move;
    public PlayerSlotUse slotUse;
    public MoveSet(){
        move = new PlayerMove(Direction.NONE);
        slotUse = new PlayerSlotUse(UsageType.NONE,new Vector2F(0,0),0);
    }
    public MoveSet(PlayerMove move, PlayerSlotUse slotUse){
        this.move = move;
        this.slotUse = slotUse;
    }
}
