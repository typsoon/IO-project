package game.engine.entities.items.attacks;

public interface DamageModifier {
    Damage modify(Damage base, IDamageable target);
}
