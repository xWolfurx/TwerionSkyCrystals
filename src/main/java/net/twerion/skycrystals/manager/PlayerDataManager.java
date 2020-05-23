package net.twerion.skycrystals.manager;

import net.twerion.skycrystals.SkyCrystals;
import net.twerion.skycrystals.database.Callback;
import net.twerion.skycrystals.database.DatabaseUpdate;
import net.twerion.skycrystals.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerDataManager {

    private Map<Player, PlayerData> playerDataMap;

    private RankingManager rankingManager;

    public PlayerDataManager() {
        SkyCrystals.getInstance().getSQLManager().executeUpdate("CREATE TABLE IF NOT EXISTS `sky_crystals` (id INT NOT NULL AUTO_INCREMENT, player_name VARCHAR(16), crystals DOUBLE, UNIQUE KEY(id))");
        this.playerDataMap = new HashMap<>();
        this.loadPlayerDataForOnlinePlayers();

        this.rankingManager = new RankingManager();
    }

    private void loadPlayerDataForOnlinePlayers() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            this.addPlayerToCache(player);
        }
    }

    public void addPlayerToCache(Player player) {
        if(this.playerDataMap.containsKey(player)) return;

        PlayerData playerData = new PlayerData(player.getName());
        playerData.addReadyExecutor(new DatabaseUpdate.ReadyExecutor() {
            @Override
            public void ready() {
                SkyCrystals.getInstance().getPlayerDataManager().getOldSkyCrystalsAsync(player.getName(), new Callback<Double>() {
                    @Override
                    public void accept(Double crystals) {
                        if (crystals > 0.0D) {
                            PlayerData playerData = SkyCrystals.getInstance().getPlayerDataManager().getPlayerData(player);
                            playerData.addCrystals(crystals);
                            SkyCrystals.getInstance().getPlayerDataManager().resetOldSkyCrystals(player.getName());
                            player.sendMessage(SkyCrystals.getInstance().getFileManager().getConfigFile().getPrefix() + "§7Es wurden §a" + crystals + SkyCrystals.getInstance().getFileManager().getConfigFile().getCurrency() + " §7übertragen.");
                        }
                    }
                });
            }
        });
        this.playerDataMap.put(player, playerData);
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

    public RankingManager getRankingManager() {
        return this.rankingManager;
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

    public class RankingManager {

        private LinkedHashMap<String, Double> skyCrystalsRanking;

        public RankingManager() {
            this.skyCrystalsRanking = new LinkedHashMap<>();
            startUpdater();
        }

        public LinkedHashMap<String, Double> getSkyCrystalsRanking() {
            return this.skyCrystalsRanking;
        }

        private void startUpdater() {
            (new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true) {
                        try {
                            PreparedStatement st = SkyCrystals.getInstance().getSQLManager().getConnection().prepareStatement("SELECT `player_name`, `crystals` FROM `sky_crystals` ORDER BY `crystals` DESC LIMIT 0,10");
                            ResultSet rs = SkyCrystals.getInstance().getSQLManager().executeQuery(st);
                            RankingManager.this.skyCrystalsRanking.clear();
                            while(rs.next()) {
                                String playerName = rs.getString("player_name");
                                double crystals = rs.getDouble("crystals");
                                RankingManager.this.skyCrystalsRanking.put(playerName, crystals);
                            }
                            rs.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        try {
                            Thread.sleep(30000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            })).start();
        }

    }

}
