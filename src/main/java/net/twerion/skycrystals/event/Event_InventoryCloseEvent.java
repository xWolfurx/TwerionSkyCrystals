package net.twerion.skycrystals.event;

import net.twerion.skycrystals.SkyCrystals;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.scheduler.BukkitRunnable;

public class Event_InventoryCloseEvent implements Listener {

    public Event_InventoryCloseEvent() {
        Bukkit.getPluginManager().registerEvents(this, SkyCrystals.getInstance());
    }

    @EventHandler
    public void on(InventoryCloseEvent e) {
        if(!(e.getPlayer() instanceof Player)) {
            return;
        }

        Player player = (Player)e.getPlayer();
        if(e.getInventory().getType() == InventoryType.ANVIL) {
            if(SkyCrystals.getInstance().getManager().getInAnvil().contains(player)) {
                e.getInventory().clear();
                SkyCrystals.getInstance().getManager().getInAnvil().remove(player);
                (new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.openInventory(SkyCrystals.getInstance().getShopManager().buildShopInventory(player));
                    }
                }).runTaskLater(SkyCrystals.getInstance(), 1L);
            }
        }
    }
}
