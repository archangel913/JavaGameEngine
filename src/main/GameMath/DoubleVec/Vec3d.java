package main.GameMath.DoubleVec;

public class Vec3d {
    private double x;
    private double y;
    private double z;

    public Vec3d(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void add(Vec3d other){
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
    }

    public void sub(Vec3d other){
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
    }

    public void mul(double num){
        this.x *= num;
        this.y *= num;
        this.z *= num;
    }

    public void div(double num){
        this.x /= num;
        this.y /= num;
        this.z /= num;
    }

    public double dot(Vec3d other){
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public Vec3d cross(Vec3d other){
        return new Vec3d(this.y * other.z - other.y * this.z, this.x * other.z - other.x * this.z, this.x * other.y - other.x * this.y);
    }

    public double getX(){ return this.x; }
    public double getY(){ return this.y; }
    public double getZ(){ return this.z; }
}
