package net.twerion.skycrystals.event;

import net.twerion.skycrystals.SkyCrystals;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Event_PlayerQuitEvent implements Listener {

    public Event_PlayerQuitEvent() {
        Bukkit.getPluginManager().registerEvents(this, SkyCrystals.getInstance());
    }

    @EventHandler
    public void on(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        SkyCrystals.getInstance().getPlayerDataManager().removePlayerFromCache(player);
    }
}
