package game.utility;

public record Rectangle2F(
        Point2F begin,
        Point2F end
) {
    public Rectangle2F(float x1, float y1, float x2, float y2) {
        this(new Point2F(x1, y1), new Point2F(x2, y2));
    }

    public Rectangle2F rotateBB(float angle, Point2F center) {
        Point2F b = this.begin;
        Point2F e = this.end;

        Point2F p1 = new Point2F(b.x(), b.y()).rotation(angle, center);
        Point2F p2 = new Point2F(e.x(), b.y()).rotation(angle, center);
        Point2F p3 = new Point2F(e.x(), e.y()).rotation(angle, center);
        Point2F p4 = new Point2F(b.x(), e.y()).rotation(angle, center);

        float minX = Math.min(Math.min(p1.x(), p2.x()), Math.min(p3.x(), p4.x()));
        float minY = Math.min(Math.min(p1.y(), p2.y()), Math.min(p3.y(), p4.y()));
        float maxX = Math.max(Math.max(p1.x(), p2.x()), Math.max(p3.x(), p4.x()));
        float maxY = Math.max(Math.max(p1.y(), p2.y()), Math.max(p3.y(), p4.y()));

        return new Rectangle2F(new Point2F(minX, minY), new Point2F(maxX, maxY));
    }

    public Rectangle2F rotateBB(float angle) {
        return rotateBB(angle, new Point2F((begin.x() + end.x()) / 2, (begin.y() + end.y()) / 2));
    }
}
