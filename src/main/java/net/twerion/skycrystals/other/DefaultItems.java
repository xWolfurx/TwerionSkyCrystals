package net.twerion.skycrystals.other;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class DefaultItems {

    public static ItemStack PLACEHOLDER = new ItemCreator().material(Material.BLACK_STAINED_GLASS_PANE).displayName(" ").build();
    public static ItemStack NOTHING = new ItemCreator().material(Material.AIR).build();

    public static final ItemStack CONFIRM_TRANSACTION = new ItemCreator().material(Material.GREEN_STAINED_GLASS_PANE).displayName("§a§lBestätigen §7§o<Klick>").build();
    public static final ItemStack CANCEL_TRANSACTION = new ItemCreator().material(Material.RED_STAINED_GLASS_PANE).displayName("§c§lAbbrechen §7§o<Klick>").build();
    public static final ItemStack TRANSACTION_DETAILS = new ItemCreator().material(Material.OAK_SIGN).displayName("§b§lDetails").lore(Arrays.asList(new String[] {" ", " §8» §7Name: §e%NAME%", " §8» §7Preis: §e%PRICE%" })).build();

    public static final ItemStack CURRENT_CRYSTALS = new ItemCreator().material(Material.PAPER).displayName("§b§lDeine SkyCrystals: §a§l%AMOUNT%").build();
    public static final ItemStack SELL_BLOCKS = new ItemCreator().material(Material.DIAMOND).displayName("§b§lAlle Blöcke verkaufen §7§o<Klick>").build();
    public static final ItemStack WITH_DRAW = new ItemCreator().material(Material.NETHER_STAR).displayName("§b§lSkyCrystals auszahlen §7§o<Klick>").build();

}
