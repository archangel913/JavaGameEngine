package main;
public class GameTimer {
    private double lastLoopTime;
    private double elapsedTime;
    private int fpsCount;
    private int upsCount;
    private int fps;
    private int ups;

    public GameTimer(){
        this.lastLoopTime = getNowTime();
    }

    private double getNowTime(){
        return System.nanoTime() / 1000000000.0;
    }

    public double getDelta(){
        double time = getNowTime();
        double delta = time - this.lastLoopTime;
        this.lastLoopTime = time;
        this.elapsedTime += delta;
        return delta;
    }

    public void updateFps(){ this.fpsCount++; }

    public void updateUps(){ this.upsCount++; }

    public void update(){
        if(this.elapsedTime > 1f){
            this.fps = this.fpsCount;
            this.fpsCount = 0;
            this.ups = this.upsCount;
            this.upsCount = 0;
            this.elapsedTime -= 1.0;
        }
    }

    public int getFps(){ return this.fps; }

    public int getUps(){ return this.ups; }
}
