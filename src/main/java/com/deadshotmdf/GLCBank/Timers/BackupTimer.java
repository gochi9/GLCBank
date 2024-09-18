package com.deadshotmdf.GLCBank.Timers;

import com.deadshotmdf.GLCBank.Managers.BankManager;
import org.bukkit.scheduler.BukkitRunnable;

public class BackupTimer extends BukkitRunnable {

    private final BankManager bankManager;

    public BackupTimer(BankManager bankManager){
        this.bankManager = bankManager;
    }

    public void run(){
        bankManager.saveInformation();
    }

}
