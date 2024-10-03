package com.deadshotmdf.GLCBank.Managers;

import com.deadshotmdf.GLCBank.*;
import com.deadshotmdf.GLCBank.Commands.BankCommand;
import com.deadshotmdf.GLCBank.File.InformationHolder;
import com.deadshotmdf.GLCBank.Objects.Player.BankProfile;
import com.deadshotmdf.GLCBank.Objects.Enums.ModifyType;
import com.deadshotmdf.GLCBank.Objects.Sign.SignActionFinish;
import com.deadshotmdf.GLCBank.Objects.Top.PlayerDataPair;
import com.deadshotmdf.GLCBank.Objects.Top.PlayerTopProfile;
import com.deadshotmdf.GLCBank.Timers.BackupTimer;
import com.deadshotmdf.GLCBank.Timers.CalculateTopBalance;
import com.deadshotmdf.GLCBank.Utils.BankUtils;
import com.deadshotmdf.GLCBank.Utils.NameSearcher;
import com.deadshotmdf.gLCoins_Server.EconomyWrapper;
import de.rapha149.signgui.SignGUI;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class BankManager extends InformationHolder {

    private final GLCB main;
    private final EconomyWrapper economy;
    private final Random random;
    private final HashMap<UUID, BankProfile> banks;
    private final HashMap<String, UUID> uuids;
    private final NameSearcher nameSearcher;
    private final BackupTimer backupTimer;
    private final CalculateTopBalance calculateTopBalance;
    private final List<PlayerDataPair> topBankBalances, topCurrentBalances, topCombinedBalances;
    private final HashMap<UUID, PlayerTopProfile> playerTopProfiles;
    private final SignGUI depositGUI, withdrawGUI;

    public BankManager(GLCB main, EconomyWrapper economy) {
        super(main, "bank.yml");
        this.main = main;
        this.economy = economy;
        this.random = new Random();
        this.banks = new HashMap<>();
        this.uuids = new HashMap<>();
        this.nameSearcher = new NameSearcher();
        this.topBankBalances = new ArrayList<>();
        this.topCurrentBalances = new ArrayList<>();
        this.topCombinedBalances = new ArrayList<>();
        this.playerTopProfiles = new HashMap<>();
        this.depositGUI = SignGUI.builder()
                .setLines(null, "^^^^^^^^^", "Please enter the", "deposit amount")
                .setHandler(new SignActionFinish(main, ModifyType.ADD)).build();
        this.withdrawGUI = SignGUI.builder()
                .setLines(null, "^^^^^^^^^", "Please enter the", "withdraw amount")
                .setHandler(new SignActionFinish(main, ModifyType.REMOVE)).build();

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
        //1hour = 7200L = 3600seconds * 20 ticks
        this.backupTimer.runTaskTimerAsynchronously(main, 72000L, 72000L);

        this.calculateTopBalance = new CalculateTopBalance(main, economy, this, banks);
        //10minutes = 12000L = 600seconds * 20 ticks
        this.calculateTopBalance.runTaskTimerAsynchronously(main, 20L, 12000L);
    }

    public BankProfile getPlayerBank(UUID uuid){
        return banks.computeIfAbsent(uuid, k -> new BankProfile(0, 0));
    }

    public UUID getOfflineUUID(String name){
        return name != null ? uuids.get(name.toLowerCase()) : null;
    }

    public List<String> getName(String index){
        return index == null || index.isBlank() ? BankCommand.EMPTY : nameSearcher.search(index.toLowerCase());
    }

    public List<String> getNames(){
        return new LinkedList<>(uuids.keySet());
    }

    public void onJoin(Player player){
        UUID uuid = player.getUniqueId();
        String name =  player.getName().toLowerCase();
        getPlayerBank(uuid).onJoin();
        uuids.put(name, uuid);
        nameSearcher.addName(name);
    }

    public void openPlayerInputSign(Player player, ModifyType modifyType){
        switch(modifyType){
            case ADD:
                depositGUI.open(player);
                break;
            case REMOVE:
                withdrawGUI.open(player);
        }
    }

    public double withdrawBank(OfflinePlayer player, double withdrawAmount){
        return this.modifyBank(player, withdrawAmount, ModifyType.REMOVE);
    }

    public double depositBank(OfflinePlayer player, double depositAmount){
        return this.modifyBank(player, depositAmount, ModifyType.ADD);
    }

    public double modifyBank(OfflinePlayer player, double amount, ModifyType modifyType){
        BankProfile bank = getPlayerBank(player.getUniqueId());
        boolean remove = modifyType == ModifyType.REMOVE;
        double balance = remove ? bank.getAmount() : economy.getBalance(player);

        if(balance < amount)
            amount = balance;

        if(remove)
            economy.noTaxDepositPlayer(player, amount);
        else
            economy.withdrawPlayer(player, amount);

        bank.modifyAmount(amount, modifyType);

        return amount;
    }

    public void onDeath(Player player){
        double balance = economy.getBalance(player);

        if(balance <= 0.01)
            return;

        double percent = getRandomNumber(ConfigSettings.getPercentMoneyLostOnDeathMin(), ConfigSettings.getPercentMoneyLotsOnDeathMax());
        double balanceLost = (percent / 100.0) * balance;
        economy.withdrawPlayer(player, balanceLost);
        player.sendMessage(ConfigSettings.getDeathBalanceLossMessage(percent, balanceLost));
    }

    public void onServerStop(){
        try{
            this.backupTimer.cancel();
            this.calculateTopBalance.cancel();
        }
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

    public void updateTop(List<PlayerDataPair> topBankBalances, List<PlayerDataPair> topCurrentBalances, List<PlayerDataPair> topCombinedBalances, HashMap<UUID, PlayerTopProfile> playerTopProfiles){
        this.topBankBalances.clear();
        this.topCurrentBalances.clear();
        this.topCombinedBalances.clear();
        this.playerTopProfiles.clear();

        this.topBankBalances.addAll(topBankBalances);
        this.topCurrentBalances.addAll(topCurrentBalances);
        this.topCombinedBalances.addAll(topCombinedBalances);
        this.playerTopProfiles.putAll(playerTopProfiles);
    }

}
