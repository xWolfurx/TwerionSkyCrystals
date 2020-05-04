package net.twerion.skycrystals.shop;

import org.bukkit.Material;

public class SellItem implements Comparable<SellItem> {

    private Material material;
    private double price;
    private String betterName;

    public SellItem(Material material, double price, String betterName) {
        this.material = material;
        this.price = price;
        this.betterName = betterName;
    }

    public Material getMaterial() {
        return this.material;
    }

    public double getPrice() {
        return this.price;
    }

    public String getBetterName() {
        return this.betterName;
    }

    @Override
    public int compareTo(SellItem sellItem) {
        return (int)(this.price - sellItem.getPrice());
    }

}
