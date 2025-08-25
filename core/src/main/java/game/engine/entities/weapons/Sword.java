package game.engine.entities.weapons;

import game.engine.IWorldView;
import game.engine.entities.IDamageable;
import game.engine.entities.IEntity;
import game.engine.modules.IGeometryRepresentation;

public class Sword implements IWeapon{

    private final float attackRangeHeight = 2f;
    private final float attackRangeWidth = 2f;
    private final Damage damage = new Damage(DamageType.SLASH,100);

    @Override
    public void attack(IWorldView view, IGeometryRepresentation representation) {
        //add rotation
        for(IEntity entity : view.getEntitiesInArea(representation.getPosition().x()-attackRangeWidth/2,
                representation.getPosition().y(),attackRangeWidth,attackRangeHeight)){
            if(entity instanceof IDamageable damageable){
                damageable.takeDamage(damage);
            }
        }
    }
}
