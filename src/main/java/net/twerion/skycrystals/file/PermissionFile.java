package net.twerion.skycrystals.file;

import org.bukkit.configuration.file.FileConfiguration;

public class PermissionFile extends FileBase {

    public PermissionFile() {
        super("", "permissions");
        this.writeDefaults();
    }

    private void writeDefaults() {
        FileConfiguration cfg = getConfig();

        cfg.addDefault("Command.SkyCrystals.Admin", 150);

        cfg.options().copyDefaults(true);
        saveConfig();
    }

    public int getAccessLevel(String path) {
        FileConfiguration cfg = getConfig();
        if(cfg.get(path) == null) return 150;
        return cfg.getInt(path);
    }
}
