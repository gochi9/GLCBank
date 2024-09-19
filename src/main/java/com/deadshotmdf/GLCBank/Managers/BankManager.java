package com.deadshotmdf.GLCBank.Managers;

import com.deadshotmdf.GLCBank.*;
import com.deadshotmdf.GLCBank.Commands.BankBalanceCommand;
import com.deadshotmdf.GLCBank.File.InformationHolder;
import com.deadshotmdf.GLCBank.Objects.BankProfile;
import com.deadshotmdf.GLCBank.Objects.ModifyType;
import com.deadshotmdf.GLCBank.Timers.BackupTimer;
import com.deadshotmdf.GLCBank.Utils.BankUtils;
import com.deadshotmdf.GLCBank.Utils.NameSearcher;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class BankManager extends InformationHolder {

    private final GLCB main;
    private final Economy economy;
    private final Random random;
    private final HashMap<UUID, BankProfile> banks;
    private final HashMap<String, UUID> uuids;
    private final NameSearcher nameSearcher;
    private final BackupTimer backupTimer;

    public BankManager(GLCB main, Economy economy) {
        super(main, "bank.yml");
        this.main = main;
        this.economy = economy;
        this.random = new Random();
        this.banks = new HashMap<>();
        this.uuids = new HashMap<>();
        this.nameSearcher = new NameSearcher();

        try{loadInformation();}
        catch (Throwable ignored){}
        for(Player player : Bukkit.getOnlinePlayers())
            try{getPlayerBank(player.getUniqueId()).onJoin();}
            catch (Throwable ignored){continue;}

        for(OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            String name = offlinePlayer.getName();
            if (name == null)
                continue;

            name = name.toLowerCase();
            uuids.put(name, offlinePlayer.getUniqueId());
            nameSearcher.addName(name);
        }

        this.backupTimer = new BackupTimer(this);
        this.backupTimer.runTaskTimerAsynchronously(main, 72000L, 72000L);
    }

    public BankProfile getPlayerBank(UUID uuid){
        return banks.computeIfAbsent(uuid, k -> new BankProfile(0, 0));
    }

    public UUID getOfflineUUID(String name){
        return name != null ? uuids.get(name.toLowerCase()) : null;
    }

    public List<String> getName(String index){
        return index == null || index.isBlank() ? BankBalanceCommand.EMPTY : nameSearcher.search(index.toLowerCase());
    }

    public List<String> getNames(){
        return new LinkedList<>(uuids.keySet());
    }

    public void withdrawBank(OfflinePlayer player, double withdrawAmount){
        this.modifyBank(player, withdrawAmount, ModifyType.REMOVE);
    }

    public void depositBank(OfflinePlayer player, double depositAmount){
        this.modifyBank(player, depositAmount, ModifyType.ADD);
    }

    public void modifyBank(OfflinePlayer player, double amount, ModifyType modifyType){
        BankProfile bank = getPlayerBank(player.getUniqueId());
        boolean remove = modifyType == ModifyType.REMOVE;
        double balance = remove ? bank.getAmount() : economy.getBalance(player);

        if(balance < amount)
            amount = balance;

        EconomyResponse response = remove ? economy.depositPlayer(player, amount) : economy.withdrawPlayer(player, amount);

        if(response.transactionSuccess())
            bank.modifyAmount(amount, modifyType);
    }

    public void onDeath(Player player){
        double balance = economy.getBalance(player);

        if(balance <= 0.0)
            return;

        economy.withdrawPlayer(player, (getRandomNumber(ConfigSettings.getPercentMoneyLostOnDeathMin(), ConfigSettings.getPercentMoneyLotsOnDeathMax()) / 100.0) * balance);
    }

    public void onServerStop(){
        try{this.backupTimer.cancel();}
        catch (Throwable ignored){}

        banks.values().forEach(this::tryCalculateBank);
        saveInformation();
    }

    private void tryCalculateBank(BankProfile bank){
        try{calculateBank(bank);}
        catch (Throwable ignored){}
    }

    private void calculateBank(BankProfile bank){
        bank.onQuit();

        double balance = bank.getAmount();
        double interest = BankUtils.getInterest(bank.getTotalMinutesPlayedToday());
        double dayStreakInterest = bank.getDayStreak() * ConfigSettings.getInterestGainStreak();
        dayStreakInterest = Math.min(dayStreakInterest, ConfigSettings.getMaxInterestGainStreak());

        if(interest <= 0 && dayStreakInterest <= 0)
            return;

        if(balance < 0.0){
            balance = 0.0;
            bank.modifyAmount(0.0, ModifyType.SET);
        }

            bank.modifyAmount((balance * interest) + (balance * dayStreakInterest), ModifyType.ADD);
    }

    @Override
    public void saveInformation(){
        FileConfiguration config = getConfig();
        config.set("Banks", null);

        new HashMap<>(banks).forEach((k, v) -> {
            String path = "Banks." + k;
            config.set(path + ".amount", Math.max(v.getAmount(), 0.0));
            config.set(path + ".dayStreak", v.hasJoinedToday() ? Math.max(v.getDayStreak(), 0) : 0);
        });

        saveC();
    }

    @Override
    public void loadInformation(){
        FileConfiguration config = getConfig();

        Set<String> keys = getKeys("Banks");

        if(keys.isEmpty())
            return;

        for(String key : keys){
            UUID uuid = BankUtils.getUUID(key);

            if(uuid == null)
                continue;

            String path = "Banks." + key;
            double amount = config.getDouble(path + ".amount");
            int dayStreak = config.getInt(path + ".dayStreak");

            banks.put(uuid, new BankProfile(Math.max(amount, 0), Math.max(dayStreak, 0)));
        }
    }

    public int getRandomNumber(int m, int mm){
        int min = Math.min(m, mm);
        int max = Math.max(m, mm);

        return min == max ? max : random.nextInt((max - min) + 1) + min;
    }

    public void onJoin(Player player){
        UUID uuid = player.getUniqueId();
        String name =  player.getName().toLowerCase();
        getPlayerBank(uuid).onJoin();
        uuids.put(name, uuid);
        nameSearcher.addName(name);
    }

}
