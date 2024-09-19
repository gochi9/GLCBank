package com.deadshotmdf.GLCBank;

import com.deadshotmdf.GLCBank.Commands.BankBalanceCommand;
import com.deadshotmdf.GLCBank.Listeners.PlayerDeathListener;
import com.deadshotmdf.GLCBank.Listeners.PlayerJoinQuitLis;
import com.deadshotmdf.GLCBank.Managers.BankManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class GLCB extends JavaPlugin {

    private Economy econ = null;
    private BankManager bankManager;

    public void onEnable() {
        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        ConfigSettings.reloadConfig(this);

        this.bankManager = new BankManager(this, econ);

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerDeathListener(bankManager), this);
        pm.registerEvents(new PlayerJoinQuitLis(bankManager), this);

        BankBalanceCommand bankBalanceCommand = new BankBalanceCommand(this, bankManager);
        this.getCommand("bank").setExecutor(bankBalanceCommand);
        this.getCommand("bank").setTabCompleter(bankBalanceCommand);
    }

    public void onDisable() {
        this.bankManager.onServerStop();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return false;

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return false;

        econ = rsp.getProvider();
        return econ != null;
    }

}
