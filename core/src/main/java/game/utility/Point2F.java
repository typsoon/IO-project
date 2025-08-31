package game.utility;

public record Point2F(
        float x,
        float y) {
    public static final int BYTES = 2 * Float.BYTES;

    public Point2F add(Point2F other) {
        return new Point2F(this.x + other.x, this.y + other.y);
    }

    public Point2F subtract(Point2F other) {
        return new Point2F(this.x - other.x, this.y - other.y);
    }

    public Point2F add(Vector2F vector) {
        return new Point2F(this.x + vector.x(), this.y + vector.y());
    }

    public Point2F subtract(Vector2F vector) {
        return new Point2F(this.x - vector.x(), this.y - vector.y());
    }

    public Point2F rotation(float angle, Point2F center) {
        float sin = (float) Math.sin(angle);
        float cos = (float) Math.cos(angle);

        float translatedX = this.x - center.x;
        float translatedY = this.y - center.y;

        float rotatedX = translatedX * cos - translatedY * sin;
        float rotatedY = translatedX * sin + translatedY * cos;

        return new Point2F(rotatedX + center.x, rotatedY + center.y);
    }

    public static float distance(Point2F a, Point2F b) {
        return (float) Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    public Vector2F multiply(float scalar) {
        return new Vector2F(this.x * scalar, this.y * scalar);
    }
}
