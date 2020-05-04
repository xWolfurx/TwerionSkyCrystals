package net.twerion.skycrystals.manager;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {

    private List<Player> inAnvil;

    public Manager() {
        this.inAnvil = new ArrayList<>();
    }

    public int getAvailableItems(Inventory inventory, ItemStack itemStack) {
        int counted = 0;
        for(ItemStack content : inventory.getContents()) {
            if(content != null && content.getType() != Material.AIR && content.getType() == itemStack.getType() && content.getDurability() == itemStack.getDurability()) {
                counted += content.getAmount();
            }
        }
        return counted;
    }

    public int getAvailableItemsExactly(Inventory inventory, ItemStack itemStack) {
        int counted = 0;
        for(ItemStack content : inventory.getContents()) {
            if(content != null && content.getType() != Material.AIR && content.getType() == itemStack.getType() && content.getDurability() == itemStack.getDurability() && content.hasItemMeta() && itemStack.hasItemMeta()) {
                if(content.getItemMeta().hasDisplayName() && itemStack.getItemMeta().hasDisplayName() && content.getItemMeta().getDisplayName().equals(itemStack.getItemMeta().getDisplayName())) {
                    counted += content.getAmount();
                }
            }
        }
        return counted;
    }


    public boolean hasEnoughItems(Inventory inventory, ItemStack itemStack, int amount) {
        return (this.getAvailableItems(inventory, itemStack) >= amount);
    }

    public boolean hasEnoughItemsExactly(Inventory inventory, ItemStack itemStack, int amount) {
        return (this.getAvailableItemsExactly(inventory, itemStack) >= amount);
    }

    public boolean removeItems(Inventory inventory, ItemStack itemStack, int amount) {
        if(!this.hasEnoughItems(inventory, itemStack, amount)) {
            return false;
        }
        int toRemove = amount;
        Map<Integer, ItemStack> slots = new HashMap<>();

        for(int slot = 0; slot < inventory.getSize(); slot++) {
            ItemStack slotItem = inventory.getItem(slot);
            if(slotItem != null && slotItem.getType() != Material.AIR && slotItem.getType() == itemStack.getType() && slotItem.getDurability() == itemStack.getDurability()) {
                slots.put(Integer.valueOf(slot), slotItem);
            }
        }

        for(Map.Entry<Integer, ItemStack> entrySlots : slots.entrySet()) {
            if(((ItemStack)entrySlots.getValue()).getAmount() <= toRemove) {
                inventory.setItem(((Integer)entrySlots.getKey()).intValue(), new ItemStack(Material.AIR));
                toRemove -= ((ItemStack)entrySlots.getValue()).getAmount();
                continue;
            }
            ItemStack invItem = inventory.getItem(((Integer)entrySlots.getKey()).intValue());
            invItem.setAmount(invItem.getAmount() - toRemove);
            break;
        }
        return true;
    }

    public boolean removeItemsExactly(Inventory inventory, ItemStack itemStack, int amount) {
        if(!this.hasEnoughItemsExactly(inventory, itemStack, amount)) {
            return false;
        }
        int toRemove = amount;
        Map<Integer, ItemStack> slots = new HashMap<>();

        for(int slot = 0; slot < inventory.getSize(); slot++) {
            ItemStack slotItem = inventory.getItem(slot);
            if(slotItem != null && slotItem.getType() != Material.AIR && slotItem.getType() == itemStack.getType() && slotItem.getDurability() == itemStack.getDurability() && slotItem.hasItemMeta() && itemStack.hasItemMeta()) {
                if(slotItem.getItemMeta().hasDisplayName() && itemStack.getItemMeta().hasDisplayName() && slotItem.getItemMeta().getDisplayName().equals(itemStack.getItemMeta().getDisplayName())) {
                    slots.put(Integer.valueOf(slot), slotItem);
                }
            }
        }

        for(Map.Entry<Integer, ItemStack> entrySlots : slots.entrySet()) {
            if(((ItemStack)entrySlots.getValue()).getAmount() <= toRemove) {
                inventory.setItem(((Integer)entrySlots.getKey()).intValue(), new ItemStack(Material.AIR));
                toRemove -= ((ItemStack)entrySlots.getValue()).getAmount();
                continue;
            }
            ItemStack invItem = inventory.getItem(((Integer)entrySlots.getKey()).intValue());
            invItem.setAmount(invItem.getAmount() - toRemove);
            break;
        }
        return true;
    }


    public void addItem(Player player, ItemStack itemStack) {
        if(player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
        } else {
            player.getInventory().addItem(new ItemStack[] { itemStack } );
        }
    }

    public List<Player> getInAnvil() {
        return this.inAnvil;
    }
}
