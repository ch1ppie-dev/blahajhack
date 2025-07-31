package me.eternadox.client.api.util.world;

import me.eternadox.client.api.util.Methods;

import java.text.DecimalFormat;

public class MoveUtil implements Methods {

    public static String getBlocksPerSecondString(){
        return new DecimalFormat("0.00").format(Math.hypot(mc.thePlayer.posX - mc.thePlayer.lastTickPosX, mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * 20);
    }

    public static double getBlocksPerSecond(){
        return Double.parseDouble(getBlocksPerSecondString());
    }

    public static float getPlayerDirection(){
        float yaw = mc.thePlayer.rotationYaw;
        float strafe = 45;
        if (mc.thePlayer.moveForward < 0){
            strafe = -45;
            yaw += 180;
        }
        if (mc.thePlayer.moveStrafing > 0){
            yaw -= strafe;
            if (mc.thePlayer.moveForward == 0){
                yaw -= 45;
            }
        } else if (mc.thePlayer.moveStrafing < 0) {
            yaw += strafe;
            if (mc.thePlayer.moveForward == 0){
                yaw += 45;
            }
        }
        return yaw;
    }

    public static boolean isPlayerMoving() {
        return mc.thePlayer.moveStrafing != 0 || mc.thePlayer.moveForward != 0;
    }

    public static void setSpeed(double speed){
        strafe(speed);
    }


    public static void strafe(){
        double currentPlayerSpeed = Math.hypot(mc.thePlayer.motionX, mc.thePlayer.motionZ);
        strafe(currentPlayerSpeed);
    }

    public static void strafe(double speed){
        if (isPlayerMoving()) {
            double yaw = Math.toRadians(getPlayerDirection());
            mc.thePlayer.motionZ = Math.cos(yaw) * speed;
            mc.thePlayer.motionX = -Math.sin(yaw) * speed;
        }
    }

}
