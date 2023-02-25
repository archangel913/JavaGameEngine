package main.Object;



import main.GameMath.DoubleVec.*;
import java.nio.DoubleBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    private double fov;

    private Double xAngle = 0.0;
    private Double yAngle = 0.0;

    private Vec3d position;
    //private Vec2d angle;

    private GLFWKeyCallback keyCallback;
    private boolean canVisibleCursor = false;

    public Camera(int angle, long window){
        this.fov = 1/ Math.tan(angle * 0.5 * Math.PI /180);
        this.position = new Vec3d(0, 1.7, 0);
        setKeyCallback();
        glfwSetKeyCallback(window, this.keyCallback);
    }

    public void input(long window){
        if(glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS){
            this.position.add(cameraRotationY(new Vec3d(0, 0, 0.1)));
        }
        if(glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS){
            this.position.add(cameraRotationY(new Vec3d(-0.1, 0, 0)));
        }
        if(glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS){
            this.position.add(cameraRotationY(new Vec3d(0, 0, -0.1)));
        }
        if(glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS){
            this.position.add(cameraRotationY(new Vec3d(0.1, 0, 0)));
        }
        if(glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS){
            this.position.add(cameraRotationY(new Vec3d(0, 0.1, 0)));
        }
        if(glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS){
            this.position.add(cameraRotationY(new Vec3d(0, -0.1, 0)));
        }
    }

    public void update(long window){
        if(!this.canVisibleCursor){
            DoubleBuffer xAngleBuffer = BufferUtils.createDoubleBuffer(1);
            DoubleBuffer yAngleBuffer = BufferUtils.createDoubleBuffer(1);
            glfwGetCursorPos(window, xAngleBuffer, yAngleBuffer);
            this.xAngle += xAngleBuffer.get(0) * 0.01;
            this.yAngle -= yAngleBuffer.get(0) * 0.01;
            glfwSetCursorPos(window, 0, 0);
        }     
    }

    public void render(){
    }

    public Vec3d[] rotationY(Vec3d[] oldVertexs) {
        Vec3d[] newVertexs = new Vec3d[oldVertexs.length];
        for(int i = 0 ; i < oldVertexs.length ; ++i){
            double rad = this.xAngle * Math.PI / 180;
            double x = oldVertexs[i].getX() - this.position.getX();
            double z = oldVertexs[i].getZ() - this.position.getZ();
            double newX = x * Math.cos(rad) + z * Math.sin(rad);
            double newZ = -x * Math.sin(rad) + z * Math.cos(rad);
            newVertexs[i] = new Vec3d(newX + this.position.getX(), oldVertexs[i].getY(), newZ + this.position.getZ());
        }
        return newVertexs;
    }

    public Vec3d[] rotationX(Vec3d[] oldVertexs){
        Vec3d[] newVertexs = new Vec3d[oldVertexs.length];
        for(int i = 0 ; i < oldVertexs.length ; ++i){
            double rad = -this.yAngle * Math.PI / 180;
            double y = oldVertexs[i].getY() - this.position.getY();
            double z = oldVertexs[i].getZ() - this.position.getZ();
            double newY = y * Math.cos(rad) - z * Math.sin(rad);
            double newZ = y * Math.sin(rad) + z * Math.cos(rad);
            newVertexs[i] = new Vec3d(oldVertexs[i].getX(), newY + this.position.getY(), newZ + this.position.getZ());
        }
        
        return newVertexs;
    }

    public Vec2d transform(Vec3d target){
        double x = target.getX() - this.position.getX();
        double y = target.getY() - this.position.getY();
        double z = target.getZ() - this.position.getZ();
        double calcZ = z >= Double.MIN_VALUE ? 1/z : -Integer.MAX_VALUE * z;
        
        return new Vec2d(x * calcZ * this.fov, y * calcZ * this.fov);
    }

    public Vec3d cameraRotationY(Vec3d target){
        double rad = -this.xAngle * Math.PI / 180;
        double x = target.getX();
        double z = target.getZ();
        double newX = x * Math.cos(rad) + z * Math.sin(rad);
        double newZ = -x * Math.sin(rad) + z * Math.cos(rad);
        return new Vec3d(newX, target.getY(), newZ);
    }

    public void keyCallbackDispose(){
        this.keyCallback.free();
    }

    public boolean isVisible(GameObject obj){
        for(Vec3d v : obj.getVertexs()){
            if((v.getZ() - this.position.getZ()) > 0) return true;
        }
        return false;
    }

    private void setKeyCallback(){
        this.keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods){
                if(key == GLFW_KEY_ESCAPE && action == GLFW_PRESS){
                    canVisibleCursor = !canVisibleCursor;
                    if(canVisibleCursor){
                        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
                    }
                    else{
                        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
                        glfwSetCursorPos(window, 0, 0);
                    }
                }
            }
        };
    }
}
