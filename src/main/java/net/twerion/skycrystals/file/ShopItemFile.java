package net.twerion.skycrystals.file;

import net.twerion.skycrystals.shop.ShopItem;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;

public class ShopItemFile extends FileBase {

    public ShopItemFile() {
        super("shop", "shopItems");
        this.writeDefault();
    }

    private void writeDefault() {
        ShopItem defaultShopItem = new ShopItem(2, Material.PLAYER_HEAD, "&aSpawnegg &8(&aZombie&8) &7&o<Klick>", Arrays.asList(new String[] { " ", " &8➥ &7Mob &8» &aZombie", " ➥ &8Preis &8» &a%PRICE%" } ), "", "", 7000.0D, 1, Material.ZOMBIE_SPAWN_EGG, "&aSpawnegg &8(&aZombie&8)", Arrays.asList(new String[] { " &8➥ &7Mob &8» &aZombie" } ));

        FileConfiguration cfg = getConfig();
        
        cfg.addDefault("ShopItem." + defaultShopItem.getSlot() + ".DisplayMaterial", defaultShopItem.getDisplayMaterial().name());
        cfg.addDefault("ShopItem." + defaultShopItem.getSlot() + ".DisplayName", defaultShopItem.getDisplayName());
        cfg.addDefault("ShopItem." + defaultShopItem.getSlot() + ".DisplayLore", defaultShopItem.getDisplayLore());
        cfg.addDefault("ShopItem." + defaultShopItem.getSlot() + ".SkinId", defaultShopItem.getSkinId());
        cfg.addDefault("ShopItem." + defaultShopItem.getSlot() + ".SkinTexture", defaultShopItem.getSkinTexture());
        cfg.addDefault("ShopItem." + defaultShopItem.getSlot() + ".Price", defaultShopItem.getPrice());
        cfg.addDefault("ShopItem." + defaultShopItem.getSlot() + ".Amount", defaultShopItem.getAmount());
        cfg.addDefault("ShopItem." + defaultShopItem.getSlot() + ".ItemMaterial", defaultShopItem.getItemMaterial().name());
        cfg.addDefault("ShopItem." + defaultShopItem.getSlot() + ".ItemName", defaultShopItem.getItemName());
        cfg.addDefault("ShopItem." + defaultShopItem.getSlot() + ".ItemLore", defaultShopItem.getItemLore());

        cfg.options().copyDefaults(true);
        saveConfig();
    }

    private void loadShopItems() {

    }

}
