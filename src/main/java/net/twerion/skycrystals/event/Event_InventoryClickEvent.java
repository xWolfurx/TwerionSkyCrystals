package net.twerion.skycrystals.event;

import net.twerion.skycrystals.SkyCrystals;
import net.twerion.skycrystals.other.Anvil;
import net.twerion.skycrystals.other.ItemCreator;
import net.twerion.skycrystals.player.PlayerData;
import net.twerion.skycrystals.shop.ShopItem;
import net.twerion.skycrystals.transaction.TransactionHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class Event_InventoryClickEvent implements Listener {

    public Event_InventoryClickEvent() {
        Bukkit.getPluginManager().registerEvents(this, SkyCrystals.getInstance());
    }

    @EventHandler
    public void on(InventoryClickEvent e) {
        if(!(e.getWhoClicked() instanceof Player)) {
            return;
        }

        final Player player = (Player)e.getWhoClicked();
        Inventory inv = e.getInventory();
        ItemStack item = e.getCurrentItem();

        if(e.getHotbarButton() != -1) {
            item = e.getView().getBottomInventory().getItem(e.getHotbarButton());
            if(item == null) {
                item = e.getCurrentItem();
            }
        }

        if((item == null) || (item.getType() == Material.AIR)) {
            return;
        }

        if(inv.getType() == InventoryType.ANVIL) {
            if(SkyCrystals.getInstance().getManager().getInAnvil().contains(player)) {
                e.setCancelled(true);
                if(e.getRawSlot() == Anvil.AnvilSlot.OUTPUT.getSlot()) {
                    if(item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
                        player.sendMessage(SkyCrystals.getInstance().getFileManager().getConfigFile().getPrefix() + "§cBitte gebe einen gültigen Betrag an.");
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0F, 1.0F);
                        return;
                    }
                    String name = item.getItemMeta().getDisplayName();
                    double toRemove = 0.0D;

                    try {
                        toRemove = Double.parseDouble(name);
                        if(toRemove <= 0) {
                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException ex) {
                        player.sendMessage(SkyCrystals.getInstance().getFileManager().getConfigFile().getPrefix() + "§cBitte gebe einen gültigen Betrag an.");
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0F, 1.0F);
                        return;
                    }

                    if(toRemove < 1.0D) {
                        player.sendMessage(SkyCrystals.getInstance().getFileManager().getConfigFile().getPrefix() + "§cBitte gebe einen Betrag größer als §e1.0" + SkyCrystals.getInstance().getFileManager().getConfigFile().getCurrency() + " §can.");
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0F, 1.0F);
                        return;
                    }

                    PlayerData playerData = SkyCrystals.getInstance().getPlayerDataManager().getPlayerData(player);
                    if(!playerData.hasEnoughCrystals(toRemove)) {
                        player.sendMessage(SkyCrystals.getInstance().getFileManager().getConfigFile().getPrefix() + "§cDu besitzt nicht genügend SkyCrystals.");
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0F, 1.0F);
                        return;
                    }
                    ItemStack itemStack = new ItemCreator().material(Material.PAPER).displayName("§b§lSkyCrystals §7§o<Rechtsklick>").lore(Arrays.asList(new String[] {" §8➥ §7Wert §8» §a" + toRemove + SkyCrystals.getInstance().getFileManager().getConfigFile().getCurrency() } )).build();
                    playerData.removeCrystals(toRemove);

                    SkyCrystals.getInstance().getManager().addItem(player, itemStack);
                    player.openInventory(SkyCrystals.getInstance().getShopManager().buildShopInventory(player));
                    player.sendMessage(SkyCrystals.getInstance().getFileManager().getConfigFile().getPrefix() + "§7Du hast §a" + toRemove + SkyCrystals.getInstance().getFileManager().getConfigFile().getCurrency() + " §7ausgezahlt.");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);

                    if(SkyCrystals.getInstance().getManager().getInAnvil().contains(player)) {
                        SkyCrystals.getInstance().getManager().getInAnvil().remove(player);
                    }
                    return;
                }
            }
        }

        if(e.getView().getTitle().equals("§cSkyCrystals §8» §bRanking")) {
            e.setCancelled(true);
            return;
        }

        if(e.getView().getTitle().equals("§cSkyCrystals §8» §bShop")) {
            e.setCancelled(true);
            if(e.getRawSlot() == 45) {
                e.getView().close();
                if(!SkyCrystals.getInstance().getManager().getInAnvil().contains(player)) {
                    SkyCrystals.getInstance().getManager().getInAnvil().add(player);
                }
                new Anvil(player).setTitle("§bWithdraw").addItem(Anvil.AnvilSlot.INPUT_LEFT, new ItemCreator().material(Material.PAPER).displayName("Betrag eingeben...").build()).open();
                return;
            }
            if(e.getRawSlot() == 53) {
                SkyCrystals.getInstance().getShopManager().sellInventory(player);
                e.getView().close();
                player.openInventory(SkyCrystals.getInstance().getShopManager().buildShopInventory(player));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 2.0F);
                return;
            }

            int slot = e.getRawSlot();
            if(SkyCrystals.getInstance().getShopManager().getShopItems().containsKey(slot)) {
                ShopItem shopItem = SkyCrystals.getInstance().getShopManager().getShopItems().get(slot);

                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
                TransactionHandler.get().createTransaction(player, new TransactionHandler.TransactionPerformer() {
                    @Override
                    public String getName() {
                        return shopItem.getItemName();
                    }

                    @Override
                    public double getPrice() {
                        return shopItem.getPrice();
                    }

                    @Override
                    public String getNotEnoughCrystalsMessage() {
                        return SkyCrystals.getInstance().getFileManager().getConfigFile().getPrefix() + "§cDu hast nicht genügend SkyCrystals.";
                    }

                    @Override
                    public boolean onBuy(TransactionHandler.Transaction transaction) {
                        ItemStack itemStack = new ItemCreator().material(shopItem.getItemMaterial()).displayName(shopItem.getItemName()).lore(shopItem.getItemLore()).amount(shopItem.getAmount()).build();
                        SkyCrystals.getInstance().getManager().addItem(transaction.getPlayer(), itemStack);
                        player.openInventory(SkyCrystals.getInstance().getShopManager().buildShopInventory(player));
                        return true;
                    }

                    @Override
                    public boolean onCancel(TransactionHandler.Transaction transaction) {
                        transaction.getPlayer().closeInventory();
                        (new BukkitRunnable() {
                            @Override
                            public void run() {
                                player.openInventory(SkyCrystals.getInstance().getShopManager().buildShopInventory(player));
                            }
                        }).runTaskLater(SkyCrystals.getInstance(), 1L);
                        return true;
                    }
                });
            }
        }
    }
}
