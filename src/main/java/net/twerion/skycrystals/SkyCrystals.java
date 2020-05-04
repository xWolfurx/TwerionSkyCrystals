package net.twerion.skycrystals;

import net.twerion.skycrystals.api.SkyCrystalsManager;
import net.twerion.skycrystals.command.Command_SkyCrystals;
import net.twerion.skycrystals.database.SQLManager;
import net.twerion.skycrystals.event.*;
import net.twerion.skycrystals.manager.FileManager;
import net.twerion.skycrystals.manager.Manager;
import net.twerion.skycrystals.manager.PlayerDataManager;
import net.twerion.skycrystals.manager.ShopManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class SkyCrystals extends JavaPlugin {

    private static SkyCrystals instance;

    private FileManager fileManager;
    private SQLManager sqlManager;
    private PlayerDataManager playerDataManager;
    private ShopManager shopManager;
    private Manager manager;

    private static SkyCrystalsManager skyCrystalsManager;

    @Override
    public void onLoad() {
        SkyCrystals.instance = this;
    }

    @Override
    public void onEnable() {
        this.registerCommands();
        this.registerEvents();
        this.registerManager();
    }

    @Override
    public void onDisable() {
        this.sqlManager.getUpdater().saveAll();
        this.sqlManager.getUpdater().setActive(false);
        this.sqlManager.closeConnection();
    }

    public static SkyCrystals getInstance() {
        return SkyCrystals.instance;
    }

    private void registerManager() {
        this.fileManager = new FileManager();
        this.loadSQL();
        this.playerDataManager = new PlayerDataManager();
        this.shopManager = new ShopManager();
        this.manager = new Manager();

        SkyCrystals.skyCrystalsManager = new SkyCrystalsManager();
    }

    private void registerCommands() {
        getCommand("SkyCrystals").setExecutor(new Command_SkyCrystals());
        getCommand("Scs").setExecutor(new Command_SkyCrystals());
    }

    private void registerEvents() {
        new Event_InventoryClickEvent();
        new Event_InventoryCloseEvent();
        new Event_PlayerInteractEvent();
        new Event_PlayerJoinEvent();
        new Event_PlayerQuitEvent();
    }

    private boolean loadSQL() {
        FileConfiguration cfg = this.getFileManager().getConfigFile().getConfig();
        String host = cfg.getString("MySQL.Host");
        String port = cfg.getString("MySQL.Port");
        String username = cfg.getString("MySQL.Username");
        String password = cfg.getString("MySQL.Password");
        String database = cfg.getString("MySQL.Database");
        sqlManager = new SQLManager(host, port, username, password, database);
        return sqlManager.openConnection();
    }

    public FileManager getFileManager() {
        return this.fileManager;
    }

    public SQLManager getSQLManager() {
        return this.sqlManager;
    }

    public PlayerDataManager getPlayerDataManager() {
        return this.playerDataManager;
    }

    public ShopManager getShopManager() {
        return this.shopManager;
    }

    public Manager getManager() {
        return this.manager;
    }

    public static SkyCrystalsManager getSkyCrystalsManager() {
        return SkyCrystals.skyCrystalsManager;
    }
}
