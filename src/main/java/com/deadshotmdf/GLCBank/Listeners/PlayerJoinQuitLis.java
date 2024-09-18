package com.deadshotmdf.GLCBank.Listeners;

import com.deadshotmdf.GLCBank.Managers.BankManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitLis implements Listener {

    private final BankManager bankManager;

    public PlayerJoinQuitLis(BankManager bankManager) {
        this.bankManager = bankManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent ev) {
        bankManager.onJoin(ev.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent ev) {
        bankManager.getPlayerBank(ev.getPlayer().getUniqueId()).onQuit();
    }

}
