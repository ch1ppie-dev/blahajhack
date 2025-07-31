package me.eternadox.client.api.util.misc;

public class TimerUtil {
    private long lastMillis;

    public TimerUtil(){
        this.reset();
    }

    public void reset(){
        this.lastMillis = System.currentTimeMillis();
    }

    public boolean hasTimeElapsed(long time){
        return this.getTimeElapsed() >= time;
    }
    public long getTimeElapsed(){
        return System.currentTimeMillis() - lastMillis;
    }
}
