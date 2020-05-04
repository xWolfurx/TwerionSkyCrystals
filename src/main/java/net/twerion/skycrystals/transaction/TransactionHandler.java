package net.twerion.skycrystals.transaction;

import net.twerion.skycrystals.SkyCrystals;
import net.twerion.skycrystals.other.DefaultItems;
import net.twerion.skycrystals.other.ItemCreator;
import net.twerion.skycrystals.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionHandler implements Listener {

    private static TransactionHandler transactionHandler = new TransactionHandler();
    private Map<Player, Transaction> transactions;

    public static TransactionHandler get() {
        return transactionHandler;
    }

    public TransactionHandler() {
        this.transactions = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(this, SkyCrystals.getInstance());
    }

    public void createTransaction(Player player, TransactionPerformer performer) {
        if(this.transactions.containsKey(player)) return;
        Transaction transaction = new Transaction(player, performer);
        this.transactions.put(player, transaction);
    }

    public void removeTransaction(Player player) {
        if(!this.transactions.containsKey(player)) return;
        Transaction transaction = this.transactions.get(player);
        if(player.getOpenInventory().getTopInventory().equals(transaction.getInventory())) {
            player.closeInventory();
        }
    }

    public boolean containsTransaction(Player player) {
        return this.transactions.containsKey(player);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(!(e.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player)e.getWhoClicked();
        Inventory inventory = e.getInventory();
        ItemStack itemStack = e.getCurrentItem();

        if(e.getHotbarButton() != -1) {
            itemStack = e.getView().getBottomInventory().getItem(e.getHotbarButton());
            if(itemStack == null) {
                itemStack =e.getCurrentItem();
            }
        }

        if((itemStack == null) || (itemStack.getType() == Material.AIR)) {
            return;
        }

        if(e.getView().getTitle() != null && e.getView().getTitle().equals("§cSkyCrystals §8» §bTransaktion")) {
            e.setCancelled(true);
            if(!this.containsTransaction(player)) {
                e.getView().close();
                player.sendMessage("§c§lTRANSAKTION §8» §cDu befindest dich in keiner Transaktion.");
                return;
            }
            Transaction transaction = this.transactions.get(player);
            if(e.getRawSlot() == 11) {
                PlayerData playerData = SkyCrystals.getInstance().getPlayerDataManager().getPlayerData(player);
                if(playerData.getCrystals() < transaction.getPerformer().getPrice()) {
                    player.sendMessage(transaction.getPerformer().getNotEnoughCrystalsMessage().replace("%PRICE%", transaction.getPerformer().getPrice() + "" + SkyCrystals.getInstance().getFileManager().getConfigFile().getCurrency()));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0F, 1.0F);
                    return;
                }
                this.transactions.remove(player);
                boolean success = transaction.getPerformer().onBuy(transaction);
                if(success) {
                    playerData.removeCrystals(transaction.getPerformer().getPrice());
                    player.sendMessage("§c§lTRANSAKTION §8» §aKauf erfolgreich.");
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 2.0F);
                } else {
                    this.transactions.put(player, transaction);
                }
            }
            if(e.getRawSlot() == 15) {
                this.transactions.remove(player);
                boolean success = transaction.getPerformer().onCancel(transaction);
                if(success) {
                    player.sendMessage("§c§lTRANSAKTION §8» §cKauf abgebrochen.");
                    player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_DEATH, 1.0F, 0.5F);
                } else {
                    this.transactions.put(player, transaction);
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if(!(e.getPlayer() instanceof Player)) {
            return;
        }

        Player player = (Player)e.getPlayer();

        if(e.getInventory().getType() != InventoryType.ANVIL) {
            if (e.getView().getTitle().equals("§cSkyCrystals §8» §bTransaktion")) {
                if (!this.containsTransaction(player)) {
                    return;
                }
                Transaction transaction = this.transactions.get(player);
                this.transactions.remove(player);
                transaction.getPerformer().onCancel(transaction);
            }
        }
    }

    public class Transaction {

        private Player player;
        private TransactionHandler.TransactionPerformer performer;
        private Inventory inventory;

        public Transaction(Player player, TransactionHandler.TransactionPerformer performer) {
            this.player = player;
            this.performer = performer;
            this.buildInventory();
            this.player.openInventory(this.inventory);
        }

        public Player getPlayer() {
            return this.player;
        }

        public TransactionHandler.TransactionPerformer getPerformer() {
            return this.performer;
        }

        public Inventory getInventory() {
            return this.inventory;
        }

        private void buildInventory() {
            this.inventory = Bukkit.createInventory(null, 27, "§cSkyCrystals §8» §bTransaktion");

            for(int i = 0; i < this.inventory.getSize(); i++) {
                this.inventory.setItem(i, DefaultItems.PLACEHOLDER);
            }

            ItemCreator itemCreator = new ItemCreator(DefaultItems.TRANSACTION_DETAILS);
            List<String> lore = itemCreator.getLore();

            for(int i = 0; i < lore.size(); i++) {
                String loreStr = lore.get(i);
                lore.remove(i);
                loreStr = loreStr.replace("%NAME%", this.performer.getName()).replace("%PRICE%", this.performer.getPrice() + "" + SkyCrystals.getInstance().getFileManager().getConfigFile().getCurrency());
                lore.add(i, loreStr);
            }
            itemCreator.lore(lore);

            this.inventory.setItem(11, DefaultItems.CONFIRM_TRANSACTION);
            this.inventory.setItem(13, itemCreator.build());
            this.inventory.setItem(15, DefaultItems.CANCEL_TRANSACTION);
        }

    }

    public interface TransactionPerformer {

        String getName();

        double getPrice();

        String getNotEnoughCrystalsMessage();

        boolean onBuy(TransactionHandler.Transaction paramTransaction);

        boolean onCancel(TransactionHandler.Transaction paramTransaction);

    }
}
