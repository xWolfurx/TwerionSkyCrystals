package net.twerion.skycrystals.other;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemCreator {


    private Material material;
    private Short data;
    private Integer amount;
    private String display;
    private String owner;
    private List<String> lore;
    private Map<Enchantment, Integer> enchantments = new HashMap<>();
    private Potion potion;

    public ItemCreator() {
    }

    public ItemCreator(ItemStack itemstack) {
        this.material = itemstack.getType();
        this.data = Short.valueOf(itemstack.getDurability());
        this.amount = Integer.valueOf(itemstack.getAmount());
        if(itemstack.hasItemMeta()) {
            if(itemstack.getItemMeta().hasDisplayName()) {
                this.display = itemstack.getItemMeta().getDisplayName();
            }
            if(itemstack.getItemMeta().hasLore()) {
                this.lore = itemstack.getItemMeta().getLore();
            }
            if(itemstack.getItemMeta().hasEnchants()) {
                this.enchantments = itemstack.getItemMeta().getEnchants();
            }
            if(itemstack.getType() == Material.PLAYER_HEAD) {
                SkullMeta meta = (SkullMeta)itemstack.getItemMeta();
                this.owner = meta.getOwner();
            }
        }
    }

    public ItemCreator material(Material material) {
        this.material = material;
        return this;
    }

    public ItemCreator data(short data) {
        this.data = Short.valueOf(data);
        return this;
    }

    public ItemCreator amount(int amount) {
        this.amount = Integer.valueOf(amount);
        return this;
    }

    public ItemCreator displayName(String displayName) {
        this.display = displayName;
        return this;
    }

    public ItemCreator lore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemCreator addEnchant(Enchantment enchantment, int level) {
        this.enchantments.put(enchantment, Integer.valueOf(level));
        return this;
    }

    public ItemCreator removeEnchantment(Enchantment enchantment) {
        if(!this.enchantments.containsKey(enchantment)) {
            return this;
        }
        this.enchantments.remove(enchantment);
        return this;
    }

    public ItemCreator owner(String owner) {
        this.owner = owner;
        return this;
    }

    public ItemCreator asPotion(PotionEffect effect, boolean splash) {
        Potion potion = new Potion(PotionType.getByEffect(effect.getType()));
        potion.getEffects().add(effect);
        potion.setLevel(effect.getAmplifier());
        potion.setSplash(splash);
        this.potion = potion;
        return this;
    }

    public Material getMaterial() {
        return this.material;
    }

    public Short getData() {
        return this.data;
    }

    public Integer getAmount() {
        return this.amount;
    }

    public String getDisplayName() {
        return this.display;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return this.enchantments;
    }

    public boolean isPotion() {
        return this.potion != null;
    }

    public Potion getPotion() {
        return this.potion;
    }

    public String getOwner() {
        return this.owner;
    }

    public ItemStack build() {
        ItemStack itemStack = null;
        if((this.material == null) && (this.potion == null)) {
            return null;
        }
        if(this.data == null) {
            this.data = Short.valueOf((short)0);
        }
        if(this.amount == null) {
            this.amount = Integer.valueOf(1);
        }
        if(this.potion != null) {
            itemStack = this.potion.toItemStack(this.amount.intValue());
        } else {
            itemStack = new ItemStack(this.material, this.amount.intValue(), this.data.shortValue());
        }
        if(itemStack.getType() == Material.LEGACY_SKULL_ITEM) {
            if(itemStack.getDurability() == 3) {
                SkullMeta meta = (SkullMeta)itemStack.getItemMeta();
                meta.setOwner(this.owner);
                if((this.display != null) || (this.lore != null)) {
                    if(this.display != null) {
                        meta.setDisplayName(this.display);
                    }
                    if(this.lore != null) {
                        meta.setLore(this.lore);
                    }
                }
                itemStack.setItemMeta(meta);
            }
        } else {
            if((this.display != null) || (this.lore != null)) {
                ItemMeta itemmeta = itemStack.getItemMeta();
                if(this.display != null) {
                    itemmeta.setDisplayName(this.display);
                }
                if(this.lore != null) {
                    itemmeta.setLore(this.lore);
                }
                itemStack.setItemMeta(itemmeta);
            }
        }
        if(this.enchantments.size() > 0) {
            itemStack.addUnsafeEnchantments(this.enchantments);
        }
        return itemStack;
    }

}
