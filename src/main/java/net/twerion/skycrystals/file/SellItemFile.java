package net.twerion.skycrystals.file;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class SellItemFile extends FileBase {

    public SellItemFile() {
        super("shop", "sellItems");
        this.writeDefaults();
    }

    private void writeDefaults() {
        FileConfiguration cfg = getConfig();

        List<String> defaultItems = new ArrayList<>();
        defaultItems.add(Material.EMERALD_BLOCK.name() + ";" + Double.valueOf(3.0) + ";Smaragdblock");
        defaultItems.add(Material.DIAMOND_BLOCK.name() + ";" + Double.valueOf(2.0) + ";Diamantblock");
        defaultItems.add(Material.GOLD_BLOCK.name() + ";" + Double.valueOf(0.5) + ";Goldblock");
        defaultItems.add(Material.IRON_BLOCK.name() + ";" + Double.valueOf(0.1) + ";Eisenblock");

        cfg.addDefault("SellItems", defaultItems);

        cfg.options().copyDefaults(true);
        saveConfig();
    }
}
