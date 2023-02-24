package main.Object;
import main.GameMath.DoubleVec.Vec3d;

public class Triangle {
    private Vec3d[] vertexs = new Vec3d [3];

    public Triangle(Vec3d v1, Vec3d v2, Vec3d v3){
        this.vertexs[0] = v1;
        this.vertexs[1] = v2;
        this.vertexs[2] = v3;
    }

    public Vec3d[] getVertexs(){ return this.vertexs; }
}
