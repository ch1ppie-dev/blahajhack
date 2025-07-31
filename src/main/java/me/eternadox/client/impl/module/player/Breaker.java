package me.eternadox.client.impl.module.player;

import me.eternadox.bus.annotations.Subscribe;
import java.util.function.Consumer;
import me.eternadox.client.api.module.Category;
import me.eternadox.client.api.module.Module;
import me.eternadox.client.api.module.annotations.Info;
import me.eternadox.client.impl.event.EventPreMotion;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@Info(displayName = "Breaker", category = Category.MOVE)
public class Breaker extends Module {



    @Subscribe
    public Consumer<EventPreMotion> onPreMotion = e -> {
        findBlockAndDestroy();
    };

    private void breakBlock(BlockPos pos){
        mc.thePlayer.swingItem();
        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.DOWN));
        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.DOWN));
        mc.playerController.clickBlock(pos, EnumFacing.DOWN);
    }

    private void findBlockAndDestroy(){
        for (int x = -6; x <= 6; x += 1){
            for (int y = -6; y <= 6; y += 1){
                for (int z = -6; z <= 6; z += 1){
                   BlockPos pos  = mc.thePlayer.getPosition().add(x, y, z);
                   IBlockState state = mc.theWorld.getBlockState(pos);

                   if (!(state.getBlock() instanceof BlockBed))
                       continue;

                   if (mc.thePlayer.getBedLocation() == null || mc.thePlayer.getBedLocation() != pos) {
                       breakBlock(pos);
                   }



                }
            }
        }
    }
}
