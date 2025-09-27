package game.engine.entities.items.attacks;

import game.engine.IWorldView;
import game.engine.entities.IEntity;
import game.utility.Rectangle2F;

import java.util.Collection;

public class RectangleSlash implements IAttack {

    private final float attackRangeHeight = 2f;
    private final float attackRangeWidth = 2f;
    private final Damage damage = new Damage(DamageType.SLASH, 30);

    @Override
    public void attack(IWorldView view, IEntity user, DamageModifier modifier) {
        float x1 = user.geometryRepresentation().getPosition().x();
        float y1 = user.geometryRepresentation().getPosition().y() - attackRangeHeight / 2;
        Rectangle2F attackArea = new Rectangle2F(x1, y1,
                x1 + attackRangeWidth,
                y1 + attackRangeHeight
        );
        attackArea = attackArea.rotateBB(user.geometryRepresentation().getRotation(), user.geometryRepresentation().getPosition());

        //need predicate that really checks if entity is in area
        Collection<IEntity> entities = view.getEntitiesInArea(attackArea, entity -> true);

        for (IEntity entity : entities) {
            if (entity != user && entity instanceof IDamageable damageable) {
                damageable.takeDamage(modifier.modify(damage, damageable), user);
            }
        }
    }
}
