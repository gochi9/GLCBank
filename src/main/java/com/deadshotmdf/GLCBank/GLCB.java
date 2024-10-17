package com.deadshotmdf.GLCBank;

import com.deadshotmdf.GLCBank.Commands.BankCommand;
import com.deadshotmdf.GLCBank.Listeners.PlayerDeathListener;
import com.deadshotmdf.GLCBank.Listeners.PlayerJoinQuitLis;
import com.deadshotmdf.GLCBank.Managers.BankManager;
import com.deadshotmdf.GLC_GUIS.GLCGGUIS;
import com.deadshotmdf.GLC_GUIS.Mayor.Enums.UpgradeType;
import com.deadshotmdf.GLC_GUIS.Mayor.MayorManager;
import com.deadshotmdf.gLCoins_Server.EconomyWrapper;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class GLCB extends JavaPlugin {

    private EconomyWrapper econ = null;
    private BankManager bankManager;
    private MayorManager mayorManager;

    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();

        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            pm.disablePlugin(this);
            return;
        }

        try{
            this.mayorManager = ((GLCGGUIS)pm.getPlugin("GLC-GUIS")).getMayorManager();
            this.mayorManager.getUpgrade(UpgradeType.BANK);
        }
        catch(Throwable ignored){
            pm.disablePlugin(this);
            return;
        }

        ConfigSettings.reloadConfig(this);

        this.bankManager = new BankManager(this, mayorManager, econ);

        pm.registerEvents(new PlayerDeathListener(bankManager), this);
        pm.registerEvents(new PlayerJoinQuitLis(bankManager), this);

        BankCommand bankCommand = new BankCommand(this, bankManager, mayorManager);
        this.getCommand("bank").setExecutor(bankCommand);
        this.getCommand("bank").setTabCompleter(bankCommand);
    }

    public void onDisable() {
        if(this.bankManager != null)
            this.bankManager.onServerStop();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return false;

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return false;

        econ = (EconomyWrapper) rsp.getProvider();
        return econ != null;
    }

}
