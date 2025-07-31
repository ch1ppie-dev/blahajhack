package me.eternadox.client.api.util.world;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public final class AnchorData {
    /*
        full credit to kinggpengu <3
    */
    private final BlockPos anchorPosition;

    private final EnumFacing facing;

    public AnchorData(final BlockPos anchorPosition, final EnumFacing facing) {
        this.anchorPosition = anchorPosition;
        this.facing = facing;
    }

    public BlockPos getAnchorPosition() {
        return anchorPosition;
    }

    public EnumFacing getFacing() {
        return facing;
    }
}