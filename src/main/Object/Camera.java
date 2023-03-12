package main.Object;



import main.GameMath.DoubleVec.*;
import java.nio.DoubleBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWKeyCallback;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL21.*;

public class Camera {
    private double fov;

    private Double xAngle = 0.0;
    private Double yAngle = 0.0;

    private Vec3d position;
    //private Vec2d angle;

    private double xRad;
    private double yRad;

    private double sinX;
    private double cosX;
    private double sinY;
    private double cosY;

    private double sinInX;
    private double cosInX;
    private double sinInY;
    private double cosInY;

    private double near = 0.001;
    

    private GLFWKeyCallback keyCallback;
    private boolean canVisibleCursor = false;

    public Camera(int angle, long window){
        this.fov = 1/ Math.tan(angle * 0.5 * Math.PI /180);
        this.position = new Vec3d(0, 1.7, 0);
        setKeyCallback();
        glfwSetKeyCallback(window, this.keyCallback);
    }

    public Vec3d getPosition(){
        return this.position;
    }

    public void input(long window){
        if(glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS){
            this.position = this.position.add(cameraRotation(new Vec3d(0, 0, 0.1)));
        }
        if(glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS){
            this.position = this.position.add(cameraRotation(new Vec3d(-0.1, 0, 0)));
        }
        if(glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS){
            this.position = this.position.add(cameraRotation(new Vec3d(0, 0, -0.1)));
        }
        if(glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS){
            this.position = this.position.add(cameraRotation(new Vec3d(0.1, 0, 0)));
        }
        if(glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS){
            this.position = this.position.add(cameraRotation(new Vec3d(0, 0.1, 0)));
        }
        if(glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS){
            this.position = this.position.add(cameraRotation(new Vec3d(0, -0.1, 0)));
        }
    }

    public void update(long window){
        if(!this.canVisibleCursor){
            DoubleBuffer xAngleBuffer = BufferUtils.createDoubleBuffer(1);
            DoubleBuffer yAngleBuffer = BufferUtils.createDoubleBuffer(1);
            glfwGetCursorPos(window, xAngleBuffer, yAngleBuffer);
            this.yAngle -= xAngleBuffer.get(0) * 0.01;
            this.xAngle -= yAngleBuffer.get(0) * 0.01;
            glfwSetCursorPos(window, 0, 0);
            this.xRad = this.xAngle * Math.PI / 180;
            this.yRad = this.yAngle * Math.PI / 180;
            this.sinY = Math.sin(yRad);
            this.cosY = Math.cos(yRad);
            this.sinX = Math.sin(xRad);
            this.cosX = Math.cos(xRad);
            this.sinInY = Math.sin(-yRad);
            this.cosInY = Math.cos(-yRad);
            this.sinInX = Math.sin(-xRad);
            this.cosInX = Math.cos(-xRad);
        }     
    }

    public void render(){
    }

    public Vec3d[] ratation(Vec3d[] oldVertexs){
        Vec3d[] newVertexs = new Vec3d[oldVertexs.length];
        for(int i = 0 ; i < oldVertexs.length ; ++i){
            double x = oldVertexs[i].getX() - this.position.getX();
            double y = oldVertexs[i].getY() - this.position.getY();
            double z = oldVertexs[i].getZ() - this.position.getZ();
            Vec3d rotationX = new Vec3d(x * this.cosY + z * this.sinY, y, -x * this.sinY + z * this.cosY);
            x = rotationX.getX();
            y = rotationX.getY();
            z = rotationX.getZ();
            Vec3d culclated = new Vec3d(x, y * this.cosX - z * this.sinX, y * this.sinX + z * this.cosX);
            newVertexs[i] = culclated.add(this.position);
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

    public void clipping(Vec3d[] targets){
        ArrayList<Vec3d> invisible = new ArrayList<>();
        ArrayList<Vec3d> visible = new ArrayList<>();
        for(Vec3d v : targets){
            if(this.isVisible(v)){
                visible.add(v);
            } 
            else{
                invisible.add(v);
            }
        }
        ArrayList<Vec3d> result = new ArrayList<>();
        if(invisible.size() == 0){
            for(int i = 0 ; i < targets.length ; ++i){
                result.add(targets[i]);
            }
        }
        else if(invisible.size() == 1){
            result.add(visible.get(0));
            Vec3d cantSeeVec = invisible.get(0);
            double csvX = cantSeeVec.getX() - this.position.getX();
            double csvY = cantSeeVec.getY() - this.position.getY();
            double csvZ = cantSeeVec.getZ() - this.position.getZ();
            for(Vec3d v : visible){
                double x = v.getX() - this.position.getX();
                double y = v.getY() - this.position.getY();
                double z = v.getZ() - this.position.getZ();
                double newX = (x - csvX) / (z - csvZ) * (-csvZ + this.near) + csvX;
                double newY = (y - csvY) / (z - csvZ) * (-csvZ + this.near) + csvY;
                result.add(new Vec3d(newX + this.position.getX(), newY + this.position.getY(), this.position.getZ() + this.near));
            }
            result.add(visible.get(1));
        }
        else if(invisible.size() == 2){
            result.add(visible.get(0));
            Vec3d canSeeVec = visible.get(0);
            double csvX = canSeeVec.getX() - this.position.getX();
            double csvY = canSeeVec.getY() - this.position.getY();
            double csvZ = canSeeVec.getZ() - this.position.getZ();
            for(Vec3d v : invisible){
                double x = v.getX() - this.position.getX();
                double y = v.getY() - this.position.getY();
                double z = v.getZ() - this.position.getZ();
                double newX = (x - csvX) / (z - csvZ) * (-z + this.near) + x;
                double newY = (y - csvY) / (z - csvZ) * (-z + this.near) + y;
                result.add(new Vec3d(newX + this.position.getX(), newY + this.position.getY(), this.position.getZ() + this.near));
            }
        }
        else{}
        glBegin(GL_TRIANGLE_FAN);
        for(Vec3d v : result){
            Vec2d tmp = this.transform(v);
            glVertex2d(tmp.getX(), tmp.getY());
        }
        glEnd();
    }

    public Vec3d cameraRotation(Vec3d front){
        //上下回転の時に対応させる
        double x = front.getX();
        double y = front.getY();
        double z = front.getZ();
        double newX = x * this.cosInY + z * this.sinInY;
        double newZ = -x * this.sinInY + z * this.cosInY;
        return new Vec3d(newX , y, newZ);
    }

    public void keyCallbackDispose(){
        this.keyCallback.free();
    }

    public boolean isVisible(GameObject obj){
        for(Vec3d v : obj.getVertexs()){
            if((v.getZ() - this.position.getZ() - this.near) > 0) return true;
        }
        return false;
    }

    public boolean isVisible(Vec3d obj){
        if((obj.getZ() - this.position.getZ() - this.near) > 0) return true;
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
