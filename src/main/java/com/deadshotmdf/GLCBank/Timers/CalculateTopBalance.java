package com.deadshotmdf.GLCBank.Timers;

import com.deadshotmdf.GLCBank.GLCB;
import com.deadshotmdf.GLCBank.Managers.BankManager;
import com.deadshotmdf.GLCBank.Objects.Player.BankProfile;
import com.deadshotmdf.GLCBank.Objects.Top.PlayerData;
import com.deadshotmdf.GLCBank.Objects.Top.PlayerDataPair;
import com.deadshotmdf.GLCBank.Objects.Top.PlayerTopProfile;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class CalculateTopBalance extends BukkitRunnable {

    private final GLCB main;
    private final Economy economy;
    private final BankManager bankManager;
    private final HashMap<UUID, BankProfile> banks;

    public CalculateTopBalance(GLCB main, Economy economy, BankManager bankManager, HashMap<UUID, BankProfile> banks) {
        this.main = main;
        this.economy = economy;
        this.bankManager = bankManager;
        this.banks = banks;
    }

    public void run(){
        HashMap<UUID, BankProfile> banksClone = new HashMap<>(banks);
        List<PlayerData> playersData = new ArrayList<>(banksClone.size());

        for (Map.Entry<UUID, BankProfile> entry : banksClone.entrySet()) {
            UUID uuid = entry.getKey();
            BankProfile bankProfile = entry.getValue();
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

            String playerName = offlinePlayer.getName();

            if (playerName == null)
                continue;

            double bankBalance = bankProfile.getAmount();
            double currentBalance = economy.getBalance(offlinePlayer);
            playersData.add(new PlayerData(playerName, uuid, bankBalance, currentBalance, bankBalance + currentBalance));
        }

        List<PlayerDataPair> topBankBalances = playersData.stream()
                .sorted(Comparator.comparingDouble(PlayerData::bankBalance).reversed())
                .map(pd -> new PlayerDataPair(pd.playerName(), pd.playerUUID(), pd.bankBalance()))
                .collect(Collectors.toList());

        List<PlayerDataPair> topCurrentBalances = playersData.stream()
                .sorted(Comparator.comparingDouble(PlayerData::currentBalance).reversed())
                .map(pd -> new PlayerDataPair(pd.playerName(), pd.playerUUID(), pd.currentBalance()))
                .collect(Collectors.toList());

        List<PlayerDataPair> topCombinedBalances = playersData.stream()
                .sorted(Comparator.comparingDouble(PlayerData::combinedBalance).reversed())
                .map(pd -> new PlayerDataPair(pd.playerName(), pd.playerUUID(), pd.combinedBalance()))
                .collect(Collectors.toList());

        HashMap<UUID, PlayerTopProfile> playerTopProfiles = new HashMap<>();

        assignAllRankings(topBankBalances, topCurrentBalances, topCombinedBalances, playerTopProfiles);

        Bukkit.getScheduler().runTask(main, () -> bankManager.updateTop(topBankBalances, topCurrentBalances, topCombinedBalances, playerTopProfiles));
    }

    private void assignAllRankings(List<PlayerDataPair> topBankBalances,
                                   List<PlayerDataPair> topCurrentBalances,
                                   List<PlayerDataPair> topCombinedBalances,
                                   Map<UUID, PlayerTopProfile> playerTopProfiles) {

        updateRankings(topBankBalances, playerTopProfiles, (profile, rank) ->
                new PlayerTopProfile(profile.name(), profile.uuid(), rank, profile.topBalance(), profile.topCombined()));

        updateRankings(topCurrentBalances, playerTopProfiles, (profile, rank) ->
                new PlayerTopProfile(profile.name(), profile.uuid(), profile.topBank(), rank, profile.topCombined()));

        updateRankings(topCombinedBalances, playerTopProfiles, (profile, rank) ->
                new PlayerTopProfile(profile.name(), profile.uuid(), profile.topBank(), profile.topBalance(), rank));
    }

    private void updateRankings(List<PlayerDataPair> balances,
                                Map<UUID, PlayerTopProfile> playerTopProfiles,
                                BiFunction<PlayerTopProfile, Integer, PlayerTopProfile> updater) {
        for (int i = 0; i < balances.size(); i++) {
            PlayerDataPair pair = balances.get(i);
            int rank = i + 1;

            playerTopProfiles.compute(pair.uuid(), (uuid, profile) -> {
                if (profile == null) {
                    profile = new PlayerTopProfile(pair.name(), uuid, 0, 0, 0);
                }
                return updater.apply(profile, rank);
            });
        }
    }

}
