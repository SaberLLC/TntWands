package pw.saber.tntwands.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pw.saber.tntwands.TntWands;
import pw.saber.tntwands.util.CC;
import pw.saber.tntwands.util.ItemCreation;

public class CommandTntWands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("tntwands.admin")) {
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("give")) {
                    try {
                        int amount = Integer.parseInt(args[2]);
                        Player player = Bukkit.getPlayer(args[1]);
                        if (player == null) {
                            sender.sendMessage(CC.translate(TntWands.getInstance().getConfig().getString("Messages.Player-Offline").replace("{player}", args[1])));
                            return false;
                        }
                        player.getInventory().addItem(ItemCreation.createTntWand(amount));
                        player.sendMessage(CC.translate(TntWands.getInstance().getConfig().getString("Messages.TntWand-Received").replace("{uses}", amount + "")));
                    } catch (NumberFormatException e) {
                        sender.sendMessage(CC.translate(TntWands.getInstance().getConfig().getString("Messages.Must-Be-Number")));

                    }
                }
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    TntWands.getInstance().reloadConfig();
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("give")) {
                    if (sender.hasPermission("tntwands.admin")) {
                        Player player = Bukkit.getPlayer(args[1]);
                        if (player == null) {
                            sender.sendMessage(CC.translate(TntWands.getInstance().getConfig().getString("Messages.Player-Offline").replace("{player}", args[1])));
                            return false;
                        }
                        player.getInventory().addItem(ItemCreation.createTntWand(-1));
                        player.sendMessage(CC.translate(TntWands.getInstance().getConfig().getString("Messages.TntWand-Received-Infinite")));
                    }
                }
            } else {
                sender.sendMessage(CC.translate(TntWands.getInstance().getConfig().getString("Messages.Command-Usage")));
            }
        } else {
            sender.sendMessage(CC.translate("&cAn internal error occurred while attempting to perform this command"));
        }
        return false;
    }
}
