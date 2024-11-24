package rolandboss1234.hu.broadcast;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import rolandboss1234.hu.broadcast.commands.BroadcastCommand;
import rolandboss1234.hu.broadcast.commands.ReloadCommand;
import rolandboss1234.hu.broadcast.utils.ColorUtils;

import java.io.File;

public final class Broadcast extends JavaPlugin implements Listener {
    private String configFile = "config.yml";

    public FileConfiguration config;
    public FileConfiguration message;

    public ColorUtils colorUtils = new ColorUtils(this);

    @Override
    public void onEnable() {
        String language = null;
        if (config != null) {
            language = config.getString("config.language", null);
        }
        if (language == null) {
            language = getConfig().getString("config.language", "en_US");
        }

        if (language.equals("en_US")) {
            getLogger().info("Plugin is starting...");
            getLogger().info("Detected server version: " + Bukkit.getVersion());
            getLogger().info("Loading configurations, messages, and protocols...");
        } else if (language.equals("hu_HU")) {
            getLogger().info("Bővítmény indítása folyamatban...");
            getLogger().info("Észlelt szerver verzió: " + Bukkit.getVersion());
            getLogger().info("Konfigurációk, üzenetek és protokolok betöltése...");
        }

        getConfig().options().copyDefaults(true);
        loadConfigs();
        loadMessages();
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(colorUtils, this);

        getCommand("reload").setExecutor(new ReloadCommand(this));
        getCommand("rl").setExecutor(new ReloadCommand(this));
        getCommand("broadcast").setExecutor(new BroadcastCommand(this));
        getCommand("bc").setExecutor(new BroadcastCommand(this));

        if (language.equals("en_US")) {
            getLogger().info("Plugin successfully loaded.");
        } else if (language.equals("hu_HU")) {
            getLogger().info("A bővítmény sikeresen betöltődött.");
        }
    }

    @Override
    public void onDisable() {
        String language = null;
        if (config != null) {
            language = config.getString("config.language", null);
        }
        if (language == null) {
            language = getConfig().getString("config.language", "en_US");
        }

        if (language.equals("en_US")) {
            getLogger().info("Plugin is shutting down...");
        } else if (language.equals("hu_HU")) {
            getLogger().info("Bővítmény leállása folyamatban...");
        }

        getServer().getScheduler().cancelTasks(this);

        if (language.equals("en_US")) {
            getLogger().info("Plugin successfully stopped.");
        } else if (language.equals("hu_HU")) {
            getLogger().info("A bővítmény sikeresen leállt.");
        }
    }

    public void loadConfigs() {
        getConfig().options().copyDefaults(true);

        File pluginsFolder = new File(this.getDataFolder(), "");
        File configs = new File(pluginsFolder, configFile);

        if (!pluginsFolder.exists()) {
            pluginsFolder.mkdir();
        }

        loadConfig(configFile);
        this.config = YamlConfiguration.loadConfiguration(configs);
    }

    public void loadMessages() {
        String language = null;
        if (config != null) {
            language = config.getString("config.language", null);
        }
        if (language == null) {
            language = getConfig().getString("config.language", "en_US");
        }

        getConfig().options().copyDefaults(true);

        File pluginsFolder = new File(this.getDataFolder(), "");

        String messagesFile = ("message-" + language + ".yml");

        File messages = new File(pluginsFolder, messagesFile);

        if (!pluginsFolder.exists()) {
            pluginsFolder.mkdir();
        }

        loadConfig(messagesFile);
        this.message = YamlConfiguration.loadConfiguration(messages);
    }

    private void loadConfig(String fileName) {
        String language = null;
        if (config != null) {
            language = config.getString("config.language", null);
        }
        if (language == null) {
            language = getConfig().getString("config.language", "en_US");
        }

        File file = new File(this.getDataFolder(), fileName);
        if (language.equals("en_US")) {
            if (!file.exists()) {
                try {
                    this.saveResource(fileName, false);
                    getLogger().info("The " + fileName + " file has been successfully created.");
                } catch (Exception e) {
                    getLogger().severe("An error occurred while creating the " + fileName + " file: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else if (language.equals("hu_HU")) {
            if (!file.exists()) {
                try {
                    this.saveResource(fileName, false);
                    getLogger().info("A(z) " + fileName + " fájl sikeresen létrehozva.");
                } catch (Exception e) {
                    getLogger().severe("Hiba történt a(z) " + fileName + " fájl létrehozása során: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    public String replacePlaceholders(String message, String enMore, String huMore) {
        String language = null;
        if (config != null) {
            language = config.getString("config.language", null);
        }
        if (language == null) {
            language = getConfig().getString("config.language", "en_US");
        }

        if (message == null) {
            return null;
        }

        String prefix = config.getString("config.prefix");

        String permission = null;
        String command = null;
        if (language.equals("en_US")) {
            permission = enMore != null && !enMore.isEmpty() ? enMore : config.getString("config.invalid-parameter");
            command = enMore != null && !enMore.isEmpty() ? enMore : config.getString("config.invalid-parameter");
        } else if (language.equals("hu_HU")) {
            permission = huMore != null && !huMore.isEmpty() ? huMore : config.getString("config.invalid-parameter");
            command = huMore != null && !huMore.isEmpty() ? huMore : config.getString("config.invalid-parameter");
        }

        message = message
                .replace("%prefix%", prefix)
                .replace("%permission%", permission)
                .replace("%command%", command);

        return ColorUtils.applyHexColors(message);
    }
}
