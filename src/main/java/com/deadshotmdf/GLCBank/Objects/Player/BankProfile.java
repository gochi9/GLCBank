package com.deadshotmdf.GLCBank.Objects.Player;

import com.deadshotmdf.GLCBank.Objects.Enums.ModifyType;
import com.deadshotmdf.GLC_GUIS.Mayor.Enums.UpgradeType;
import com.deadshotmdf.GLC_GUIS.Mayor.MayorManager;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BankProfile {

    private double amount;
    private int dayStreak;
    private long totalMinutesPlayedToday;
    private long join;
    private boolean joinedToday;

    public BankProfile(double amount, int dayStreak) {
        this.amount = amount;
        this.dayStreak = dayStreak;
        this.totalMinutesPlayedToday = 0;
        this.join = 0;
        this.joinedToday = false;
    }

    public double getAmount() {
        return amount;
    }

    public int getDayStreak() {
        return dayStreak;
    }

    public long getTotalMinutesPlayedToday() {
        return totalMinutesPlayedToday;
    }

    public boolean hasJoinedToday(){
        return joinedToday;
    }

    public void modifyAmount(double amount, ModifyType modifyType, MayorManager mayorManager, UUID uuid) {
        switch (modifyType) {
            case ADD -> this.amount += amount;
            case REMOVE -> this.amount -= amount;
            case SET -> this.amount = amount;
        }

        int i = (int) mayorManager.getUpgradeBenefit(uuid, UpgradeType.BANK_LIMIT);
        if(i <= 1)
            return;

        this.amount = Math.min(this.amount, i);
    }

    public void onJoin(){
        this.join = System.currentTimeMillis();

        if(this.joinedToday)
            return;

        this.joinedToday = true;
        ++this.dayStreak;
    }

    public void onQuit(){
        if(this.join == 0)
            return;

        this.totalMinutesPlayedToday += TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - this.join);
        this.join = 0;
    }

}
