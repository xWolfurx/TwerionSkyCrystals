package net.twerion.skycrystals.shop;

import org.bukkit.Material;

import java.util.List;

public class ShopItem {

    private int slot;
    private Material displayMaterial;
    private String displayName;
    private List<String> displayLore;

    //if displayMaterial = PLAYER_HEAD
    private String skinId;
    private String skinTexture;

    private double price;
    private int amount;

    private Material itemMaterial;
    private String itemName;
    private List<String> itemLore;

    public ShopItem(int slot, Material displayMaterial, String displayName, List<String> displayLore, String skinId, String skinTexture, double price, int amount, Material itemMaterial, String itemName, List<String> itemLore) {
        this.slot = slot;
        this.displayMaterial = displayMaterial;
        this.displayName = displayName;
        this.displayLore = displayLore;
        this.skinId = skinId;
        this.skinTexture = skinTexture;
        this.price = price;
        this.amount = amount;
        this.itemMaterial = itemMaterial;
        this.itemName = itemName;
        this.itemLore = itemLore;
    }

    public int getSlot() {
        return this.slot;
    }

    public Material getDisplayMaterial() {
        return this.displayMaterial;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public List<String> getDisplayLore() {
        return this.displayLore;
    }

    public String getSkinId() {
        return this.skinId;
    }

    public String getSkinTexture() {
        return this.skinTexture;
    }

    public double getPrice() {
        return this.price;
    }

    public int getAmount() {
        return this.amount;
    }

    public Material getItemMaterial() {
        return this.itemMaterial;
    }

    public String getItemName() {
        return this.itemName;
    }

    public List<String> getItemLore() {
        return this.itemLore;
    }


}
