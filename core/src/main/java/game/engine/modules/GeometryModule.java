package game.engine.modules;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import game.engine.entities.EntityGeometryConfig;
import game.utility.Point2F;
import game.utility.Vector2F;

public class GeometryModule implements IGeometryModule, IGeometryFactory, Closeable {

    private final World world = new World(new Vector2(0,0), true);
    private final float timeStep;
    private final int velocityIterations;
    private final int positionIterations;

    //if map performance is an issue we can use setUserData (using Object)
    private final Map<Body, IManagingGeometryRepresentation> geometryRepresentationMap = new HashMap<>();

    public GeometryModule(float timeStep, int velocityIterations, int positionIterations ) {
        this.timeStep = timeStep;
        this.velocityIterations = velocityIterations;
        this.positionIterations = positionIterations;
    }
    public GeometryModule(float timeStep) {
        this(timeStep, 6, 2);
    }
    public GeometryModule() {
        this(1/64f);
    }

    @Override
    public IManagingGeometryRepresentation createGeometryRepresentation(EntityGeometryConfig config, float startingX, float startingY) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = switch (config.bodyType()) {
            case STATIC -> BodyDef.BodyType.StaticBody;
            case DYNAMIC -> BodyDef.BodyType.DynamicBody;
            case KINEMATIC -> BodyDef.BodyType.KinematicBody;
        };
        bodyDef.position.set(startingX, startingY);
        bodyDef.linearDamping = config.linearDamping();
        bodyDef.angularDamping = config.angularDamping();
        bodyDef.fixedRotation = !config.isRotatable();
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
        IManagingGeometryRepresentation geometryRepresentation = new IManagingGeometryRepresentation() {
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
            public float getRotation() {
                return body.getAngle();
            }
            @Override
            public void move(float dx, float dy) {
//                body.applyLinearImpulse(dx,dy, body.getWorldCenter().x, body.getWorldCenter().y, true);
                body.setLinearVelocity(dx, dy);
            }
            @Override
            public void setPosition(float x, float y) {
                body.setTransform(x, y, body.getAngle());
            }
            @Override
            public void setVelocity(float vx, float vy) {
                body.setLinearVelocity(vx, vy);
            }
            @Override
            public void setRotation(float angle) {
                body.setTransform(body.getPosition(), angle);
            }
            @Override
            public void dispose() {
                world.destroyBody(body);
                geometryRepresentationMap.remove(body);
            }
        };
        geometryRepresentationMap.put(body, geometryRepresentation);
        return geometryRepresentation;
    }

    @Override
    public Collection<IMovingGeometryRepresentation> getEntitiesInArea(float x, float y, float width, float height) {
        Collection<IMovingGeometryRepresentation> entitiesInArea = new ArrayList<>();
        world.QueryAABB(
                fixture -> {
                    entitiesInArea.add(geometryRepresentationMap.get(fixture.getBody()));
                    return true;
                },
                x, y, x + width, y + height);
        return entitiesInArea;
    }

    @Override
    public void cycle() {
        world.step(timeStep, velocityIterations, positionIterations);
    }

    @Override
    public void close() {
        world.dispose();
    }

    //for debug purposes
    public World getWorld() {
        return world;
    }
}
