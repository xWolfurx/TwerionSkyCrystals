package net.twerion.skycrystals.other;

import net.minecraft.server.v1_15_R1.*;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Anvil {

    private EntityPlayer entityPlayer;
    private String title;
    private AnvilContainer container;
    private int c;

    public Anvil(Player player) {
        entityPlayer = ((CraftPlayer)player).getHandle();
        c = entityPlayer.nextContainerCounter();
        container = new AnvilContainer(c, entityPlayer);
    }

    public void open() {
        entityPlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(c, Containers.ANVIL, new ChatMessage(title)));
        entityPlayer.activeContainer = container;
        entityPlayer.activeContainer.addSlotListener(entityPlayer);
    }

    public Anvil setTitle(String title) {
        this.title = title;
        return this;
    }

    public Anvil addItem(AnvilSlot anvilSlot, ItemStack itemStack) {
        container.setItem(anvilSlot.getSlot(), CraftItemStack.asNMSCopy(itemStack));
        return this;
    }

    public enum AnvilSlot {

        INPUT_LEFT(0),
        INPUT_RIGHT(1),
        OUTPUT(2);

        private int slot;

        AnvilSlot(int slot) {
            this.slot = slot;
        }

        public int getSlot() {
            return this.slot;
        }

    }

    private class AnvilContainer extends ContainerAnvil {

        public AnvilContainer(int containerId, EntityHuman entityHuman) {
            super(containerId, entityHuman.inventory, ContainerAccess.at(entityHuman.world, new BlockPosition(0, 0, 0)));
            this.checkReachable = false;
        }

    }

}
