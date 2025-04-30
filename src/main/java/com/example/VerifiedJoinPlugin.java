package com.example.verifiedjoin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class VerifiedJoinPlugin extends JavaPlugin implements Listener, CommandExecutor {

    @Override
    public void onEnable() {
        // Register the event
        Bukkit.getPluginManager().registerEvents(this, this);
        
        // Register the /discordlink command
        this.getCommand("discordlink").setExecutor(this);

        // Create the default config.yml if it doesn't exist
        this.saveDefaultConfig();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Skip freezing if the player is an operator (admin)
        if (!player.isOp()) {
            // Freezing the player (disabling movement)
            player.setWalkSpeed(0); // Freezes the player by setting walk speed to 0
            player.setAllowFlight(true); // Allow flying (to prevent falling or accidental movement)
        }

        // Display the verification message on the player's screen
        player.sendTitle("Account Verification", "Join our Discord for more info!", 10, 70, 20);

        // Message in the chat
        player.sendMessage("§7To start playing, you need to join our Discord server and link your Minecraft account with Discord!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("discordlink")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                // Check if the player is an admin (OP)
                if (player.isOp()) {
                    if (args.length == 1) {
                        String newInviteLink = args[0];

                        // Validate the URL format
                        if (newInviteLink.startsWith("https://discord.gg/")) {
                            // Save the new invite link to the config
                            getConfig().set("discordInvite", newInviteLink);
                            saveConfig();

                            player.sendMessage("§7The Discord invite link has been updated to: " + newInviteLink);
                        } else {
                            player.sendMessage("§cInvalid Discord invite link. Please ensure it starts with 'https://discord.gg/'");
                        }
                    } else {
                        player.sendMessage("§cUsage: /discordlink <new invite link>");
                    }
                } else {
                    player.sendMessage("§cYou don't have permission to use this command.");
                }
            } else {
                sender.sendMessage("§cOnly players can use this command.");
            }
            return true;
        }
        return false;
    }

    @Override
    public void onDisable() {
        // Any cleanup logic, if necessary
    }
}
