package me.eternadox.client.impl.event;

import me.eternadox.bus.event.CancellableEvent;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;

public class EventItemRender extends CancellableEvent {
    private EnumAction action;
    private boolean usingItem;
    private ItemStack itemRendered;
    private float animationProgression, partialTicks, swingProgress;

    public EventItemRender(EnumAction action, boolean usingItem, float animationProgression, float partialTicks, float swingProgress, ItemStack itemRendered){
        this.action = action;
        this.usingItem = usingItem;
        this.animationProgression = animationProgression;
        this.partialTicks = partialTicks;
        this.swingProgress = swingProgress;
        this.itemRendered = itemRendered;

    }

    public EnumAction getAction() {
        return action;
    }

    public void setAction(EnumAction action) {
        this.action = action;
    }

    public boolean isUsingItem() {
        return usingItem;
    }

    public void setUsingItem(boolean usingItem) {
        this.usingItem = usingItem;
    }

    public ItemStack getItemRendered() {
        return itemRendered;
    }

    public void setItemRendered(ItemStack itemRendered) {
        this.itemRendered = itemRendered;
    }

    public float getAnimationProgression() {
        return animationProgression;
    }

    public void setAnimationProgression(float animationProgression) {
        this.animationProgression = animationProgression;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getSwingProgress() {
        return swingProgress;
    }

    public void setSwingProgress(float swingProgress) {
        this.swingProgress = swingProgress;
    }
}
