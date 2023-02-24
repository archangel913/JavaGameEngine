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

    public void add(double x, double y, double z){
        this.x += x;
        this.y += y;
        this.z += z;
    }

    public void add(Vec3d other){
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
    }

    public double getX(){ return this.x; }
    public double getY(){ return this.y; }
    public double getZ(){ return this.z; }
}
