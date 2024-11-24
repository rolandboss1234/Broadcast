package rolandboss1234.hu.broadcast.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import rolandboss1234.hu.broadcast.Broadcast;

public class BroadcastCommand implements CommandExecutor {
    private final Broadcast plugin;

    public BroadcastCommand(Broadcast plugin) {

        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("broadcast") || command.getName().equalsIgnoreCase("bc")) {
            if (sender.hasPermission("broadcast.admin")) {
                String defaultColor = plugin.config.getString("config.default-color");
                String format = plugin.message.getString("message.broadcast.format");
                String message = defaultColor + String.join(" ", args);
                String finalMessage = format.replace("%message%", message);

                if (args.length > 0) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        plugin.getServer().broadcastMessage(plugin.replacePlaceholders(finalMessage, null, null));
                        player.sendMessage(plugin.replacePlaceholders(plugin.message.getString("message.broadcast.success-send"), null, null));
                        plugin.getLogger().info(player.getName() + " : " + plugin.replacePlaceholders(plugin.message.getString("message.broadcast.success-send"), null, null));
                    } else if (sender instanceof ConsoleCommandSender) {
                        plugin.getServer().broadcastMessage(plugin.replacePlaceholders(finalMessage, null, null));
                    }
                    return true;
                } else {
                    sender.sendMessage(plugin.replacePlaceholders(plugin.message.getString("message.usage"), "/broadcast <messages>", "/broadcast <üzenet>"));
                    return false;
                }
            } else {
                sender.sendMessage(plugin.replacePlaceholders(plugin.message.getString("message.no-permissions"), "broadcast.admin", "broadcast.admin"));
                return false;
            }
        }
        return false;
    }
}