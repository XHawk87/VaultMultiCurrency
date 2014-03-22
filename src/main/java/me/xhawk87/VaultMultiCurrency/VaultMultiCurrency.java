/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.xhawk87.VaultMultiCurrency;

import java.util.HashMap;
import java.util.Map;
import me.xhawk87.VaultMultiCurrency.vault.MultiEconomy;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author XHawk87
 */
public class VaultMultiCurrency extends JavaPlugin {

    private MultiEconomy economy;
    private Map<String, Economy> providers = new HashMap<>();

    @Override
    public void onEnable() {
        registerVaultHook();
        new BukkitRunnable() {
            @Override
            public void run() {
                registerCurrencies();
            }
        }.runTaskLater(this, 40);
    }

    private void registerVaultHook() {
        economy = new MultiEconomy(this);
        Plugin vaultPlugin = getServer().getPluginManager().getPlugin("Vault");
        if (vaultPlugin == null) {
            getLogger().severe("Vault could not be located. This plugin is an add-on for Vault and cannot run without it. Please install Vault and restart the server.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getServer().getServicesManager().register(Economy.class, economy, vaultPlugin, ServicePriority.Highest);
    }

    private void registerCurrencies() {
        RegisteredServiceProvider<Economy> highest = null;
        for (RegisteredServiceProvider<Economy> provider : getServer().getServicesManager().getRegistrations(Economy.class)) {
            providers.put(provider.getProvider().getName(), provider.getProvider());
            if (highest == null || highest.getPriority().ordinal() < provider.getPriority().ordinal()) {
                highest = provider;
            }
        }
    }

    public MultiEconomy getEconomy() {
        return economy;
    }

    public Economy getProvider(String economyName) {
        Economy provider = providers.get(economyName);
        if (provider == null) {
            for (RegisteredServiceProvider<Economy> rsp : getServer().getServicesManager().getRegistrations(Economy.class)) {
                Economy registered = rsp.getProvider();
                if (!providers.containsKey(registered.getName())) {
                    providers.put(registered.getName(), registered);
                }
                if (registered.getName().equalsIgnoreCase(economyName)) {
                    provider = registered;
                }
            }
        }
        return provider;
    }
}
