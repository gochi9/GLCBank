package com.deadshotmdf.GLCBank.Listeners;

import com.deadshotmdf.GLCBank.Managers.BankManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private final BankManager bankManager;

    public PlayerDeathListener(BankManager bankManager) {
        this.bankManager = bankManager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent ev) {
        bankManager.onDeath(ev.getPlayer());
    }

}
