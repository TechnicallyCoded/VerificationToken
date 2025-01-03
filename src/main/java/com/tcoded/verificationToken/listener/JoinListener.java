package com.tcoded.verificationToken.listener;

import com.tcoded.verificationToken.VerificationToken;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Set;
import java.util.UUID;

public class JoinListener implements Listener {

    private final Set<UUID> warnedPlayers;
    private final VerificationToken plugin;

    public JoinListener(VerificationToken plugin) {
        this.plugin = plugin;
        this.warnedPlayers = plugin.getWarnedPlayers();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        warnedPlayers.remove(playerUUID);
    }
}