package game.engine.components;

public record EntityGeometryConfig(
        float startingX,
        float startingY,
        float width,
        float height,
        BodyType bodyType,
        float friction,
        float restitution,
        float density,
        float linearDamping,
        float angularDamping
) {}
