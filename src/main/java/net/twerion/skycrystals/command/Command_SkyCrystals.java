package net.twerion.skycrystals.command;

import net.twerion.beast.client.api.CloudAPI;
import net.twerion.beast.protocol.types.rank.Rank;
import net.twerion.skycrystals.SkyCrystals;
import net.twerion.skycrystals.database.Callback;
import net.twerion.skycrystals.database.DatabaseUpdate;
import net.twerion.skycrystals.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_SkyCrystals implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(sender instanceof Player) {

            Player player = (Player)sender;
            Rank rank = CloudAPI.getPlayerAPI().getRank(player);

            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("convert")) {
                    player.sendMessage(SkyCrystals.getInstance().getFileManager().getConfigFile().getPrefix() + "§7Alte SkyCrystals werden übertragen...");
                    SkyCrystals.getInstance().getPlayerDataManager().getOldSkyCrystalsAsync(player.getName(), new Callback<Double>() {
                        @Override
                        public void accept(Double crystals) {
                            PlayerData playerData = SkyCrystals.getInstance().getPlayerDataManager().getPlayerData(player);
                            playerData.addCrystals(crystals);
                            SkyCrystals.getInstance().getPlayerDataManager().resetOldSkyCrystals(player.getName());
                            player.sendMessage(SkyCrystals.getInstance().getFileManager().getConfigFile().getPrefix() + "§7Es wurden §a" + crystals + SkyCrystals.getInstance().getFileManager().getConfigFile().getCurrency() + " §7übertragen.");
                        }
                    });
                } else if(args[0].equalsIgnoreCase("open")) {
                    player.openInventory(SkyCrystals.getInstance().getShopManager().buildShopInventory(player));
                } else {
                    String targetPlayerName = args[0];
                    if (Bukkit.getPlayer(targetPlayerName) == null) {
                        final PlayerData targetPlayerData = new PlayerData(targetPlayerName, false);
                        targetPlayerData.addReadyExecutor(new DatabaseUpdate.ReadyExecutor() {
                            @Override
                            public void ready() {
                                player.sendMessage(SkyCrystals.getInstance().getFileManager().getConfigFile().getPrefix() + "§7SkyCrystals von §a" + targetPlayerName + "§7: §a" + targetPlayerData.getCrystals() + SkyCrystals.getInstance().getFileManager().getConfigFile().getCurrency());
                            }
                        });
                    } else {
                        PlayerData targetPlayerData = SkyCrystals.getInstance().getPlayerDataManager().getPlayerData(Bukkit.getPlayer(targetPlayerName));
                        player.sendMessage(SkyCrystals.getInstance().getFileManager().getConfigFile().getPrefix() + "§7SkyCrystals von §a" + targetPlayerName + "§7: §a" + targetPlayerData.getCrystals() + SkyCrystals.getInstance().getFileManager().getConfigFile().getCurrency());
                    }
                }
            } else if(args.length == 3) {
                if(rank.getAccesslevel() >= SkyCrystals.getInstance().getFileManager().getPermissionFile().getAccessLevel("Command.SkyCrystals.Admin")) {
                    final double amount;

                    try {
                        amount = Double.parseDouble(args[2]);
                    } catch (NumberFormatException e) {
                        player.sendMessage(SkyCrystals.getInstance().getFileManager().getConfigFile().getPrefix() + "§cBitte gebe eine gültigen Betrag an.");
                        return false;
                    }

                    if((args[0].equalsIgnoreCase("add") || (args[0].equalsIgnoreCase("remove")) && amount <= 0.0D)) {
                        player.sendMessage(SkyCrystals.getInstance().getFileManager().getConfigFile().getPrefix() + "§cDer Betrag muss größer als §e0" + SkyCrystals.getInstance().getFileManager().getConfigFile().getCurrency() + " §csein.");
                        return false;
                    }

                    if(args[0].equalsIgnoreCase("set") && amount < 0.0D) {
                        player.sendMessage(SkyCrystals.getInstance().getFileManager().getConfigFile().getPrefix() + "§cDer Betrag muss größer oder gleich §e0" + SkyCrystals.getInstance().getFileManager().getConfigFile().getCurrency() + " §csein.");
                        return false;
                    }

                    String targetPlayerName = args[1];

                    if(Bukkit.getPlayer(targetPlayerName) == null) {
                        final PlayerData targetPlayerData = new PlayerData(targetPlayerName, false);
                        targetPlayerData.addReadyExecutor(new DatabaseUpdate.ReadyExecutor() {
                            @Override
                            public void ready() {
                                if(args[0].equalsIgnoreCase("add")) {
                                    targetPlayerData.addCrystals(amount);
                                    player.sendMessage(SkyCrystals.getInstance().getFileManager().getConfigFile().getPrefix() + "§7Dem Spieler '§a" + targetPlayerName + "§7' wurden §a" + amount + "" + SkyCrystals.getInstance().getFileManager().getConfigFile().getCurrency() + " §7hinzugefügt.");
                                } else if(args[0].equalsIgnoreCase("remove")) {
                                    targetPlayerData.removeCrystals(amount);
                                    player.sendMessage(SkyCrystals.getInstance().getFileManager().getConfigFile().getPrefix() + "§7Dem Spieler '§a" + targetPlayerName + "§7' wurden §a" + amount + "" + SkyCrystals.getInstance().getFileManager().getConfigFile().getCurrency() + " §7entfernt.");
                                } else if(args[0].equalsIgnoreCase("set")) {
                                    targetPlayerData.setCrystals(amount);
                                    player.sendMessage(SkyCrystals.getInstance().getFileManager().getConfigFile().getPrefix() + "§7Die SkyCrystals von '§a" + targetPlayerName + "§7' wurden auf §a" + amount + "" + SkyCrystals.getInstance().getFileManager().getConfigFile().getCurrency() + " §7gesetzt.");
                                } else {
                                    player.sendMessage(SkyCrystals.getInstance().getFileManager().getConfigFile().getPrefix() + "§cVerwendung: §b/" + label + " <add|remove|set> <player> <amount>");
                                }
                            }
                        });
                    } else {
                        PlayerData targetPlayerData = SkyCrystals.getInstance().getPlayerDataManager().getPlayerData(Bukkit.getPlayer(targetPlayerName));
                        if(args[0].equalsIgnoreCase("add")) {
                            targetPlayerData.addCrystals(amount);
                            player.sendMessage(SkyCrystals.getInstance().getFileManager().getConfigFile().getPrefix() + "§7Dem Spieler '§a" + targetPlayerName + "§7' wurden §a" + amount + "" + SkyCrystals.getInstance().getFileManager().getConfigFile().getCurrency() + " §7hinzugefügt.");
                        } else if(args[0].equalsIgnoreCase("remove")) {
                            targetPlayerData.removeCrystals(amount);
                            player.sendMessage(SkyCrystals.getInstance().getFileManager().getConfigFile().getPrefix() + "§7Dem Spieler '§a" + targetPlayerName + "§7' wurden §a" + amount + "" + SkyCrystals.getInstance().getFileManager().getConfigFile().getCurrency() + " §7entfernt.");
                        } else if(args[0].equalsIgnoreCase("set")) {
                            targetPlayerData.setCrystals(amount);
                            player.sendMessage(SkyCrystals.getInstance().getFileManager().getConfigFile().getPrefix() + "§7Die SkyCrystals von '§a" + targetPlayerName + "§7' wurden auf §a" + amount + "" + SkyCrystals.getInstance().getFileManager().getConfigFile().getCurrency() + " §7gesetzt.");
                        } else {
                            player.sendMessage(SkyCrystals.getInstance().getFileManager().getConfigFile().getPrefix() + "§cVerwendung: §b/" + label + " <add|remove|set> <player> <amount>");
                        }
                    }
                } else {
                    player.sendMessage(SkyCrystals.getInstance().getFileManager().getConfigFile().getPrefix() + SkyCrystals.getInstance().getFileManager().getConfigFile().getNoPermission());
                }
            } else {
                this.sendHelp(player, rank, label);
            }
        } else {
            sender.sendMessage(SkyCrystals.getInstance().getFileManager().getConfigFile().getPrefix() + SkyCrystals.getInstance().getFileManager().getConfigFile().getWrongCommandExecutor());
        }
        return false;
    }

    private void sendHelp(Player player, Rank rank, String label) {
        player.sendMessage("§b§m-->-----------------§b<< §c§lSkyCrystals §b>>§b§m-----------------<--");
        player.sendMessage(" §8» §7SkyCrystals-Shop öffnen: §a/" + label + " open");
        player.sendMessage(" §8» §7Alte SkyCrystals übertragen: §a/" + label + " convert");
        player.sendMessage(" §8» §7SkyCrystals von einem Spieler: §a/" + label + " <player>");
        if(rank.getAccesslevel() >= SkyCrystals.getInstance().getFileManager().getPermissionFile().getAccessLevel("Command.SkyCrystals.Admin")) {
            player.sendMessage(" §8» §7SkyCrystals setzten: §a/" + label + " set <player> <amount>");
            player.sendMessage(" §8» §7SkyCrystals hinzufügen: §a/" + label + " add <player> <amount>");
            player.sendMessage(" §8» §7SkyCrystals entfernen: §a/" + label + " remove <player> <amount>");
        }
        player.sendMessage("§b§m-->-----------------§b<< §c§lSkyCrystals §b>>§b§m-----------------<--");
    }
}
