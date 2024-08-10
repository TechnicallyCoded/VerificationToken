package com.tcoded.verificationToken;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public final class VerificationToken extends JavaPlugin implements CommandExecutor, Listener {

    private FileConfiguration pinsConfig;
    private File pinsFile;
    private Set<UUID> warnedPlayers;

    @Override
    public void onEnable() {
        // Plugin startup logic
        loadPinsFile();
        this.getCommand("mytoken").setExecutor(this);
        warnedPlayers = new HashSet<>();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        savePinsFile();
    }

    private void loadPinsFile() {
        pinsFile = new File(getDataFolder(), "pins.yml");
        if (!pinsFile.exists()) {
            pinsFile.getParentFile().mkdirs();
            try {
                pinsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        pinsConfig = YamlConfiguration.loadConfiguration(pinsFile);
    }

    private void savePinsFile() {
        if (pinsConfig != null) {
            try {
                pinsConfig.save(pinsFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String generateRandomPin() {
        Random random = new Random();
        int pin = 100000 + random.nextInt(900000);
        return String.valueOf(pin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID playerUUID = player.getUniqueId();
            String pin = pinsConfig.getString(playerUUID.toString());

            if (pin == null) {
                pin = generateRandomPin();
                pinsConfig.set(playerUUID.toString(), pin);
                savePinsFile();
            }

            if (!warnedPlayers.contains(playerUUID)) {
                player.sendMessage(ChatColor.RED + "WARNING: Your token is a private code and sharing it to non-staff will lead to a ban on your account! Run the command again to show the code.");
                warnedPlayers.add(playerUUID);
            } else {
                player.sendMessage(ChatColor.GREEN + "Your secret token is: " + ChatColor.AQUA + pin);
            }
            return true;
        }
        return false;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        warnedPlayers.remove(playerUUID);
    }
}