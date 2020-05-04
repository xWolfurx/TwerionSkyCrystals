package net.twerion.skycrystals.event;

import net.twerion.skycrystals.SkyCrystals;
import net.twerion.skycrystals.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Event_PlayerInteractEvent implements Listener {

    public Event_PlayerInteractEvent() {
        Bukkit.getPluginManager().registerEvents(this, SkyCrystals.getInstance());
    }

    @EventHandler
    public void on(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getItem();

        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = e.getClickedBlock();
            if(!(block.getType() == Material.SPAWNER)) return;

            if(item != null && item.getType() != Material.AIR) {
                if((item.getType() == SkyCrystals.getInstance().getFileManager().getConfigFile().getIronGolemMaterial()) && item.getItemMeta().hasDisplayName()) {
                    if(item.getItemMeta().getDisplayName().equals(SkyCrystals.getInstance().getFileManager().getConfigFile().getIronGolemDisplayName())) {
                        CreatureSpawner creatureSpawner = (CreatureSpawner)block.getState();
                        creatureSpawner.setSpawnedType(EntityType.IRON_GOLEM);
                        creatureSpawner.update();
                        SkyCrystals.getInstance().getManager().removeItemsExactly(player.getInventory(), item, 1);
                    }
                }
            }
        }

        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(item != null && item.getType() != Material.AIR) {
                if(item.getType() == Material.NETHER_STAR && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                    if(item.getItemMeta().getDisplayName().equals("§b§lSkyCrystals §7§o<Rechtsklick>")) {
                        e.setCancelled(true);
                        String[] loreArray = item.getItemMeta().getLore().get(0).split(" §8➥ §7Wert §8» §a");
                        String lore = loreArray[1].substring(0, loreArray[1].length() - 1);

                        double toAdd = 0.0D;

                        try {
                            toAdd = Double.parseDouble(lore);
                            if(toAdd <= 0) {
                                throw new NumberFormatException();
                            }
                        } catch (NumberFormatException ex) {
                            player.sendMessage(SkyCrystals.getInstance().getFileManager().getConfigFile().getPrefix() + "§cEs trat ein Fehler beim Einzahlen der SkyCrystals auf.");
                            return;
                        }

                        PlayerData playerData = SkyCrystals.getInstance().getPlayerDataManager().getPlayerData(player);
                        playerData.addCrystals(toAdd);
                        player.sendMessage(SkyCrystals.getInstance().getFileManager().getConfigFile().getPrefix() + "§7Deinem Konto wurden §a" + toAdd + SkyCrystals.getInstance().getFileManager().getConfigFile().getCurrency() + " §7gutgeschrieben.");

                        if(item.getAmount() > 1) {
                            item.setAmount(item.getAmount() - 1);
                        } else {
                            player.setItemInHand(new ItemStack(Material.AIR));
                        }
                        player.updateInventory();

                        return;
                    }
                }
            }
        }
    }
}
