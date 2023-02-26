package main;

import main.GameMath.DoubleVec.Vec2d;
import main.GameMath.DoubleVec.Vec3d;
import main.Object.Triangle;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL21.*;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWKeyCallback;

public class Projection {
    private double fov;

    private Double xAngle = 0.0;
    private Double yAngle = 0.0;

    private Vec3d camera;

    private GLFWKeyCallback keyCallback;
    private boolean canVisibleCursor = false;

    public Projection(int angle, long window){
        this.fov = 1/ Math.tan(angle * 0.5 * Math.PI /180);
        this.camera = new Vec3d(0, 1.7, 0);
        setKeyCallback();
        glfwSetKeyCallback(window, this.keyCallback);
    }

    public void input(long window){
        if(glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS){
            this.camera.add(cameraRotationY(new Vec3d(0, 0, 0.1)));
        }
        if(glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS){
            this.camera.add(cameraRotationY(new Vec3d(-0.1, 0, 0)));
        }
        if(glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS){
            this.camera.add(cameraRotationY(new Vec3d(0, 0, -0.1)));
        }
        if(glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS){
            this.camera.add(cameraRotationY(new Vec3d(0.1, 0, 0)));
        }
        if(glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS){
            this.camera.add(cameraRotationY(new Vec3d(0, 0.1, 0)));
        }
        if(glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS){
            this.camera.add(cameraRotationY(new Vec3d(0, -0.1, 0)));
        }
    }

    public void update(long window){
        if(!this.canVisibleCursor){
            DoubleBuffer xAngleBuffer = BufferUtils.createDoubleBuffer(1);
            DoubleBuffer yAngleBuffer = BufferUtils.createDoubleBuffer(1);
            glfwGetCursorPos(window, xAngleBuffer, yAngleBuffer);
            this.xAngle -= xAngleBuffer.get(0) * 0.01;
            this.yAngle -= yAngleBuffer.get(0) * 0.01;
            glfwSetCursorPos(window, 0, 0);
        }     
    }

    public void render(Triangle obj){
        glPointSize(10);
        glColor3d(1,1,1);
        glBegin(GL_TRIANGLES);
        obj = rotationY(obj); 
        obj = rotationX(obj);
        for(Vec3d v : obj.getVertexs()){
            if(!isVisible(obj)) break;
            Vec2d tmp = transform(v);
            glVertex2d(tmp.getX(), tmp.getY());
        }
        glEnd();
    }

    public Triangle rotationY(Triangle target){
        Vec3d[] oldVertexs = target.getVertexs();
        Vec3d[] newVertexs = new Vec3d[3];
        for(int i = 0 ; i < oldVertexs.length ; ++i){
            double rad = this.xAngle * Math.PI / 180;
            double x = oldVertexs[i].getX() - this.camera.getX();
            double z = oldVertexs[i].getZ() - this.camera.getZ();
            double newX = x * Math.cos(rad) + z * Math.sin(rad);
            double newZ = -x * Math.sin(rad) + z * Math.cos(rad);
            newVertexs[i] = new Vec3d(newX + this.camera.getX(), oldVertexs[i].getY(), newZ + this.camera.getZ());
        }
        
        return new Triangle(newVertexs[0], newVertexs[1] , newVertexs[2]);
    }

    public Vec3d cameraRotationY(Vec3d target){
        double rad = -this.xAngle * Math.PI / 180;
        double x = target.getX();
        double z = target.getZ();
        double newX = x * Math.cos(rad) + z * Math.sin(rad);
        double newZ = -x * Math.sin(rad) + z * Math.cos(rad);
        return new Vec3d(newX, target.getY(), newZ);
    }

    public Triangle rotationX(Triangle target){
        Vec3d[] oldVertexs = target.getVertexs();
        Vec3d[] newVertexs = new Vec3d[3];
        for(int i = 0 ; i < oldVertexs.length ; ++i){
            double rad = this.yAngle * Math.PI / 180;
            double y = oldVertexs[i].getY() - this.camera.getY();
            double z = oldVertexs[i].getZ() - this.camera.getZ();
            double newY = y * Math.cos(rad) - z * Math.sin(rad);
            double newZ = y * Math.sin(rad) + z * Math.cos(rad);
            newVertexs[i] = new Vec3d(oldVertexs[i].getX(), newY + this.camera.getY(), newZ + this.camera.getZ());
        }
        
        return new Triangle(newVertexs[0], newVertexs[1] , newVertexs[2]);
    }

    public Vec2d transform(Vec3d target){
        double x = target.getX() - this.camera.getX();
        double y = target.getY() - this.camera.getY();
        double z = target.getZ() - this.camera.getZ();
        double calcZ = z >= Double.MIN_VALUE ? 1/z : -Integer.MAX_VALUE * z;
        
        return new Vec2d(x * calcZ * this.fov, y * calcZ * this.fov);
    }

    public void keyCallbackDispose(){
        this.keyCallback.free();
    }

    private boolean isVisible(Triangle obj){
        for(Vec3d v : obj.getVertexs()){
            if((v.getZ() - this.camera.getZ()) > 0) return true;
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
