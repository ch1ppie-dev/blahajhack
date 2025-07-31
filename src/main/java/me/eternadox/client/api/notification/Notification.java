package me.eternadox.client.api.notification;

import me.eternadox.client.api.util.misc.TimerUtil;

public class Notification {
    private Type type;
    private final TimerUtil timer;
    private String header, body;
    private long duration;

    public Notification(Type type, String header, String body, long duration){
        this.type = type;
        this.header = header;
        this.body = body;
        this.duration = duration;
        this.timer = new TimerUtil();
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public TimerUtil getTimer() {
        return timer;
    }
    public boolean hasNotificationEnded(){
        return timer.hasTimeElapsed(this.getDuration());
    }
}
