package net.twerion.skycrystals.event;

import net.twerion.skycrystals.SkyCrystals;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Event_PlayerJoinEvent implements Listener {

    public Event_PlayerJoinEvent() {
        Bukkit.getPluginManager().registerEvents(this, SkyCrystals.getInstance());
    }

    @EventHandler
    public void on(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        SkyCrystals.getInstance().getPlayerDataManager().addPlayerToCache(player);
    }
}
