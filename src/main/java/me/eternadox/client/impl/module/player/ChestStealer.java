package me.eternadox.client.impl.module.player;

import me.eternadox.bus.annotations.Subscribe;
import java.util.function.Consumer;
import me.eternadox.client.api.module.Category;
import me.eternadox.client.api.module.Module;
import me.eternadox.client.api.module.annotations.Info;
import me.eternadox.client.api.util.misc.TimerUtil;
import me.eternadox.client.impl.event.EventTick;
import me.eternadox.client.impl.setting.NumberSetting;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.Objects;

@Info(displayName = "Stealer", category = Category.PLAYER)
public class ChestStealer extends Module {

    private final TimerUtil timer = new TimerUtil();
    private final NumberSetting delay = new NumberSetting("Delay", 50,0, 1000, 50.0);


    public ChestStealer(){
        this.addSettings(delay);
    }
    @Subscribe
    public Consumer<EventTick> onTick = e -> {
        this.setSuffix(Double.toString(delay.getValue()));
        if (mc.thePlayer == null || mc.thePlayer.openContainer == null || !(mc.currentScreen instanceof GuiChest)) return;

        GuiChest chest = (GuiChest) mc.currentScreen;
        IInventory inv =  chest.lowerChestInventory;


        for (int i = 0; i < inv.getSizeInventory(); i++){
            ItemStack stack = chest.lowerChestInventory.getStackInSlot(i);

            if (stack != null && !stack.hasDisplayName() && timer.hasTimeElapsed((long)delay.getValue())) {
                mc.playerController.windowClick(chest.inventorySlots.windowId, i, 0, 1, mc.thePlayer);
                timer.reset();
            }
        }

        if (Arrays.stream(((InventoryBasic)chest.lowerChestInventory).inventoryContents).allMatch(Objects::isNull))
            mc.thePlayer.closeScreen();

    };
}
