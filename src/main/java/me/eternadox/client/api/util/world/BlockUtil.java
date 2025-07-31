package me.eternadox.client.api.util.world;
import me.eternadox.client.api.util.Methods;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3i;

/*
 full credit to kinggpengu <3
*/

public final class BlockUtil implements Methods {

    public static boolean isAirBlock(final double x, final double y, final double z) {
        return getBlockState(x, y, z) instanceof BlockAir;
    }

    public static boolean isSolidBlock(final double x, final double y, final double z) {
        final Block block = getBlockState(x, y, z);

        return block instanceof BlockAir || block instanceof BlockLiquid;
    }

    public static Block getBlockState(final double x, final double y, final double z) {
        return mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
    }
    public static boolean isOnGround(double y) {
        return !mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -y, 0.0D)).isEmpty();
    }
    public static Block getBlock(double y) {
        return mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -y, 0.0D).minY, mc.thePlayer.posZ)).getBlock();
    }
    public boolean isBlock(Block block, Block block2) {
        return block == block2;
    }

    public static boolean isLookingAtBlock(BlockPos blockFacing, float yaw, float pitch, EnumFacing enumFacing) {
        MovingObjectPosition movingObjPos = mc.objectMouseOver;

        return !(movingObjPos == null);
    }

    public static AnchorData getAnchorForBlockPos(final BlockPos position) {
        final double posX = position.getX();
        final double posY = position.getY();
        final double posZ = position.getZ();

        for(final EnumFacing facing : EnumFacing.values()) {
            if(facing == EnumFacing.UP || facing == EnumFacing.DOWN)
                continue;
            final Vec3i facingVec = facing.getDirectionVec();
            final BlockPos offsetPotential = position.add(facingVec);

            final int directionX = facingVec.getX();
            final int directionZ = facingVec.getY();

            if(canBePlacedOn(offsetPotential))
                return new AnchorData(offsetPotential, facing.getOpposite());

            final int offsetX = directionX != 0 ? 0 : directionZ;
            final int offsetZ = directionZ != 0 ? 0 : directionX;

            final BlockPos offsetOffsetPotential = offsetPotential.add(offsetX, 0, offsetZ);

            if(canBePlacedOn(offsetOffsetPotential))
                return new AnchorData(offsetOffsetPotential, facing.getOpposite());
        }

        BlockPos down = position;
        for(int i = 0; i < 3; ++i) {
            if(!canBePlacedOn(down)) {
                down = down.down();
                continue;
            }
            return new AnchorData(down, EnumFacing.UP);
        }
        return null;
    }

    public static boolean canBePlacedOn(final BlockPos blockPos) {
        final Material material = mc.theWorld.getBlockState(blockPos).getBlock().getMaterial();
        return material.blocksMovement() && material.isSolid();
    }
}