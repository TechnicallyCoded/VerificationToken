package com.tcoded.verificationToken.command;

import com.tcoded.verificationToken.manager.PinManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CheckTokenCommand implements CommandExecutor {

    private final PinManager pinManager;

    public CheckTokenCommand(PinManager pinManager) {
        this.pinManager = pinManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /checktoken <token>");
            return false;
        }

        String token = args[0];
        UUID playerUUID = pinManager.getUUIDFromToken(token);

        if (playerUUID != null) {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player != null) {
                sendPlayerDetails(sender, player.getName(), playerUUID);
            } else {
                sendPlayerDetails(sender, "Unknown", playerUUID);
            }
        } else {
            sendNotFoundMessage(sender, token);
        }

        return true;
    }



    private void sendPlayerDetails(CommandSender sender, String playerName, UUID playerUUID) {
        Component message = Component.text("Found player matching the token provided:", NamedTextColor.DARK_GREEN);
        sender.sendMessage(message);

        Component ignMessage = Component.text("IGN: ", NamedTextColor.GREEN)
                .append(Component.text(playerName, NamedTextColor.WHITE)
                        .clickEvent(ClickEvent.copyToClipboard(playerName))
                        .hoverEvent(Component.text("Click to copy!", NamedTextColor.GREEN)));
        sender.sendMessage(ignMessage);

        Component uuidMessage = Component.text("UUID: ", NamedTextColor.GREEN)
                .append(Component.text(playerUUID.toString(), NamedTextColor.WHITE)
                        .clickEvent(ClickEvent.copyToClipboard(playerUUID.toString()))
                        .hoverEvent(Component.text("Click to copy!", NamedTextColor.GREEN)));
        sender.sendMessage(uuidMessage);
    }

    private void sendNotFoundMessage(CommandSender sender, String token) {
        sender.sendMessage(ChatColor.RED + "Could not find a player matching the token: " + token);
    }
}