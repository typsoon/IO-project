package game.engine.modules;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import game.engine.entities.geometry.EntityGeometryConfig;
import game.utility.Point2F;
import game.utility.Vector2F;
import utils.IDisposable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class GeometryModule implements IGeometryModule, IGeometryFactory, IDisposable {

    private static final Logger LOGGER = Logger.getLogger(GeometryModule.class.getName());
    private final World world = new World(new Vector2(0, 0), true);
    private final float timeStep;
    private final int velocityIterations;
    private final int positionIterations;

    //if map performance is an issue we can use setUserData (using Object)
    private final Map<Body, IManagingGeometryRepresentation> geometryRepresentationMap = new HashMap<>();
    private final Collection<ICollisionSubscriber> collisionSubscribers = new ArrayList<>();

    {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                for (ICollisionSubscriber subscriber : collisionSubscribers) {
                    subscriber.onCollisionBegin(
                            geometryRepresentationMap.get(contact.getFixtureA().getBody()),
                            geometryRepresentationMap.get(contact.getFixtureB().getBody())
                    );
                }
            }

            @Override
            public void endContact(Contact contact) {
                for (ICollisionSubscriber subscriber : collisionSubscribers) {
                    subscriber.onCollisionEnd(
                            geometryRepresentationMap.get(contact.getFixtureA().getBody()),
                            geometryRepresentationMap.get(contact.getFixtureB().getBody())
                    );
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }

    public GeometryModule(float timeStep, int velocityIterations, int positionIterations) {
        this.timeStep = timeStep;
        this.velocityIterations = velocityIterations;
        this.positionIterations = positionIterations;
    }

    public GeometryModule(float timeStep) {
        this(timeStep, 6, 2);
    }

    public GeometryModule() {
        this(1 / 64f);
    }

    @Override
    public IManagingGeometryRepresentation createGeometryRepresentation(EntityGeometryConfig config, float startingX, float startingY, boolean isSensor) {
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
        shape.setAsBox(config.width() / 2, config.height() / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = config.density();
        fixtureDef.friction = config.friction();
        fixtureDef.restitution = config.restitution();
        fixtureDef.isSensor = isSensor;
        body.createFixture(fixtureDef);
        shape.dispose();
        IManagingGeometryRepresentation geometryRepresentation = new IManagingGeometryRepresentation() {
            private boolean disposed = false;

            private void checkDisposed(String methodName) {
                if (disposed) {
                    LOGGER.info("INFO: called " + methodName + " on disposed geometry.");
                }
            }

            @Override
            public Point2F getPosition() {
                if (disposed) {
                    checkDisposed("getPosition");
                    return new Point2F(0, 0);
                }
                return new Point2F(body.getPosition().x, body.getPosition().y);
            }

            @Override
            public Vector2F getVelocity() {
                if (disposed) {
                    checkDisposed("getVelocity");
                    return new Vector2F(0, 0);
                }
                Vector2 velocity = body.getLinearVelocity();
                return new Vector2F(velocity.x, velocity.y);
            }

            @Override
            public float getRotation() {
                if (disposed) {
                    checkDisposed("getRotation");
                    return 0;
                }
                return body.getAngle();
            }

            @Override
            public void move(float dx, float dy) {
                if (disposed) {
                    checkDisposed("move");
                    return;
                }
//                body.applyLinearImpulse(dx,dy, body.getWorldCenter().x, body.getWorldCenter().y, true);
                body.setLinearVelocity(dx, dy);
            }

            @Override
            public void setPosition(float x, float y) {
                if (disposed) {
                    checkDisposed("setPosition");
                    return;
                }
                body.setTransform(x, y, body.getAngle());
            }

            @Override
            public void setVelocity(float vx, float vy) {
                if (disposed) {
                    checkDisposed("setVelocity");
                    return;
                }
                body.setLinearVelocity(vx, vy);
            }

            @Override
            public void setRotation(float angle) {
                if (disposed) {
                    checkDisposed("setRotation");
                    return;
                }
                body.setTransform(body.getPosition(), angle);
            }

            @Override
            public void dispose() {
                if (!disposed) {
                    disposed = true;
                    world.destroyBody(body);
                    geometryRepresentationMap.remove(body);
                }
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
                    if (geometryRepresentationMap.get(fixture.getBody()) != null)
                        entitiesInArea.add(geometryRepresentationMap.get(fixture.getBody()));
                    return true;
                },
                x, y, x + width, y + height);
//        debuging purposes
//        BodyDef bodyDef = new BodyDef();
//        bodyDef.type = BodyDef.BodyType.StaticBody;
//        bodyDef.position.set(x + width / 2f, y + height / 2f); // center of AABB
//
//        Body debugBody = world.createBody(bodyDef);
//
//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox(width / 2f, height / 2f); // half-width/half-height
//
//        FixtureDef fixtureDef = new FixtureDef();
//        fixtureDef.shape = shape;
//        fixtureDef.isSensor = true; // nie wpływa na kolizje
//        fixtureDef.density = 0;
//
//        debugBody.createFixture(fixtureDef);
//        shape.dispose();


        return entitiesInArea;
    }

    @Override
    public void cycle() {
        world.step(timeStep, velocityIterations, positionIterations);
    }

    @Override
    public void dispose() {
        world.dispose();
    }

    @Override
    public void subscribeToCollisions(ICollisionSubscriber subscriber) {
        collisionSubscribers.add(subscriber);
    }

    @Override
    public void unsubscribeFromCollisions(ICollisionSubscriber subscriber) {
        collisionSubscribers.remove(subscriber);
    }

    //for debug purposes
    public World getWorld() {
        return world;
    }
}
