package net.twerion.skycrystals.manager;

import net.twerion.skycrystals.SkyCrystals;
import net.twerion.skycrystals.database.Callback;
import net.twerion.skycrystals.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    private double getOldSkyCrystals(String playerName) {
        double crystals = 0.0D;
        try {
            PreparedStatement st = SkyCrystals.getInstance().getSQLManager().getConnection().prepareStatement("SELECT * FROM `crystals` WHERE `name` = ?");
            st.setString(1, playerName);
            ResultSet rs = SkyCrystals.getInstance().getSQLManager().executeQuery(st);
            if(rs.next()) {
                crystals = rs.getDouble("crystals");
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return crystals;
    }

    public void getOldSkyCrystalsAsync(final String playerName, final Callback<Double> callback) {
        SkyCrystals.getInstance().getSQLManager().getAsyncHandler().getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                callback.accept(PlayerDataManager.this.getOldSkyCrystals(playerName));
            }
        });
    }

    public void resetOldSkyCrystals(String playerName) {
        try {
            PreparedStatement stCheck = SkyCrystals.getInstance().getSQLManager().getConnection().prepareStatement("SELECT * FROM `crystals` WHERE `name` = ?");
            stCheck.setString(1, playerName);
            ResultSet rs = SkyCrystals.getInstance().getSQLManager().executeQuery(stCheck);
            if(rs.next()) {
                PreparedStatement st = SkyCrystals.getInstance().getSQLManager().getConnection().prepareStatement("UPDATE `crystals` SET `crystals` = ? WHERE `name` = ?");
                st.setDouble(1, 0.0D);
                st.setString(2, playerName);
                SkyCrystals.getInstance().getSQLManager().executeUpdate(st);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
