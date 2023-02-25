package main.Object;

import main.GameMath.DoubleVec.*;;
public abstract class GameObject {
    protected Vec3d[] vertexs;
    protected Camera camera;

    public abstract void input();
    public abstract void update();
    public abstract void render();

    public GameObject(int vertexNum){
        this.vertexs = new Vec3d [vertexNum];
    }

    public Vec3d[] getVertexs(){
        return this.vertexs;
    }
}
