package com.codenjoy.dojo.snake.client;

public class LeePoint {
    public final int x;
    public final int y;

    public LeePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static LeePoint of(int x, int y) {
        return new LeePoint(x, y);
    }

    public LeePoint move(LeePoint delta) {
        return LeePoint.of(
                x + delta.x,
                y + delta.y
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LeePoint lPoint = (LeePoint) o;

        if (x != lPoint.x) return false;
        return y == lPoint.y;
    }

    @Override
    public int hashCode() { 
        return x << 16 + y; 
    }

    @Override
    public String toString() {
        return String.format("[%2d:%2d]", x, y);
    }

    public static void main(String[] args) {
        LeePoint p1 = new LeePoint(1, 2);
        LeePoint p2 = LeePoint.of(1, 2);
    }

}
