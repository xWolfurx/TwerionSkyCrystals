package net.twerion.skycrystals.player;

import net.twerion.skycrystals.SkyCrystals;
import net.twerion.skycrystals.database.DatabaseUpdate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerData extends DatabaseUpdate {

    private String playerName;
    private double crystals;

    public PlayerData(String playerName, boolean addUpdater) {
        this.playerName = playerName;
        this.crystals = 0.0D;

        this.loadDataAsync();

        if(addUpdater) this.addToUpdater();
    }

    public PlayerData(String playerName) {
        this(playerName, true);
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public double getCrystals() {
        return this.crystals;
    }

    public void setCrystals(double crystals) {
        this.crystals = crystals;
        this.setUpdate(true);
    }

    public void addCrystals(double crystals) {
        this.crystals += crystals;
        this.setUpdate(true);
    }

    public void removeCrystals(double crystals) {
        if((this.crystals - crystals) <= 0) this.crystals = 0;
        this.crystals -= crystals;
        this.setUpdate(true);
    }

    public boolean hasEnoughCrystals(double crystals) {
        return this.crystals >= crystals;
    }

    @Override
    public void loadData() {
        try {
            PreparedStatement st = SkyCrystals.getInstance().getSQLManager().getConnection().prepareStatement("SELECT * FROM `sky_crystals` WHERE `player_name` = ?");
            st.setString(1, this.getPlayerName());
            ResultSet rs = SkyCrystals.getInstance().getSQLManager().executeQuery(st);
            if(!rs.next()) {
                this.saveData();
            } else {
                this.crystals = rs.getDouble("crystals");
            }
            rs.close();
            st.close();
            this.setReady(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadDataAsync() {
        SkyCrystals.getInstance().getSQLManager().getAsyncHandler().getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                PlayerData.this.loadData();
            }
        });
    }

    @Override
    public void saveData() {
        try {
            PreparedStatement stCheck = SkyCrystals.getInstance().getSQLManager().getConnection().prepareStatement("SELECT * FROM `sky_crystals` WHERE `player_name` = ?");
            stCheck.setString(1, this.getPlayerName());
            ResultSet rsCheck = SkyCrystals.getInstance().getSQLManager().executeQuery(stCheck);
            if(!rsCheck.next()) {
                PreparedStatement st = SkyCrystals.getInstance().getSQLManager().getConnection().prepareStatement("INSERT INTO `sky_crystals` (player_name, crystals) VALUES (? ,?)");
                st.setString(1, this.getPlayerName());
                st.setDouble(2, this.getCrystals());
                SkyCrystals.getInstance().getSQLManager().executeUpdate(st);
            } else {
                PreparedStatement st = SkyCrystals.getInstance().getSQLManager().getConnection().prepareStatement("UPDATE `sky_crystals` SET `crystals` = ? WHERE `player_name` = ?");
                st.setDouble(1, this.getCrystals());
                st.setString(2, this.getPlayerName());
                SkyCrystals.getInstance().getSQLManager().executeUpdate(st);
            }
            rsCheck.close();
            stCheck.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveDataAsync() {
        SkyCrystals.getInstance().getSQLManager().getAsyncHandler().getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                PlayerData.this.saveData();
            }
        });
    }

}
