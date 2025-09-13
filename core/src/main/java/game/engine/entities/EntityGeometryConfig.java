package game.engine.entities;

public record EntityGeometryConfig(
        float width,
        float height,
        BodyType bodyType,
        boolean isRotatable,
        float friction,
        float restitution,
        float density,
        float linearDamping,
        float angularDamping
) {
}
