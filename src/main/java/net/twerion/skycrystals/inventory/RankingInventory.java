package net.twerion.skycrystals.inventory;

import net.twerion.skycrystals.SkyCrystals;
import net.twerion.skycrystals.other.DefaultItems;
import net.twerion.skycrystals.other.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class RankingInventory {

    public static void openRankingInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, "§cSkyCrystals §8» §bRanking");

        for(int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, DefaultItems.PLACEHOLDER);
        }

        ItemStack crystals = new ItemCreator().material(Material.NETHER_STAR).displayName("§c§lCrystals-Ranking §7§o<Klick>").build();

        inventory.setItem(13, crystals);

        player.openInventory(inventory);
        updateRankingInventory(player, inventory, RankingType.CRYSTALS);
    }

    public static void updateRankingInventory(Player player, Inventory inventory, RankingType rankingType) {
        if(!inventory.getViewers().contains(player)) return;

        for(int i = 29; i <= 42; i++) {
            inventory.setItem(i, DefaultItems.PLACEHOLDER);
        }
        for(int i = 0; i < 9; i++) {
            inventory.getItem(i + 8).removeEnchantment(Enchantment.ARROW_DAMAGE);
        }

        if(rankingType == RankingType.CRYSTALS) {
            inventory.getItem(13).addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
            LinkedHashMap<OfflinePlayer, Double> ranking = new LinkedHashMap<>();
            for(Map.Entry<String, Double> entry : (Iterable<Map.Entry<String, Double>>) SkyCrystals.getInstance().getPlayerDataManager().getRankingManager().getSkyCrystalsRanking().entrySet()) {
                String playerName = entry.getKey();
                double crystals = entry.getValue();
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
                if(offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) continue;

                ranking.put(offlinePlayer, crystals);
            }

            int counter = 0;
            for(Map.Entry<OfflinePlayer, Double> entry : ranking.entrySet()) {
                OfflinePlayer offlinePlayer = entry.getKey();
                double crystals  = entry.getValue();

                ItemStack display = new ItemCreator().material(Material.PLAYER_HEAD).displayName("§a§l" + (counter + 1) + ". §e" + offlinePlayer.getName()).lore(Arrays.asList(new String[] { " ", " §8» §7SkyCrystals: §a" + crystals + SkyCrystals.getInstance().getFileManager().getConfigFile().getCurrency() } )).build();
                SkullMeta skullMeta = (SkullMeta)display.getItemMeta();
                skullMeta.setOwner(offlinePlayer.getName());
                display.setItemMeta(skullMeta);

                if(counter >= 5) {
                    inventory.setItem(29 + counter + 4, display);
                } else {
                    inventory.setItem(29 + counter, display);
                }
                counter++;
            }
        }
    }

    public enum RankingType {

        CRYSTALS;

    }
}
