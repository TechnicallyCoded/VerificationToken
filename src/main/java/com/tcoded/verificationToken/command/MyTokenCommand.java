package com.tcoded.verificationToken.command;

import com.tcoded.verificationToken.manager.PinManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public class MyTokenCommand implements CommandExecutor {

    private final PinManager pinManager;
    private final Set<UUID> warnedPlayers;
    private final String warningMessage;
    private final String tokenMessage;

    public MyTokenCommand(PinManager pinManager, Set<UUID> warnedPlayers, String warningMessage, String tokenMessage) {
        this.pinManager = pinManager;
        this.warnedPlayers = warnedPlayers;
        this.warningMessage = warningMessage;
        this.tokenMessage = tokenMessage;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID playerUUID = player.getUniqueId();
            String pin = pinManager.getPin(playerUUID);

            if (!warnedPlayers.contains(playerUUID)) {
                player.sendMessage(warningMessage);
                warnedPlayers.add(playerUUID);
            } else {
                player.sendMessage(tokenMessage.replace("{token}", pin));
            }
            return true;
        }
        return false;
    }
}