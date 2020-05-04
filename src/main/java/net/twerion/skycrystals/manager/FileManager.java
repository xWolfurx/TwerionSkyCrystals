package net.twerion.skycrystals.manager;

import net.twerion.skycrystals.file.*;

public class FileManager {

    private ConfigFile configFile;
    private PermissionFile permissionFile;
    private ShopItemFile shopItemFile;
    private SellItemFile sellItemFile;

    public FileManager() {
        this.configFile = new ConfigFile();
        this.permissionFile = new PermissionFile();
        this.shopItemFile = new ShopItemFile();
        this.sellItemFile = new SellItemFile();
    }

    public ConfigFile getConfigFile() {
        return this.configFile;
    }

    public PermissionFile getPermissionFile() {
        return this.permissionFile;
    }

    public ShopItemFile getShopItemFile() {
        return this.shopItemFile;
    }

    public SellItemFile getSellItemFile() {
         return this.sellItemFile;
    }

}
