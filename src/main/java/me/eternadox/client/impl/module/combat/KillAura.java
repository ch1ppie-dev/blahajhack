package me.eternadox.client.impl.module.combat;

import me.eternadox.bus.annotations.Subscribe;

import java.util.Comparator;
import java.util.function.Consumer;
import me.eternadox.client.api.module.Category;
import me.eternadox.client.api.module.Module;
import me.eternadox.client.api.module.annotations.Info;
import me.eternadox.client.api.util.misc.TimerUtil;
import me.eternadox.client.impl.Client;
import me.eternadox.client.impl.event.EventPostMotion;
import me.eternadox.client.impl.event.EventTick;
import me.eternadox.client.impl.setting.ModeSetting;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import me.eternadox.client.impl.event.EventPreMotion;
import me.eternadox.client.impl.setting.BooleanSetting;
import me.eternadox.client.impl.setting.NumberSetting;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;
import java.util.stream.Collectors;

@Info(displayName = "Kill Aura", category = Category.COMBAT)
public class KillAura extends Module {
    private final TimerUtil timer = new TimerUtil();

    private final NumberSetting minCps = new NumberSetting("Min CPS", 20,1.0, 20.0, 1);
    private final NumberSetting maxCps = new NumberSetting("Max CPS", 20, 1.0, 20.0, 1);
    private final NumberSetting reach = new NumberSetting("Reach", 5, 1.0, 6.0, 0.1);

    private final BooleanSetting invisibles = new BooleanSetting("Invisibles", false);
    private final BooleanSetting players = new BooleanSetting("Players", true);
    private final BooleanSetting mobs = new BooleanSetting("Mobs", false);
    private final BooleanSetting keepSprint = new BooleanSetting("Keep Sprint", true);

    private final ModeSetting autoblock = new ModeSetting("Autoblock", "Test", "Fake", "Tick", "Packet", "Test");
    private final ModeSetting sorting = new ModeSetting("Sort", "Health", "Health", "Distance", "HurtTime", "None");
    private final ModeSetting attackEvent = new ModeSetting("Attack Event", "Post", "Pre", "Post");
    private final ModeSetting blockEvent = new ModeSetting("Block Event", "Pre", "Pre", "Post");

    private final BooleanSetting hvhCrits = new BooleanSetting("HvH Criticals", true);
    private final BooleanSetting smooth = new BooleanSetting("Smooth Block", () -> !autoblock.getMode().equals("Fake"), true);

    private final ModeSetting rotations = new ModeSetting("Rotations", "Vanilla", "None", "Vanilla");

    public boolean isBlocking;
    private boolean block;
    private EntityLivingBase entityAttacking;
    private boolean critEntity;
    private List<Entity> entities;


    public KillAura() {
        this.addSettings(minCps, maxCps, reach, invisibles, players, mobs, keepSprint, autoblock, smooth, rotations, attackEvent, blockEvent);
    }

    private double getCPS(){
        if (minCps.getValue() == maxCps.getValue()){
            return maxCps.getValue();
        }
        return mc.thePlayer.getRNG().doubles(minCps.getValue(), maxCps.getValue()).findFirst().getAsDouble();
    }

    @Subscribe
    public Consumer<EventPreMotion> onPreMotion = e -> {
        if (mc.thePlayer == null) return;

        entities = mc.theWorld.loadedEntityList.stream().filter(this::checkEntity).collect(Collectors.toList());

        sortEntities(entities);

        for (Entity target : entities) {
            if (blockEvent.getMode().equals("Pre") && autoblock.getMode().equals("Fake") || smooth.isToggled()){
                isBlocking = true;
            }
            switch (rotations.getMode()) {
                case "Vanilla":
                    mc.thePlayer.rotationYawHead = getVanillaRotations(target)[0];
                    mc.thePlayer.rotationPitchHead = getVanillaRotations(target)[1];

                    e.setYaw(getVanillaRotations(target)[0]);
                    e.setPitch(getVanillaRotations(target)[1]);
                break;
            }

            if (timer.hasTimeElapsed((long) (1000L / getCPS()))) {
                if (attackEvent.getMode().equals("Pre")) {
                    mc.thePlayer.swingItem();
                    if (keepSprint.isToggled()) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                    } else {
                        mc.playerController.attackEntity(mc.thePlayer, target);

                    }
                    entityAttacking = (EntityLivingBase) target;

                    timer.reset();
                }
               // blockSword();
                //blockSword();


                if (blockEvent.getMode().equals("Pre")) {
                    switch (autoblock.getMode()) {
                        case "Tick":
                            if (mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {

                                // mc.thePlayer.sendQueue.addToSendQueueSilent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
                                //mc.thePlayer.setItemInUse(mc.thePlayer.getCurrentEquippedItem(), 2);
                                if (block) {
                                    mc.thePlayer.stopUsingItem();
                                    block = false;

                                } else {
                                    if (mc.thePlayer.getCurrentEquippedItem() != null)
                                        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
                                    if (mc.thePlayer.ticksExisted % 2 == 0) {
                                        block = true;
                                    }
                                }

                            }
                            break;

                        case "Packet":
                            mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
                            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
                            break;
                    }
                }


            }

        }
        if ((autoblock.getMode().equals("Fake") || smooth.isToggled()) && entities.isEmpty() ) {
            isBlocking = false;
        }



    };

