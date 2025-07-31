package me.eternadox.client.impl.event;

import me.eternadox.bus.event.CancellableEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;


public class EventCollide extends CancellableEvent {
    private AxisAlignedBB aabb;
    private double x, y, z;
    private Block block;
    private Entity entity;

    public EventCollide(double x, double y, double z, AxisAlignedBB aabb, Entity entity, Block block){
        this.x = x;
        this.y = y;
        this.z = z;
        this.aabb = aabb;
        this.entity = entity;
        this.block = block;
    }

    public void setBoundingBox(AxisAlignedBB aabb) {
        this.aabb = aabb;
    }

    public AxisAlignedBB getBoundingBox() {
        return aabb;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public Block getBlock() {
        return block;
    }

    public Entity getEntity() {
        return entity;
    }
}
