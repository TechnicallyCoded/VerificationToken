package com.tcoded.verificationToken.manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

public class PinManager {

    private final File pinsFile;
    private final FileConfiguration pinsConfig;

    public PinManager(File dataFolder) {
        pinsFile = new File(dataFolder, "pins.yml");
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

    public String getPin(UUID playerUUID) {
        String pin = pinsConfig.getString(playerUUID.toString());
        if (pin == null) {
            pin = generateRandomPin();
            pinsConfig.set(playerUUID.toString(), pin);
            savePinsFile();
        }
        return pin;
    }

    private String generateRandomPin() {
        Random random = new Random();
        int pin = 100000 + random.nextInt(900000);
        return String.valueOf(pin);
    }

    public void savePinsFile() {
        try {
            pinsConfig.save(pinsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UUID getUUIDFromToken(String token) {
        for (String key : pinsConfig.getKeys(false)) {
            if (pinsConfig.getString(key).equals(token)) {
                return UUID.fromString(key);
            }
        }
        return null;
    }

}