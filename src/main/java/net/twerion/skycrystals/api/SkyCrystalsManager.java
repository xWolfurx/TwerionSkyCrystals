package net.twerion.skycrystals.api;

import net.twerion.skycrystals.SkyCrystals;
import org.bukkit.entity.Player;


public class SkyCrystalsManager implements ISkyCrystals {

    public SkyCrystalsManager() {
    }

    @Override
    public double getSkyCrystals(Player player) {
        if(player != null) {
            return SkyCrystals.getInstance().getPlayerDataManager().getPlayerData(player).getCrystals();
        }
        return -1.0D;
    }

    @Override
    public boolean addSkyCrystals(Player player, double crystals) {
        if(player != null) {
            SkyCrystals.getInstance().getPlayerDataManager().getPlayerData(player).addCrystals(crystals);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeSkyCrystals(Player player, double crystals) {
        if(player != null) {
            SkyCrystals.getInstance().getPlayerDataManager().getPlayerData(player).removeCrystals(crystals);
            return true;
        }
        return false;
    }

    @Override
    public boolean setSkyCrystals(Player player, double crystals) {
        if(player != null) {
            SkyCrystals.getInstance().getPlayerDataManager().getPlayerData(player).setCrystals(crystals);
            return true;
        }
        return false;
    }


}
