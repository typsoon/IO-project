package game.utility;

public record Vector2F(
        float x,
        float y) {

    public static final int BYTES = 2 * Float.BYTES;

    public Vector2F(final Point2F point) {
        this(point.x(), point.y());
    }

    public final Vector2F add(final Vector2F other) {
        return new Vector2F(this.x + other.x, this.y + other.y);
    }

    public final Vector2F subtract(final Vector2F other) {
        return new Vector2F(this.x - other.x, this.y - other.y);
    }

    public final Vector2F multiply(final float scalar) {
        return new Vector2F(this.x * scalar, this.y * scalar);
    }

    public final float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public final float dot(final Vector2F other) {
        return this.x * other.x + this.y * other.y;
    }

    public final Vector2F normalize() {
        final float len = length();
        if (len == 0)
            return new Vector2F(0, 0);
        return new Vector2F(x / len, y / len);
    }
}
