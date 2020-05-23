package net.twerion.skycrystals.event;

import net.twerion.skycrystals.SkyCrystals;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class Event_CreatureSpawnEvent implements Listener {

    public Event_CreatureSpawnEvent() {
        Bukkit.getPluginManager().registerEvents(this, SkyCrystals.getInstance());
    }

    @EventHandler
    public void on(CreatureSpawnEvent e) {
        if(e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG) {
            LivingEntity livingEntity = e.getEntity();
            livingEntity.setCustomName("");
            livingEntity.setCustomNameVisible(false);
        }
    }
}
