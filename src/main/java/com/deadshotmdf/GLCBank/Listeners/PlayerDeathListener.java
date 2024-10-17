package com.deadshotmdf.GLCBank.Listeners;

import com.deadshotmdf.GLCBank.Managers.BankManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private final BankManager bankManager;

    public PlayerDeathListener(BankManager bankManager) {
        this.bankManager = bankManager;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent ev) {
        if(!ev.getPlayer().hasPermission("glc.cookiebooster"))
            bankManager.onDeath(ev.getPlayer());
    }

}
