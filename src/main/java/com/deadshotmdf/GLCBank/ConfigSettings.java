package com.deadshotmdf.GLCBank;

import com.deadshotmdf.GLCBank.Utils.BankUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigSettings {

    private static String reloadConfig;
    private static String noPermission;

    private static int percentMoneyLostOnDeathMin;
    private static int percentMoneyLotsOnDeathMax;

    private static double interestMin;
    private static double interestMax;
    private static int minutesPlayedForMax;

    private static double interestGainStreak;
    private static double maxInterestGainStreak;

    //////////////
    // Messages //
    //////////////
    private static String deathBalanceLossMessage;

    private static String noAvailableCommands;
    private static String invalidCommand;
    private static String invalidTransactionType;
    private static String invalidAmount;
    private static String playerNotFound;
    private static String playerBalance;
    private static String playersOnly;
    private static String consoleOnly;

    private static String depositHelpMessage;
    private static String withdrawHelpMessage;
    private static String invalidDepositSyntax;
    private static String invalidWithdrawSyntax;
    private static String depositSuccessMessage;
    private static String withdrawSuccessMessage;

    private static String consoleDepositWithdrawHelpMessage;
    private static String invalidConsoleDepositWithdrawSyntax;
    private static String consoleDepositWithdrawSuccessMessage;

    private static String peekBalanceHelpMessage;
    private static String invalidPeekBalanceSyntax;

    private static String seeBalanceSelfHelpMessage;

    private static String reloadConfigHelpMessage;

    private static String openPlayerInputHelpMessage;
    private static String invalidOpenPlayerInputSyntax;
    private static String playerOffline;

    public static String getReloadConfig(){
        return reloadConfig;
    }

    public static String getNoPermission(){
        return noPermission;
    }

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

    //////////////
    // Messages //
    //////////////
    public static String getDeathBalanceLossMessage(double percentage, double balanceLost) {
        return deathBalanceLossMessage
                .replace("{percentage}", BankUtils.formatDouble(percentage))
                .replace("{balanceLost}", BankUtils.formatDouble(balanceLost));
    }

    public static String getNoAvailableCommands() {
        return noAvailableCommands;
    }

    public static String getInvalidCommand() {
        return invalidCommand;
    }

    public static String getInvalidTransactionType() {
        return invalidTransactionType;
    }

    public static String getInvalidAmount() {
        return invalidAmount;
    }

    public static String getPlayerNotFound(String player) {
        return playerNotFound.replace("{player}", player);
    }

    public static String getPlayerBalance(String player, double balance) {
        return playerBalance.replace("{player}", player).replace("{balance}", BankUtils.formatDouble(balance));
    }

    public static String getPlayersOnly() {
        return playersOnly;
    }

    public static String getConsoleOnly() {
        return consoleOnly;
    }

    public static String getDepositHelpMessage() {
        return depositHelpMessage;
    }

    public static String getWithdrawHelpMessage() {
        return withdrawHelpMessage;
    }

    public static String getInvalidDepositSyntax() {
        return invalidDepositSyntax;
    }

    public static String getInvalidWithdrawSyntax() {
        return invalidWithdrawSyntax;
    }

    public static String getDepositSuccessMessage(double amount) {
        return depositSuccessMessage.replace("{amount}", BankUtils.formatDouble(amount));
    }

    public static String getWithdrawSuccessMessage(double amount) {
        return withdrawSuccessMessage.replace("{amount}", BankUtils.formatDouble(amount));
    }

    public static String getConsoleDepositWithdrawHelpMessage() {
        return consoleDepositWithdrawHelpMessage;
    }

    public static String getInvalidConsoleDepositWithdrawSyntax() {
        return invalidConsoleDepositWithdrawSyntax;
    }

    public static String getConsoleDepositWithdrawSuccessMessage(String transactionType, double amount, String player) {
        return consoleDepositWithdrawSuccessMessage
                .replace("{transactionType}", transactionType)
                .replace("{amount}", BankUtils.formatDouble(amount))
                .replace("{player}", player);
    }

    public static String getPeekBalanceHelpMessage() {
        return peekBalanceHelpMessage;
    }

    public static String getInvalidPeekBalanceSyntax() {
        return invalidPeekBalanceSyntax;
    }

    public static String getSeeBalanceSelfHelpMessage() {
        return seeBalanceSelfHelpMessage;
    }

    public static String getReloadConfigHelpMessage() {
        return reloadConfigHelpMessage;
    }

    public static String getOpenPlayerInputHelpMessage() {
        return openPlayerInputHelpMessage;
    }

    public static String getInvalidOpenPlayerInputSyntax() {
        return invalidOpenPlayerInputSyntax;
    }

    public static String getPlayerOffline(String player) {
        return playerOffline.replace("{player}", player);
    }

    public static void reloadConfig(GLCB main) {
        main.saveDefaultConfig();
        main.reloadConfig();

        FileConfiguration config = main.getConfig();

        reloadConfig = color(config.getString("reloadConfig"));
        noPermission = color(config.getString("noPermission"));

        percentMoneyLostOnDeathMin = config.getInt("percentMoneyLostOnDeathMin");
        percentMoneyLotsOnDeathMax = config.getInt("percentMoneyLotsOnDeathMax");

        double minInterest = config.getDouble("interestMin");
        double maxInterest  = config.getDouble("interestMax");
        minutesPlayedForMax = config.getInt("minutesPlayedForMax");

        interestGainStreak = config.getDouble("interestGainStreak") / 100;
        maxInterestGainStreak = config.getDouble("maxInterestGainStreak") / 100;

        interestMin = Math.min(minInterest, maxInterest) / 100;
        interestMax = Math.max(minInterest, maxInterest) / 100;

        ConfigurationSection messagesSection = config.getConfigurationSection("messages");

        if(messagesSection == null)
            return;

        deathBalanceLossMessage = color(messagesSection.getString("deathBalanceLossMessage"));

        noAvailableCommands = color(messagesSection.getString("noAvailableCommands"));
        invalidCommand = color(messagesSection.getString("invalidCommand"));
        invalidTransactionType = color(messagesSection.getString("invalidTransactionType"));
        invalidAmount = color(messagesSection.getString("invalidAmount"));
        playerNotFound = color(messagesSection.getString("playerNotFound"));
        playerBalance = color(messagesSection.getString("playerBalance"));
        playersOnly = color(messagesSection.getString("playersOnly"));
        consoleOnly = color(messagesSection.getString("consoleOnly"));

        depositHelpMessage = color(messagesSection.getString("depositHelpMessage"));
        withdrawHelpMessage = color(messagesSection.getString("withdrawHelpMessage"));
        invalidDepositSyntax = color(messagesSection.getString("invalidDepositSyntax"));
        invalidWithdrawSyntax = color(messagesSection.getString("invalidWithdrawSyntax"));
        depositSuccessMessage = color(messagesSection.getString("depositSuccessMessage"));
        withdrawSuccessMessage = color(messagesSection.getString("withdrawSuccessMessage"));

        consoleDepositWithdrawHelpMessage = color(messagesSection.getString("consoleDepositWithdrawHelpMessage"));
        invalidConsoleDepositWithdrawSyntax = color(messagesSection.getString("invalidConsoleDepositWithdrawSyntax"));
        consoleDepositWithdrawSuccessMessage = color(messagesSection.getString("consoleDepositWithdrawSuccessMessage"));

        peekBalanceHelpMessage = color(messagesSection.getString("peekBalanceHelpMessage"));
        invalidPeekBalanceSyntax = color(messagesSection.getString("invalidPeekBalanceSyntax"));

        seeBalanceSelfHelpMessage = color(messagesSection.getString("seeBalanceSelfHelpMessage"));

        reloadConfigHelpMessage = color(messagesSection.getString("reloadConfigHelpMessage"));

        openPlayerInputHelpMessage = color(messagesSection.getString("openPlayerInputHelpMessage"));
        invalidOpenPlayerInputSyntax = color(messagesSection.getString("invalidOpenPlayerInputSyntax"));
        playerOffline = color(messagesSection.getString("playerOffline"));
    }

    private static String color(String s){
        return ChatColor.translateAlternateColorCodes('&', s != null ? s : "NULL");
    }

    private static String s(Object o){
        return String.valueOf(o);
    }
}
