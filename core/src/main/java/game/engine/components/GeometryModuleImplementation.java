package game.engine.components;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import game.utility.Point2F;
import game.utility.Vector2F;

public class GeometryModuleImplementation implements GeometryModule, GeometryFactory, Closeable {

    private final World world = new World(new Vector2(0,0), true);
    private final float timeStep;
    private final int velocityIterations;
    private final int positionIterations;

    private final Map<Body, GeometryRepresentation> geometryRepresentationMap = new HashMap<>();

    public GeometryModuleImplementation(float timeStep, int velocityIterations, int positionIterations ) {
        this.timeStep = timeStep;
        this.velocityIterations = velocityIterations;
        this.positionIterations = positionIterations;
    }
    public GeometryModuleImplementation(float timeStep) {
        this(timeStep, 6, 2);
    }
    public GeometryModuleImplementation() {
        this(1/64f);
    }

    @Override
    public GeometryRepresentation createGeometryRepresentation(EntityGeometryConfig config) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = switch (config.bodyType()) {
            case STATIC -> BodyDef.BodyType.StaticBody;
            case DYNAMIC -> BodyDef.BodyType.DynamicBody;
            case KINEMATIC -> BodyDef.BodyType.KinematicBody;
        };
        bodyDef.position.set(config.startingX(), config.startingY());
        bodyDef.linearDamping = config.linearDamping();
        bodyDef.angularDamping = config.angularDamping();

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(config.width()/2, config.height()/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = config.density();
        fixtureDef.friction = config.friction();
        fixtureDef.restitution = config.restitution();
        body.createFixture(fixtureDef);
        shape.dispose();
        GeometryRepresentation geometryRepresentation = new GeometryRepresentation() {
            @Override
            public Point2F getPosition() {
                return new Point2F(body.getPosition().x, body.getPosition().y);
            }
            @Override
            public Vector2F getVelocity() {
                Vector2 velocity = body.getLinearVelocity();
                return new Vector2F(velocity.x, velocity.y);
            }
            @Override
            public void move(float dx, float dy) {
                body.applyLinearImpulse(dx,dy, body.getWorldCenter().x, body.getWorldCenter().y, true);
            }
        };
        geometryRepresentationMap.put(body, geometryRepresentation);
        return geometryRepresentation;
    }

    @Override
    public Collection<GeometryRepresentation> getEntitiesInArea(float x, float y, float width, float height) {
        Collection<GeometryRepresentation> entitiesInArea = new ArrayList<>();
        world.QueryAABB(
                fixture -> {
                    entitiesInArea.add(geometryRepresentationMap.get(fixture.getBody()));
                    return true;
                },
                x, y, x + width, y + height);
        return entitiesInArea;
    }

    @Override
    public void Cycle() {
        world.step(timeStep, velocityIterations, positionIterations);
    }

    @Override
    public void close() {
        world.dispose();
    }
}
