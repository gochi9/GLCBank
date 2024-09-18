package com.deadshotmdf.GLCBank;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigSettings {

    private static int percentMoneyLostOnDeathMin;
    private static int percentMoneyLotsOnDeathMax;

    private static double interestMin;
    private static double interestMax;
    private static int minutesPlayedForMax;

    private static double interestGainStreak;
    private static double maxInterestGainStreak;

    public static int getPercentMoneyLostOnDeathMin() {
        return percentMoneyLostOnDeathMin;
    }

    public static int getPercentMoneyLotsOnDeathMax() {
        return percentMoneyLotsOnDeathMax;
    }

    public static double getInterestMin() {
        return interestMin;
    }

    public static double getInterestMax() {
        return interestMax;
    }

    public static int getMinutesPlayedForMax() {
        return minutesPlayedForMax;
    }

    public static double getInterestGainStreak() {
        return interestGainStreak;
    }

    public static double getMaxInterestGainStreak() {
        return maxInterestGainStreak;
    }

    public static void reloadConfig(GLCB main) {
        main.saveDefaultConfig();
        main.reloadConfig();

        FileConfiguration config = main.getConfig();

        percentMoneyLostOnDeathMin = config.getInt("percentMoneyLostOnDeathMin");
        percentMoneyLotsOnDeathMax = config.getInt("percentMoneyLotsOnDeathMax");

        double minInterest = config.getDouble("interestMin");
        double maxInterest  = config.getDouble("interestMax");
        minutesPlayedForMax = config.getInt("minutesPlayedForMax");

        interestGainStreak = config.getDouble("interestGainStreak") / 10;
        maxInterestGainStreak = config.getDouble("maxInterestGainStreak") / 10;

        interestMin = Math.min(minInterest, maxInterest) / 10;
        interestMax = Math.max(minInterest, maxInterest) / 10;
    }
}
