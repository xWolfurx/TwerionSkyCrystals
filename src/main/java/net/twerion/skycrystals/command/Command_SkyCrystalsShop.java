package net.twerion.skycrystals.command;

import net.twerion.skycrystals.SkyCrystals;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_SkyCrystalsShop implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(sender instanceof Player) {

            Player player = (Player)sender;

            if(args.length == 0) {
                player.openInventory(SkyCrystals.getInstance().getShopManager().buildShopInventory(player));
            } else {
                player.sendMessage(SkyCrystals.getInstance().getFileManager().getConfigFile().getPrefix() + "§cVerwendung: §b/" + label);
            }
        } else {
            sender.sendMessage(SkyCrystals.getInstance().getFileManager().getConfigFile().getPrefix() + SkyCrystals.getInstance().getFileManager().getConfigFile().getWrongCommandExecutor());
        }
        return false;
    }
}