    @Subscribe
    public Consumer<EventPostMotion> onPostMotion = e -> {
        if (mc.thePlayer == null) return;

        for (Entity target : entities) {
            if (blockEvent.getMode().equals("Post") && autoblock.getMode().equals("Fake") || smooth.isToggled()){
                isBlocking = true;
            }

            if (timer.hasTimeElapsed((long) (1000L / getCPS()))) {
                if (attackEvent.getMode().equals("Post")) {

                    mc.thePlayer.swingItem();
                    if (hvhCrits.isToggled() && critEntity){
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.025F, mc.thePlayer.posZ, false));
                    }

                    if (keepSprint.isToggled()) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                    } else {
                        mc.playerController.attackEntity(mc.thePlayer, target);
                    }

                    if (hvhCrits.isToggled() && critEntity){
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
                    }

                    entityAttacking = (EntityLivingBase) target;

                    timer.reset();
                }

                if (blockEvent.getMode().equals("Post")) {
                    switch (autoblock.getMode()) {
                        case "Tick":
                            if (mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {

                                // mc.thePlayer.sendQueue.addToSendQueueSilent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
                                //mc.thePlayer.setItemInUse(mc.thePlayer.getCurrentEquippedItem(), 2);
                                if (block) {
                                    mc.thePlayer.stopUsingItem();
                                    block = false;

                                } else {
                                    if (mc.thePlayer.getCurrentEquippedItem() != null)
                                        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
                                    if (mc.thePlayer.ticksExisted % 2 == 0) {
                                        block = true;
                                    }
                                }

                            }
                            break;

                        case "Packet":
                            mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
                            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
                            break;
                    }
                }


            }

        }
        if (blockEvent.getMode().equals("Post") && (autoblock.getMode().equals("Fake") || smooth.isToggled()) && entities.isEmpty() ) {
            isBlocking = false;
        }

        entityAttacking = null;



    };

    @Subscribe
    public Consumer<EventTick> onTick = e -> {
        if (mc.thePlayer == null)
            return;
        this.setSuffix(minCps.getValue() + "-" + maxCps.getValue() + " " + reach.getValue());

        /*
        if (block) {
            mc.thePlayer.stopUsingItem();
            block = false;

        } else {
            if (mc.thePlayer.getCurrentEquippedItem() != null)
                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
            if (mc.thePlayer.ticksExisted % 4 == 0) {
                block = true;
            }
        }
        */
        critEntity = entityAttacking != null && entityAttacking.hurtTime > 0 && hvhCrits.isToggled();

        if (autoblock.getMode().equals("Test") && isBlocking){
            mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, null, 0f, 0f, 0f));

            if (timer.hasTimeElapsed((long) (1000L / getCPS()))){
                mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
        }

    };

    private boolean checkEntity(Entity entity){
        BackTrack backTrack = (BackTrack) Client.INSTANCE.getModuleManager().getModule("Back Track");

        return entity.isEntityAlive()
                && entity.getDistanceToEntity(mc.thePlayer) <= reach.getValue()
                && entity != mc.thePlayer
                && (backTrack.fakePlayer == null || !(entity.getUniqueID() == backTrack.fakePlayer.getUniqueID()))
                && entity instanceof EntityLivingBase
                && (!entity.isInvisible() || invisibles.isToggled())
                && (!(entity instanceof EntityPlayer) || players.isToggled())
                && (!(entity instanceof EntityMob) || mobs.isToggled());
    }

    private void sortEntities(List<Entity> entities){
        if (entities.isEmpty()) return;

        switch (sorting.getMode()){
            case "Health":
                entities.sort(Comparator.comparingInt(en -> (int)((EntityLivingBase)en).getHealth()));
                break;
            case "Distance":
                entities.sort(Comparator.comparingInt(en -> (int)en.getDistanceToEntity(mc.thePlayer)));
                break;
            case "HurtTime":
                entities.sort(Comparator.comparingInt(en -> -((EntityLivingBase)en).hurtTime));
                break;
        }
    }


    public float[] getVanillaRotations(Entity entityIn) // from EntityLiving, originally called faceEntity
    {
        EntityPlayerSP entity = mc.thePlayer; // the player (your character)

        double deltaX = entityIn.posX - entity.posX;
        double deltaZ = entityIn.posZ - entity.posZ;
        double deltaY;

        if (entityIn instanceof EntityLivingBase)
        {
            EntityLivingBase entitylivingbase = (EntityLivingBase)entityIn;
            deltaY = entitylivingbase.posY + (double)entitylivingbase.getEyeHeight() - (entity.posY + (double)entity.getEyeHeight());
        }
        else
        {
            deltaY = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0D - (entity.posY + (double)entity.getEyeHeight());
        }

        double hypotXZ = Math.hypot(deltaX,  deltaZ);
        float yaw = (float) Math.toDegrees(MathHelper.atan2(deltaZ, deltaX)) - 90.0F;
        float pitch = (float) Math.toDegrees(-(MathHelper.atan2(deltaY, hypotXZ)));
        return new float[]{yaw, pitch};
    }


}
