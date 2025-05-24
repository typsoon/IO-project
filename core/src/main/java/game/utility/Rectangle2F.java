package game.utility;

public record Rectangle2F(
        Point2F begin,
        Point2F end
){
    public Rectangle2F(float x1, float y1, float x2, float y2) {
        this(new Point2F(x1, y1), new Point2F(x2, y2));
    }
}
