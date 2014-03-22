/*
 * Copyright (C) 2014 XHawk87
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
