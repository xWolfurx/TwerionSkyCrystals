package net.twerion.skycrystals.api;

import net.twerion.skycrystals.database.Callback;
import org.bukkit.entity.Player;

public interface ISkyCrystals {

    double getSkyCrystals(Player paramPlayer);

    boolean addSkyCrystals(Player paramPlayer, double paramDouble);

    boolean removeSkyCrystals(Player paramPlayer, double paramDouble);

    boolean setSkyCrystals(Player paramPlayer, double paramDouble);
}
