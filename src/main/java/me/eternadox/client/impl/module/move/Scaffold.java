package me.eternadox.client.impl.module.move;

import me.eternadox.bus.annotations.Subscribe;
import java.util.function.Consumer;
import me.eternadox.client.api.module.Category;
import me.eternadox.client.api.module.Module;
import me.eternadox.client.api.module.annotations.Info;
import me.eternadox.client.api.util.world.AnchorData;
import me.eternadox.client.api.util.world.BlockUtil;
import me.eternadox.client.impl.event.EventPreMotion;
import me.eternadox.client.impl.setting.BooleanSetting;
import me.eternadox.client.impl.setting.ModeSetting;
import net.minecraft.block.*;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@Info(displayName = "Scaffold", category = Category.MOVE)
public class Scaffold extends Module {
    private final BooleanSetting safeWalk = new BooleanSetting("Safe Walk", true);
    private final BooleanSetting swing = new BooleanSetting("Swing", true);
    private final BooleanSetting sprint = new BooleanSetting("Sprint", false);

    private final ModeSetting rotations = new ModeSetting("Rotations", "Back", "None", "Back");


    public Scaffold(){
        this.addSettings(safeWalk, swing, sprint, rotations);
    }

    private boolean usableBlock(ItemBlock item) {
        return !(item.getBlock() instanceof BlockSand || item.getBlock() instanceof BlockSlab || item.getBlock() instanceof BlockAnvil || item.getBlock() instanceof BlockBed || !item.getBlock().isFullBlock() || item.getBlock() instanceof BlockChest);
    }


    @Subscribe
    public Consumer<EventPreMotion> onPreMotion = e -> {
        if (mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock) {
            ItemBlock itemBlock = (ItemBlock) mc.thePlayer.getCurrentEquippedItem().getItem();
            if (usableBlock(itemBlock)) {

                mc.thePlayer.setSprinting(sprint.isToggled());

                switch (rotations.getMode()) {
                    case "Back":
                        e.setYaw(mc.thePlayer.rotationYaw - 180);
                        e.setPitch(80);
                    break;
                }


                BlockPos pos = new BlockPos(mc.thePlayer).down();
               // anchorData = getNextPos(pos);
                AnchorData anchorData = BlockUtil.getAnchorForBlockPos(pos);

                if (anchorData == null) {
                    if (safeWalk.isToggled())
                        mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
                } else if (BlockUtil.isAirBlock(pos.getX(), pos.getY(), pos.getZ())) {
                    placeBlock(anchorData.getAnchorPosition(), anchorData.getFacing(), mc.thePlayer.getCurrentEquippedItem());

                }

            }
        }
    };

    private void placeBlock(BlockPos pos, EnumFacing enumFacing, ItemStack item){
        if (swing.isToggled())
            mc.thePlayer.swingItem();
        mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, item, pos, enumFacing, mc.objectMouseOver.hitVec);
    }

    /* TODO: work on better way of getting block
    private AnchorData getNextPos(BlockPos currentPos){
        for (int x = -3; x <= 3; x += 0.5){
            for (int y = -3; y <= 3; y += 0.5){
                for (int z = -3; z <= 3; z += 0.5){
                    BlockPos newPos = currentPos.add(x, y, z);
                    AnchorData data = BlockUtil.getAnchorForBlockPos(newPos);

                    if (data != null && BlockUtil.isAirBlock(newPos.getX(), newPos.getY(), newPos.getZ())) {
                        return data;
                    }
                }
            }
        }
        return null;
    }
    */
}
