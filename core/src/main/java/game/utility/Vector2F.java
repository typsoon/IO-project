package game.utility;

public record Vector2F(
        float x,
        float y) {

    public static final int BYTES = 2 * Float.BYTES;

    public Vector2F(final Point2F point) {
        this(point.x(), point.y());
    }

    public Vector2F add(final Vector2F other) {
        return new Vector2F(this.x + other.x, this.y + other.y);
    }

    public Vector2F subtract(final Vector2F other) {
        return new Vector2F(this.x - other.x, this.y - other.y);
    }

    public Vector2F multiply(final float scalar) {
        return new Vector2F(this.x * scalar, this.y * scalar);
    }

    public float angle() {
        return (float) Math.atan2(y, x);
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public float dot(final Vector2F other) {
        return this.x * other.x + this.y * other.y;
    }

    public Vector2F normalize() {
        final float len = length();
        if (len == 0f)
            return new Vector2F(0, 0);
        return new Vector2F(x / len, y / len);
    }

    public Vector2F rotate(float angleRadians) {
        float cos = (float) Math.cos(angleRadians);
        float sin = (float) Math.sin(angleRadians);
        return new Vector2F(
                x * cos - y * sin,
                x * sin + y * cos
        );
    }
}
