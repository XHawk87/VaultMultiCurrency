/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.xhawk87.VaultMultiCurrency.commands;

import me.xhawk87.VaultMultiCurrency.VaultMultiCurrency;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author XHawk87
 */
public class SetEconomyCommand implements CommandExecutor {

    private VaultMultiCurrency plugin;

    public SetEconomyCommand(VaultMultiCurrency plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args.length == 1 && args[0].equalsIgnoreCase("help")) {
            sender.sendMessage("/SetEconomy ([default|(p:)player|(w:)world]) [economy] (-s)");
            sender.sendMessage("[default] - Set the default economy to use for the whole server");
            sender.sendMessage("[(w:)world] - Set the default economy to use within a given world. Using the w: prefix is optional for clarity to distinguish between worlds and players");
            sender.sendMessage("[(p:)player] - Sets the economy to use for a given player. Using the p: prefix is optional for clarity to distinguish between players and worlds");
            sender.sendMessage("If this argument is omitted, the command will set the player's personal economy");
            sender.sendMessage("If the command is sent from the console, it command will set the server's default economy");
            sender.sendMessage("[economy] - The name of the economy to use. Use /ListEconomies to see a full list of installed economies");
            sender.sendMessage("(-s) - Add this option to the end of the command to execute it silently, with no confirmation message, when successful");
            return true;
        }

        if (args.length < 1 || args.length > 3) {
            return false;
        }

        boolean silent = false;
        World worldTarget = null;
        boolean global = false;
        Player playerTarget = null;
        String economyName;
        if (args.length == 1) {
            if (sender instanceof Player) {
                playerTarget = (Player) sender;
                if (!sender.hasPermission("vmc.commands.seteconomy")
                        && !sender.hasPermission("vmc.commands.seteconomy.self")) {
                    sender.sendMessage("You do not have permission to set your economy");
                    return true;
                }
            }
            economyName = args[0];
        } else {
            if (!sender.hasPermission("vmc.commands.seteconomy")) {
                sender.sendMessage("You do not have permission to set the economy");
                return true;
            }
            if (args.length == 3) {
                if (args[2].equals("-s")) {
                    silent = true;
                } else {
                    sender.sendMessage("Unknown option (" + args[2] + ") expected (-s)");
                    return true;
                }
                economyName = args[1];
            } else {
                if (args[1].equals("-s")) {
                    silent = true;
                    economyName = args[0];
                    global = true;
                } else {
                    economyName = args[1];
                }
            }
            if (!global) {
                String target = args[0];
                if (target.toLowerCase().startsWith("p:")) {
                    playerTarget = plugin.getServer().getPlayerExact(target.substring(2));
                    if (playerTarget == null) {
                        sender.sendMessage("Unknown player: " + target.substring(2));
                        return true;
                    }
                } else if (target.toLowerCase().startsWith("w:")) {
                    worldTarget = plugin.getServer().getWorld(target.substring(2));
                    if (worldTarget == null) {
                        sender.sendMessage("Unknown world: " + target.substring(2));
                        return true;
                    }
                } else {
                    playerTarget = plugin.getServer().getPlayerExact(target);
                    if (playerTarget == null) {
                        worldTarget = plugin.getServer().getWorld(target);
                        if (worldTarget == null) {
                            sender.sendMessage("Unknown player or world: " + target);
                            return true;
                        }
                    }
                }
            }
        }

        Economy economy = plugin.getProvider(economyName);
        if (economy == null) {
            sender.sendMessage("Unknown economy: " + economyName);
            return true;
        }

        if (global) {
            plugin.getEconomy().setDefaultEconomy(economy);
            if (!silent) {
                sender.sendMessage("The default economy for the server has been set to " + economy.currencyNameSingular() + ChatColor.RESET + " (" + economy.getName() + ")");
            }
            return true;
        } else if (worldTarget != null) {
            plugin.getEconomy().setWorldEconomy(worldTarget, economy);
            if (!silent) {
                sender.sendMessage("The default economy for " + worldTarget.getName() + " has been set to " + economy.currencyNameSingular() + ChatColor.RESET + " (" + economy.getName() + ")");
            }
            return true;
        } else if (playerTarget != null) {
            plugin.getEconomy().setEconomy(playerTarget.getName(), economy);
            if (!silent) {
                sender.sendMessage("The economy for " + playerTarget.getDisplayName() + ChatColor.RESET + " has been set to " + economy.currencyNameSingular() + ChatColor.RESET + " (" + economy.getName() + ")");
            }
            return true;
        } else {
            throw new RuntimeException("Unreachable code");
        }
    }
}
