package game.engine.entities;

import game.engine.IWorldView;
import game.engine.entities.items.attacks.Damage;
import game.engine.entities.items.attacks.DamageModifier;
import game.engine.entities.items.attacks.DamageType;
import game.engine.entities.items.attacks.IDamageable;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

public class BasicUsageModifiers implements IUsageModifiers {
    Map<DamageType, Integer> bonusDamage = new EnumMap<>(DamageType.class) {{
        for (DamageType type : DamageType.values()) {
            put(type, 0);
        }
    }};
    Collection<IDamageEffect> onHitEffects = new java.util.ArrayList<>();

    Map<DamageType, Integer> defence = new EnumMap<>(DamageType.class) {{
        for (DamageType type : DamageType.values()) {
            put(type, 0);
        }
    }};
    Collection<IDamageEffect> onDamagedEffects = new java.util.ArrayList<>();

    private final IEntity owner;
    private IWorldView view;

    public BasicUsageModifiers(IEntity owner) {
        this.owner = owner;
    }

    public void setWorldView(IWorldView view) {
        this.view = view;
    }


    @Override
    public DamageModifier getDamageModifier() {
        return this::damageModifier;
    }

    @Override
    public void addBonusDamage(DamageType type, int bonusDamage) {
        this.bonusDamage.put(type, this.bonusDamage.get(type) + bonusDamage);
    }

    @Override
    public void addBonusDefense(DamageType type, int bonusDefense) {
        this.defence.put(type, this.defence.get(type) + bonusDefense);
    }

    @Override
    public void addOnHitEffect(Collection<DamageType> types, IDamageEffect effect) {
        onHitEffects.add(effect);
    }

    @Override
    public void addOnDamagedEffect(Collection<DamageType> types, IDamageEffect effect) {
        onDamagedEffects.add(effect);
    }

    private Damage damageModifier(Damage damage, IDamageable damageable) {
        onHitEffects.forEach(effect -> effect.applyEffect(view, owner, damageable, damage));
        return new Damage(damage.type(), damage.value() + bonusDamage.get(damage.type()));
    }
}
