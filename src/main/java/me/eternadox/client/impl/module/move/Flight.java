package me.eternadox.client.impl.module.move;

import me.eternadox.bus.annotations.Subscribe;
import me.eternadox.client.api.module.Category;
import me.eternadox.client.api.module.Module;
import me.eternadox.client.api.module.annotations.Info;
import me.eternadox.client.api.util.world.ChatUtil;
import me.eternadox.client.api.util.world.MoveUtil;
import me.eternadox.client.impl.event.EventCollide;
import me.eternadox.client.impl.event.EventPreMotion;
import me.eternadox.client.impl.setting.ModeSetting;
import me.eternadox.client.impl.setting.NumberSetting;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

import java.util.function.Consumer;

@Info(displayName = "Flight", category = Category.MOVE)
public class Flight extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", "Mospixel", "Collide", "Vanilla", "Mospixel", "Mospixel 2");

    private final NumberSetting vanillaSpeed = new NumberSetting("Speed", () -> mode.getMode().equals("Vanilla"),2, 0, 5, 0.1);

    BlockPos startingPosition;

    public Flight(){
        this.addSettings(mode, vanillaSpeed);
    }

    @Override
    public void onDisable(){
        super.onDisable();
        mc.timer.timerSpeed = 1.0f;
        mc.thePlayer.jumpMovementFactor = 0.02f;

        double distance = Math.abs((startingPosition.getX() - mc.thePlayer.posX) + (startingPosition.getY() - mc.thePlayer.posY) + (startingPosition.getZ() - mc.thePlayer.posZ));
        ChatUtil.send("Distance traveled: " + distance);
    }

    @Override
    public void onEnable(){
        super.onEnable();
        startingPosition = mc.thePlayer.getPosition();
    }

    @Subscribe
    public Consumer<EventCollide> collisionEventListener = e ->{
        switch (mode.getMode()) {
            case "Mospixel":
            case "Collide":
                if (e.getBlock() instanceof BlockAir && e.getY() < mc.thePlayer.posY) {
                    e.setBoundingBox(AxisAlignedBB.fromBounds(-15, -1, -15, 15, 1, 15).offset(e.getX(), e.getY(), e.getZ()));
                }
                break;

        }
    };
    @Subscribe
    public Consumer<EventPreMotion> onPreMotion = e -> {
        if (mc.thePlayer == null) return;
        this.setSuffix(mode.getMode());

        switch (mode.getMode()) {

            case "Vanilla":
                MoveUtil.setSpeed(vanillaSpeed.getValue() / 2);

                if (mc.gameSettings.keyBindJump.isKeyDown()){
                    mc.thePlayer.motionY = vanillaSpeed.getValue();
                } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.thePlayer.motionY = -vanillaSpeed.getValue();
                } else {
                    mc.thePlayer.motionY = 0;
                }
                break;
            case "Mospixel":
                mc.thePlayer.jumpMovementFactor = 0;
                if (MoveUtil.isPlayerMoving()) {
                    if (mc.thePlayer.ticksExisted % 5 == 0)
                        mc.timer.timerSpeed = 0.8f;
                    else
                        mc.timer.timerSpeed = 1.25f;

                } else {
                    mc.timer.timerSpeed = 1f;
                }

                break;

        }

    };
}
