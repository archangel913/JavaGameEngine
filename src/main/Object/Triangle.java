package main.Object;

import main.GameMath.DoubleVec.*;

import static org.lwjgl.opengl.GL21.*;

public class Triangle extends GameObject{
    public Triangle(Vec3d v1, Vec3d v2, Vec3d v3, Camera camera){
        super(3);
        super.vertexs[0] = v1;
        super.vertexs[1] = v2;
        super.vertexs[2] = v3;
        super.camera = camera;
    }

    @Override
    public void input() {
    }

    @Override
    public void update() {
    }

    @Override
    public void render() {
        glPointSize(10);
        glColor3d(1,1,1);
        
        Vec3d[] culcVertexs = super.getVertexs();
        culcVertexs = super.camera.ratation(culcVertexs);

        super.camera.clipping(culcVertexs);
    }
}
