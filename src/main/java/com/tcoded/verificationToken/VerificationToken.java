package com.tcoded.verificationToken;

import com.tcoded.verificationToken.command.CheckTokenCommand;
import com.tcoded.verificationToken.command.MyTokenCommand;
import com.tcoded.verificationToken.listener.JoinListener;
import com.tcoded.verificationToken.manager.PinManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class VerificationToken extends JavaPlugin {

    private PinManager pinManager;
    private Set<UUID> warnedPlayers;

    private String warningMessage;
    private String tokenMessage;

    @Override
    public void onEnable() {
        // Plugin startup logic
        pinManager = new PinManager(getDataFolder());
        loadConfig();
        warnedPlayers = new HashSet<>();

        MyTokenCommand myTokenCmd = new MyTokenCommand(pinManager, warnedPlayers, warningMessage, tokenMessage);
        CheckTokenCommand checkTokenCmd = new CheckTokenCommand(pinManager);
        this.getCommand("mytoken").setExecutor(myTokenCmd);
        this.getCommand("checktoken").setExecutor(checkTokenCmd);

        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        pinManager.savePinsFile();
    }

    private void loadConfig() {
        saveDefaultConfig();
        warningMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.warning"));
        tokenMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.token"));
    }

    public Set<UUID> getWarnedPlayers() {
        return warnedPlayers;
    }

}