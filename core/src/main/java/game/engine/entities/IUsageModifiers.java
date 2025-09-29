package game.engine.entities;

import game.engine.IWorldView;
import game.engine.entities.items.attacks.DamageModifier;
import game.engine.entities.items.attacks.DamageType;

import java.util.Collection;

public interface IUsageModifiers {
    DamageModifier getDamageModifier();

    void addBonusDamage(DamageType type, int bonusDamage);

    void addBonusDefense(DamageType type, int bonusDefense);

    void addOnHitEffect(Collection<DamageType> types, IDamageEffect effect);

    void addOnDamagedEffect(Collection<DamageType> types, IDamageEffect effect);

    void setWorldView(IWorldView view);
}
