package game.utility;

public record Point2F(
        float x,
        float y
) {
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
}
