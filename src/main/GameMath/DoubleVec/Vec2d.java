package main.GameMath.DoubleVec;

public class Vec2d {
    private double x;
    private double y;

    public Vec2d(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void add(Vec2d other){
        this.x += other.x;
        this.y += other.y;
    }

    public void sub(Vec2d other){
        this.x -= other.x;
        this.y -= other.y;
    }

    public void mul(double num){
        this.x *= num;
        this.y *= num;
    }

    public void div(double num){
        this.x /= num;
        this.y /= num;
    }

    public double dot(Vec2d other){
        return this.x * other.x + this.y * other.y;
    }

    public Vec3d cross(Vec2d other){
        return new Vec3d(0, 0, this.x * other.y - other.x * this.y);
    }

    public double getX(){ return this.x; }
    public double getY(){ return this.y; }
}
