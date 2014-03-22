/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.xhawk87.VaultMultiCurrency.vault;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.xhawk87.VaultMultiCurrency.VaultMultiCurrency;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
 * @author XHawk87
 */
public class MultiEconomy implements Economy {

    private Map<String, Economy> perPlayer = new HashMap<>();
    private Map<String, Economy> perWorld = new HashMap<>();
    private Economy serverEconomy;
    private VaultMultiCurrency plugin;

    public MultiEconomy(VaultMultiCurrency plugin) {
        this.plugin = plugin;
    }

    public void setDefaultEconomy(Economy economy) {
        this.serverEconomy = economy;
    }

    public Economy getDefaultEconomy() {
        return serverEconomy;
    }

    public void setWorldEconomy(World worldTarget, Economy economy) {
        perWorld.put(worldTarget.getName(), economy);
    }

    public void setEconomy(String playerName, Economy economy) {
        perPlayer.put(playerName, economy);
    }

    public Economy getEconomy(String playerName) {
        Economy economy = perPlayer.get(playerName);
        if (economy == null) {
            Player player = plugin.getServer().getPlayerExact(playerName);
            if (player != null) {
                economy = perWorld.get(player.getWorld().getName());
                if (economy != null) {
                    return economy;
                }
            }
            return getDefaultEconomy();
        }
        return economy;
    }

    @Override
    public boolean isEnabled() {
        return serverEconomy.isEnabled();
    }

    @Override
    public String getName() {
        return serverEconomy.getName();
    }

    @Override
    public boolean hasBankSupport() {
        return serverEconomy.hasBankSupport();
    }

    @Override
    public int fractionalDigits() {
        return serverEconomy.fractionalDigits();
    }

    @Override
    public String format(double amount) {
        return serverEconomy.format(amount);
    }

    @Override
    public String currencyNamePlural() {
        return serverEconomy.currencyNamePlural();
    }

    @Override
    public String currencyNameSingular() {
        return serverEconomy.currencyNameSingular();
    }

    @Override
    public boolean hasAccount(String playerName) {
        return serverEconomy.hasAccount(playerName);
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return serverEconomy.hasAccount(playerName, worldName);
    }

    @Override
    public double getBalance(String playerName) {
        return serverEconomy.getBalance(playerName);
    }

    @Override
    public double getBalance(String playerName, String world) {
        return serverEconomy.getBalance(playerName, world);
    }

    @Override
    public boolean has(String playerName, double amount) {
        return serverEconomy.has(playerName, amount);
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return serverEconomy.has(playerName, worldName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return serverEconomy.withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return serverEconomy.withdrawPlayer(playerName, worldName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return serverEconomy.depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return serverEconomy.depositPlayer(playerName, worldName, amount);
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return serverEconomy.createBank(name, player);
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return serverEconomy.deleteBank(name);
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return serverEconomy.bankBalance(name);
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return serverEconomy.bankHas(name, amount);
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return serverEconomy.bankWithdraw(name, amount);
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return serverEconomy.bankDeposit(name, amount);
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return serverEconomy.isBankOwner(name, playerName);
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return serverEconomy.isBankMember(name, playerName);
    }

    @Override
    public List<String> getBanks() {
        return serverEconomy.getBanks();
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return serverEconomy.createPlayerAccount(playerName);
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return serverEconomy.createPlayerAccount(playerName, worldName);
    }
}
