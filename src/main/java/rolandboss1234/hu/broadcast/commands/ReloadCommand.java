package rolandboss1234.hu.broadcast.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import rolandboss1234.hu.broadcast.Broadcast;

public class ReloadCommand implements CommandExecutor {
    private final Broadcast plugin;

    public ReloadCommand(Broadcast plugin) {

        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("reload") || command.getName().equalsIgnoreCase("rl")) {
            if (sender.hasPermission("broadcast.admin")) {
                try {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        plugin.loadConfigs();
                        plugin.loadMessages();

                        player.sendMessage(plugin.replacePlaceholders(plugin.message.getString("message.reload.success"), null, null));
                        plugin.getLogger().info(player.getName() + ": " + plugin.replacePlaceholders(plugin.message.getString("message.reload.success"), null, null));
                    } else if (sender instanceof ConsoleCommandSender) {
                        plugin.loadConfigs();
                        plugin.loadMessages();

                        plugin.getLogger().info(plugin.replacePlaceholders(plugin.message.getString("message.reload.success"), null, null));
                    }
                    return true;
                } catch (Exception e) {
                    String errorMessage = e.getMessage();

                    String failedMessage = plugin.message.getString("message.reload.failed");
                    String finalMessage = failedMessage.replace("%errors%", errorMessage);

                    String language = null;
                    if (plugin.config != null) {
                        language = plugin.config.getString("config.language", null);
                    }
                    if (language == null) {
                        language = plugin.getConfig().getString("config.language", "en_US");
                    }

                    if (language.equals("en_US")) {
                        if (e.getMessage().contains("config")) {
                            plugin.getLogger().severe(finalMessage + " Error in the config file: " + errorMessage);
                        } else if (e.getMessage().contains("message")) {
                            plugin.getLogger().severe(finalMessage + " Error in the message file: " + errorMessage);
                        } else if (e.getMessage().contains("emoji")) {
                            plugin.getLogger().severe(finalMessage + " Error in the emoji file: " + errorMessage);
                        } else {
                            plugin.getLogger().severe(finalMessage + " Unknown source: " + errorMessage);
                        }
                    } else if (language.equals("hu_HU")) {
                        if (e.getMessage().contains("config")) {
                            plugin.getLogger().severe(finalMessage + " Hiba a konfigurációs fájlban: " + errorMessage);
                        } else if (e.getMessage().contains("message")) {
                            plugin.getLogger().severe(finalMessage + " Hiba az üzenetfájlban: " + errorMessage);
                        } else if (e.getMessage().contains("emoji")) {
                            plugin.getLogger().severe(finalMessage + " Hiba az emoji fájlban: " + errorMessage);
                        } else {
                            plugin.getLogger().severe(finalMessage + " Ismeretlen forrás: " + errorMessage);
                        }
                    }

                    e.printStackTrace();
                    return false;
                }
            } else {
                sender.sendMessage(plugin.replacePlaceholders(plugin.message.getString("message.no-permissions"), "broadcast.developer", "broadcast.developer"));
                return false;
            }
        }
        return false;
    }
}