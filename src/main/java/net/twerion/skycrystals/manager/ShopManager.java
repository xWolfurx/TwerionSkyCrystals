package net.twerion.skycrystals.manager;


import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.NBTTagList;
import net.minecraft.server.v1_15_R1.NBTTagString;
import net.twerion.skycrystals.SkyCrystals;
import net.twerion.skycrystals.other.DefaultItems;
import net.twerion.skycrystals.other.ItemCreator;
import net.twerion.skycrystals.player.PlayerData;
import net.twerion.skycrystals.shop.SellItem;
import net.twerion.skycrystals.shop.ShopItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ShopManager {

    private Map<Integer, ShopItem> shopItems;
    private Map<Material, SellItem> sellItems;

    public ShopManager() {
        this.shopItems = new HashMap<>();
        this.sellItems = new HashMap<>();
        this.loadShopItems();
        this.loadSellItems();
    }

    private void loadShopItems() {
        this.shopItems.clear();

        FileConfiguration cfg = SkyCrystals.getInstance().getFileManager().getShopItemFile().getConfig();
        if(cfg.get("ShopItem") == null) return;

        for(String strSlot : cfg.getConfigurationSection("ShopItem").getKeys(false)) {
            int slot = Integer.valueOf(strSlot);
            Material displayMaterial = Material.matchMaterial(cfg.getString("ShopItem." + slot + ".DisplayMaterial"));
            String displayName = cfg.getString("ShopItem." + slot + ".DisplayName");
            List<String> displayLore = cfg.getStringList("ShopItem." + slot + ".DisplayLore");
            String skinId = cfg.getString("ShopItem." + slot + ".SkinId");
            String skinTexture = cfg.getString("ShopItem." + slot + ".SkinTexture");
            double price = cfg.getDouble("ShopItem." + slot + ".Price");
            int amount = cfg.getInt("ShopItem." + slot + ".Amount");
            Material itemMaterial = Material.matchMaterial(cfg.getString("ShopItem." + slot + ".ItemMaterial"));
            String itemName = cfg.getString("ShopItem." + slot + ".ItemName");
            List<String> itemLore = cfg.getStringList("ShopItem." + slot + ".ItemLore");

            ShopItem shopItem = new ShopItem(slot, displayMaterial, displayName, displayLore, skinId, skinTexture, price, amount, itemMaterial, itemName, itemLore);
            this.shopItems.put(slot, shopItem);
        }
        SkyCrystals.getInstance().getLogger().info("Loaded " + shopItems.size() + " ShopItems.");
    }

    private void loadSellItems() {
        this.sellItems.clear();

        FileConfiguration cfg = SkyCrystals.getInstance().getFileManager().getSellItemFile().getConfig();
        if(cfg.get("SellItems") == null) return;

        for(String itemString : cfg.getStringList("SellItems")) {
            String[] itemArray = itemString.split(";");

            Material material = null;
            double price = 0.0D;
            String betterName = "";

            try {
                material = Material.matchMaterial(itemArray[0]);
                price = Double.parseDouble(itemArray[1]);
                betterName = itemArray[2];
            } catch (Exception e) {
                SkyCrystals.getInstance().getLogger().warning("An error occurred while reading sell items: " + e.toString());
                continue;
            }

            SellItem sellItem = new SellItem(material, price, betterName);
            if(!this.sellItems.containsKey(material)) {
                this.sellItems.put(material, sellItem);
            }
        }
        SkyCrystals.getInstance().getLogger().info("Loaded " + this.sellItems.size() + " SellItems.");
    }

    public Inventory buildShopInventory(Player player) {
        PlayerData playerData = SkyCrystals.getInstance().getPlayerDataManager().getPlayerData(player);

        Inventory shopInventory = Bukkit.createInventory(null, 54, "§cSkyCrystals §8» §bShop");

        for(int i = 0; i < shopInventory.getSize(); i++) {
            shopInventory.setItem(i, DefaultItems.PLACEHOLDER);
        }

        ItemStack currentCrystals = DefaultItems.CURRENT_CRYSTALS.clone();
        ItemMeta itemMeta = currentCrystals.getItemMeta();
        itemMeta.setDisplayName(itemMeta.getDisplayName().replaceAll("%AMOUNT%", playerData.getCrystals() + SkyCrystals.getInstance().getFileManager().getConfigFile().getCurrency()));
        currentCrystals.setItemMeta(itemMeta);

        ItemStack sellBlocks = DefaultItems.SELL_BLOCKS.clone();
        List<String> sellItemLore = new ArrayList<>();
        sellItemLore.add(" ");

        List<SellItem> sellItemsList = new ArrayList<>(this.getSellItems().values());
        Collections.sort(sellItemsList);

        for(SellItem sellItem : sellItemsList) {
            sellItemLore.add(" §8➥ " + sellItem.getBetterName() + "§7: §a" + sellItem.getPrice() + SkyCrystals.getInstance().getFileManager().getConfigFile().getCurrency());
        }

        ItemMeta sellItemMeta = sellBlocks.getItemMeta();
        sellItemMeta.setLore(sellItemLore);
        sellBlocks.setItemMeta(sellItemMeta);

        shopInventory.setItem(45, DefaultItems.WITH_DRAW);
        shopInventory.setItem(49, currentCrystals);
        shopInventory.setItem(53, sellBlocks);

        for(int slot : this.shopItems.keySet()) {
            ShopItem shopItem = this.shopItems.get(slot);

            List<String> lore = shopItem.getDisplayLore();
            lore.replaceAll(str -> str.replaceAll("%PRICE%", shopItem.getPrice() + SkyCrystals.getInstance().getFileManager().getConfigFile().getCurrency()));

            ItemStack shopItemStack = new ItemCreator().material(shopItem.getDisplayMaterial()).displayName(shopItem.getDisplayName()).lore(lore).build();

            if(shopItemStack.getType() == Material.PLAYER_HEAD) {
                net.minecraft.server.v1_15_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(shopItemStack);
                NBTTagCompound compound = nmsItemStack.getTag();

                if(compound == null) {
                    compound = new NBTTagCompound();
                    nmsItemStack.setTag(compound);
                    compound = nmsItemStack.getTag();
                }

                NBTTagCompound skullOwner = new NBTTagCompound();
                skullOwner.set("Id", NBTTagString.a(shopItem.getSkinId()));
                NBTTagCompound properties = new NBTTagCompound();
                NBTTagList textures = new NBTTagList();
                NBTTagCompound value = new NBTTagCompound();
                value.set("Value", NBTTagString.a(shopItem.getSkinTexture()));
                textures.add(value);
                properties.set("textures", textures);
                skullOwner.set("Properties", properties);

                compound.set("SkullOwner", skullOwner);
                nmsItemStack.setTag(compound);

                shopItemStack = CraftItemStack.asBukkitCopy(nmsItemStack);
            }

            shopInventory.setItem(slot, shopItemStack);
        }
        return shopInventory;
    }

    public void sellInventory(Player player) {
        PlayerData playerData = SkyCrystals.getInstance().getPlayerDataManager().getPlayerData(player);
        double amount = 0.0D;
        for(ItemStack itemStack : player.getInventory().getContents()) {
            if(itemStack != null && itemStack.getType() != Material.AIR) {
                if(this.sellItems.containsKey(itemStack.getType())) {
                    SellItem sellItem = this.sellItems.get(itemStack.getType());
                    int availableItems = SkyCrystals.getInstance().getManager().getAvailableItems(player.getInventory(), itemStack);
                    if (SkyCrystals.getInstance().getManager().removeItems(player.getInventory(), itemStack, availableItems)) {
                        amount += availableItems * sellItem.getPrice();
                    } else {
                        player.sendMessage(SkyCrystals.getInstance().getFileManager().getConfigFile().getPrefix() + "§cEs trat ein Fehler beim Verkaufen der Items auf.");
                        break;
                    }
                }
            }
        }
        if(amount > 0.0D) {
            playerData.addCrystals(amount);
            player.sendMessage(SkyCrystals.getInstance().getFileManager().getConfigFile().getPrefix() + "§7Du hast deine Items für §a" + amount + SkyCrystals.getInstance().getFileManager().getConfigFile().getCurrency() + " §7verkauft.");
        } else {
            player.sendMessage(SkyCrystals.getInstance().getFileManager().getConfigFile().getPrefix() + "§cDu hast keine Items zum Verkaufen im Inventar.");
        }
    }

    public Map<Material, SellItem> getSellItems() {
        return this.sellItems;
    }

    public Map<Integer, ShopItem> getShopItems() {
        return this.shopItems;
    }
}
