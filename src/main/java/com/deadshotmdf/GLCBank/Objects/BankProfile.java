package com.deadshotmdf.GLCBank.Objects;

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

    public void modifyAmount(double amount, ModifyType modifyType) {
        switch (modifyType) {
            case ADD -> this.amount += amount;
            case REMOVE -> this.amount -= amount;
            case SET -> this.amount = amount;
        }
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
