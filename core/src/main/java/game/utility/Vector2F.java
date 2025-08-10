package game.utility;

public record Vector2F(
        float x,
        float y) {

    public static final int BYTES = 2 * Float.BYTES;

    public Vector2F add(Vector2F other) {
        return new Vector2F(this.x + other.x, this.y + other.y);
    }

    public Vector2F subtract(Vector2F other) {
        return new Vector2F(this.x - other.x, this.y - other.y);
    }

    public Vector2F multiply(float scalar) {
        return new Vector2F(this.x * scalar, this.y * scalar);
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public Vector2F normalize() {
        float len = length();
        if (len == 0)
            return new Vector2F(0, 0);
        return new Vector2F(x / len, y / len);
    }
}
