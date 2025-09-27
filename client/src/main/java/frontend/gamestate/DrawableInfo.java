package frontend.gamestate;

import game.engine.entities.EntityGroupID;

// TODO: Consider whether using the Enum's ordinal instead of the Enum itself would be better.
public class DrawableInfo {
    private EntityGroupID entityGroupID;
    private EntityVisibleState state;
    private float stateTime;
    // TODO: Modifiers modifiers,
    private float x;
    private float y;
    private float width;
    private float height;
    private float originX;
    private float originY;
    private float scaleX;
    private float scaleY;
    private float rotation;

    public DrawableInfo(EntityGroupID entityGroupID, EntityVisibleState state) {
        this.entityGroupID = entityGroupID;
        this.state = state;
        stateTime = 0;
        x = 0;
        y = 0;
        width = 0.5f;
        height = 0.5f;
        originX = 0.25f;
        originY = 0.25f;// TODO check what should be here
        scaleX = 1;
        scaleY = 1;
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
        this.originY = height / 2;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.originX = width / 2;
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

    @Override
    public String toString() {
        return "DrawableInfo [entityGroupID=" + entityGroupID + ", state=" + state + ", stateTime=" + stateTime + ", x="
                + x + ", y=" + y + ", width=" + width + ", height=" + height + ", originX=" + originX + ", originY="
                + originY + ", scaleX=" + scaleX + ", scaleY=" + scaleY + ", rotation=" + rotation + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((entityGroupID == null) ? 0 : entityGroupID.hashCode());
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        result = prime * result + Float.floatToIntBits(stateTime);
        result = prime * result + Float.floatToIntBits(x);
        result = prime * result + Float.floatToIntBits(y);
        result = prime * result + Float.floatToIntBits(width);
        result = prime * result + Float.floatToIntBits(height);
        result = prime * result + Float.floatToIntBits(originX);
        result = prime * result + Float.floatToIntBits(originY);
        result = prime * result + Float.floatToIntBits(scaleX);
        result = prime * result + Float.floatToIntBits(scaleY);
        result = prime * result + Float.floatToIntBits(rotation);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DrawableInfo other = (DrawableInfo) obj;
        if (entityGroupID != other.entityGroupID)
            return false;
        if (state != other.state)
            return false;
        if (Float.floatToIntBits(stateTime) != Float.floatToIntBits(other.stateTime))
            return false;
        if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
            return false;
        if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
            return false;
        if (Float.floatToIntBits(width) != Float.floatToIntBits(other.width))
            return false;
        if (Float.floatToIntBits(height) != Float.floatToIntBits(other.height))
            return false;
        if (Float.floatToIntBits(originX) != Float.floatToIntBits(other.originX))
            return false;
        if (Float.floatToIntBits(originY) != Float.floatToIntBits(other.originY))
            return false;
        if (Float.floatToIntBits(scaleX) != Float.floatToIntBits(other.scaleX))
            return false;
        if (Float.floatToIntBits(scaleY) != Float.floatToIntBits(other.scaleY))
            return false;
        if (Float.floatToIntBits(rotation) != Float.floatToIntBits(other.rotation))
            return false;
        return true;
    }
}
