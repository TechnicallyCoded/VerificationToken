package com.tcoded.verificationToken;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

public final class VerificationToken extends JavaPlugin implements CommandExecutor {

    private FileConfiguration pinsConfig;
    private File pinsFile;

    @Override
    public void onEnable() {
        // Plugin startup logic
        loadPinsFile();
        this.getCommand("mytoken").setExecutor(this);
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
            saveResource("pins.yml", false);
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

            player.sendMessage(ChatColor.GREEN + "Your secret token is: " + ChatColor.AQUA + pin);
            return true;
        }
        return false;
    }
}