package net.twerion.skycrystals.manager;

import net.twerion.skycrystals.SkyCrystals;
import net.twerion.skycrystals.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerDataManager {

    private Map<Player, PlayerData> playerDataMap;

    public PlayerDataManager() {
        SkyCrystals.getInstance().getSQLManager().executeUpdate("CREATE TABLE IF NOT EXISTS `sky_crystals` (id INT NOT NULL AUTO_INCREMENT, player_name VARCHAR(16), crystals DOUBLE, UNIQUE KEY(id))");
        this.playerDataMap = new HashMap<>();
        this.loadPlayerDataForOnlinePlayers();
    }

    private void loadPlayerDataForOnlinePlayers() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            this.addPlayerToCache(player);
        }
    }

    public void addPlayerToCache(Player player) {
        if(this.playerDataMap.containsKey(player)) return;
        this.playerDataMap.put(player, new PlayerData(player.getName()));
    }

    public void removePlayerFromCache(Player player) {
        if(!(this.playerDataMap.containsKey(player))) return;
        PlayerData playerData = this.playerDataMap.get(player);
        playerData.saveDataAsync();
        playerData.removeFromUpdater();
        this.playerDataMap.remove(player);
    }

    public PlayerData getPlayerData(Player player) {
        if(!(this.playerDataMap.containsKey(player))) this.addPlayerToCache(player);
        return this.playerDataMap.get(player);
    }

    public Map<Player, PlayerData> getPlayerDataMap() {
        return this.playerDataMap;
    }

}
