package frontend.gamestate;

import game.engine.entities.EntityGroupID;

// TODO: Consider whether using the Enum's ordinal instead of the Enum itself would be better.
public class DrawableInfo{
    EntityGroupID entityGroupID;
    EntityVisibleState state;
    float stateTime;
    // TODO: Modifiers modifiers,
    float x; float y; float width; float height;
    float originX; float originY;
    float scaleX; float scaleY;
    float rotation;

    public DrawableInfo(EntityGroupID entityGroupID, EntityVisibleState state){
        this.entityGroupID = entityGroupID;
        this.state = state;
        stateTime = 0;
        x = 0; y = 0; width = 0; height = 0;
        originX = 0; originY = 0;
        scaleX = 1; scaleY = 1;
        rotation = 0;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public float getOriginY() {
        return originY;
    }

    public void setOriginY(float originY) {
        this.originY = originY;
    }

    public float getOriginX() {
        return originX;
    }

    public void setOriginX(float originX) {
        this.originX = originX;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public EntityVisibleState getState() {
        return state;
    }

    public void setState(EntityVisibleState state) {
        this.state = state;
    }

    public EntityGroupID getEntityGroupID() {
        return entityGroupID;
    }

    public void setEntityGroupID(EntityGroupID entityGroupID) {
        this.entityGroupID = entityGroupID;
    }
}
