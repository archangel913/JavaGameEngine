package main;
import java.nio.IntBuffer;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class MainWindow {
    private GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);
    private String title = "game";
    private int widthSize = 600;
    private int heightSize = 400;
    private long window;
    private IntBuffer widthBuffer = MemoryUtil.memAllocInt(1);
    private IntBuffer heightBuffer = MemoryUtil.memAllocInt(1);
    
    private GameLoop gameLoop;

    private boolean init(){
        try{
            glfwSetErrorCallback(this.errorCallback);
            if(!glfwInit()){
                throw new IllegalStateException("Unable to initialize GLFW");
            }

            glfwDefaultWindowHints();
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 2);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
            this.window = glfwCreateWindow(this.widthSize, this.heightSize, this.title, NULL, NULL);
            if(this.window == NULL){
                glfwTerminate();
                throw new RuntimeException("Faild to create the GLFW window");
            }

            glfwMakeContextCurrent(this.window);
            GL.createCapabilities();

            glfwSwapInterval(1);
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void loop(){
        this.gameLoop = new GameLoop(this.window, this.widthBuffer, this.heightBuffer);
        this.gameLoop.gameLoop();
    }

    private void dispose(){
        MemoryUtil.memFree(this.widthBuffer);
        MemoryUtil.memFree(this.heightBuffer);

        glfwDestroyWindow(this.window);

        glfwTerminate();
        this.errorCallback.free();
    }

    public static void main(String[] args) {
        MainWindow main = new MainWindow();
        if(!main.init()){
            System.exit(-1);
        }
        main.loop();
        main.dispose();
    }
}