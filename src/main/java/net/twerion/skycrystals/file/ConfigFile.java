package net.twerion.skycrystals.file;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class ConfigFile extends FileBase {

    private String prefix;
    private String noPermission;
    private String wrongCommandExecutor;
    private String currency;

    private Material ironGolemMaterial;
    private String ironGolemDisplayName;

    public ConfigFile() {
        super("", "config");
        this.writeDefaults();

        this.loadPrefix();
        this.loadNoPermission();
        this.loadWrongCommandExecutor();
        this.loadCurrency();
        this.loadIronGolemMaterial();
        this.loadIronGolemDisplayName();
    }

    private void writeDefaults() {
        FileConfiguration cfg = getConfig();

        cfg.addDefault("MySQL.Host", "localhost");
        cfg.addDefault("MySQL.Port", "3306");
        cfg.addDefault("MySQL.Username", "Username");
        cfg.addDefault("MySQL.Password", "Password");
        cfg.addDefault("MySQL.Database", "Database");

        cfg.addDefault("System.Prefix", "§8[§eTwerion§8] ");
        cfg.addDefault("System.NoPermission", "§cDu hast keinen Zugriff auf diesen Befehl.");
        cfg.addDefault("System.WrongCommandExecutor", "§cDu kannst diesen Befehl nicht verwenden.");
        cfg.addDefault("System.Currency", "✮");

        cfg.addDefault("Spawner.IronGolemMaterial", Material.FIREWORK_STAR.name());
        cfg.addDefault("Spawner.IronGolemDisplayName", "§aSpawnegg §8(§aIron Golem§8)");

        cfg.options().copyDefaults(true);
        saveConfig();
    }

    private void loadPrefix() {
        FileConfiguration cfg = getConfig();
        if(cfg.get("System.Prefix") == null) return;
        this.prefix = cfg.getString("System.Prefix");
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        FileConfiguration cfg = getConfig();
        cfg.set("System.Prefix", this.prefix);
        saveConfig();
    }

    private void loadNoPermission() {
        FileConfiguration cfg = getConfig();
        if(cfg.get("System.NoPermission") == null) return;
        this.noPermission = cfg.getString("System.NoPermission");
    }

    private void loadWrongCommandExecutor() {
        FileConfiguration cfg = getConfig();
        if(cfg.get("System.WrongCommandExecutor") == null) return;
        this.wrongCommandExecutor = cfg.getString("System.WrongCommandExecutor");
    }

    private void loadCurrency() {
        FileConfiguration cfg = getConfig();
        if(cfg.get("System.Currency") == null) return;
        this.currency = cfg.getString("System.Currency");
    }

    private void loadIronGolemMaterial() {
        FileConfiguration cfg = getConfig();
        if(cfg.get("Spawner.IronGolemMaterial") == null) return;
        this.ironGolemMaterial = Material.matchMaterial(cfg.getString("Spawner.IronGolemMaterial"));
    }

    private void loadIronGolemDisplayName() {
        FileConfiguration cfg = getConfig();
        if(cfg.get("Spawner.IronGolemDisplayName") == null) return;
        this.ironGolemDisplayName = cfg.getString("Spawner.IronGolemDisplayName");
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getNoPermission() {
        return this.noPermission;
    }

    public String getWrongCommandExecutor() {
        return this.wrongCommandExecutor;
    }

    public String getCurrency() {
        return this.currency;
    }

    public Material getIronGolemMaterial() {
        return this.ironGolemMaterial;
    }

    public String getIronGolemDisplayName() {
        return this.ironGolemDisplayName;
    }

}
