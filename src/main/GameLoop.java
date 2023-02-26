package main;
import java.nio.IntBuffer;

import main.GameMath.DoubleVec.Vec3d;
import main.Object.Camera;
import main.Object.GameObject;
import main.Object.Triangle;

import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.glfw.GLFW.*;


public class GameLoop {
    private GameTimer timer = new GameTimer();
    private double delta;
    private double accumulater = 0;
    private double interval = 1.0 / 60.0;
    private double alpha;

    private long window;
    private IntBuffer widthBuffer;
    private IntBuffer heightBuffer;

    private Camera mainCamera;
    private GameObject gameObject[] = new Triangle[11];

    public GameLoop(long window, IntBuffer widthBuffer, IntBuffer heightBuffer){
        this.window = window;
        this.widthBuffer = widthBuffer;
        this.heightBuffer = heightBuffer;
        this.mainCamera = new Camera(60, this.window);
        for(int i = 0 ; i < 11 ; ++i){
            this.gameObject[i] = new Triangle(
                new Vec3d(i * 11 - 5, -1, 10),
                new Vec3d(i * 11 + 5, -1, 10),
                new Vec3d(i * 11 , -1, 20),
                this.mainCamera);
        }  
    }

    public void gameLoop(){
        glfwSetInputMode(this.window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        while(!glfwWindowShouldClose(this.window)){
            this.delta = this.timer.getDelta();
            this.accumulater += this.delta;
            input();
            while(this.accumulater >= this.interval){
                update();
                this.accumulater -= interval;
            }
            this.alpha = this.accumulater / interval;
            render();
        }
        this.mainCamera.keyCallbackDispose();
    }

    private void input(){
        this.mainCamera.input(this.window);
        for(GameObject t : this.gameObject){
            t.input();
        }

    }

    private void render(){
        this.timer.updateFps();
        glfwGetFramebufferSize(this.window, this.widthBuffer, this.heightBuffer);

        this.widthBuffer.rewind();
        this.heightBuffer.rewind();

        glViewport(0, 0, this.widthBuffer.get(), this.heightBuffer.get());
        glClear(GL_COLOR_BUFFER_BIT);


        this.mainCamera.render();
        for(GameObject t : this.gameObject){
            t.render();
        }

        glfwSwapBuffers(this.window);
        glfwPollEvents();

        this.widthBuffer.flip();
        this.heightBuffer.flip();
    }

    private void update(){
        this.timer.update();
        this.mainCamera.update(this.window);
        for(GameObject t : this.gameObject){
            t.update();
        }
    }
}
