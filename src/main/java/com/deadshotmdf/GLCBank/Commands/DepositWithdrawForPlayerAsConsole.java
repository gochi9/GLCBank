package com.deadshotmdf.GLCBank.Commands;

import com.deadshotmdf.GLCBank.ConfigSettings;
import com.deadshotmdf.GLCBank.Managers.BankManager;
import com.deadshotmdf.GLCBank.Objects.Enums.CommandType;
import com.deadshotmdf.GLCBank.Objects.Enums.ModifyType;
import com.deadshotmdf.GLCBank.Utils.BankUtils;
import com.deadshotmdf.GLC_GUIS.Mayor.Enums.UpgradeType;
import com.deadshotmdf.GLC_GUIS.Mayor.MayorManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class DepositWithdrawForPlayerAsConsole extends SubCommand{

    private final MayorManager mayorManager;

    public DepositWithdrawForPlayerAsConsole(BankManager bankManager, MayorManager mayorManager, String permission, CommandType commandType, int argsRequired, String commandHelpMessage, String commandWrongSyntax) {
        super(bankManager, permission, commandType, argsRequired, commandHelpMessage, commandWrongSyntax);
        this.mayorManager = mayorManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ModifyType modifyType = ModifyType.getModifyType(args[3]);

        if(modifyType == null || modifyType == ModifyType.SET){
            sender.sendMessage(ConfigSettings.getInvalidTransactionType());
            return;
        }

        UUID target = bankManager.getOfflineUUID(args[1]);

        if(target == null){
            sender.sendMessage(ConfigSettings.getPlayerNotFound(args[1]));
            return;
        }

        if(mayorManager.getPlayerUpgrade(target, UpgradeType.UNLOCK_BANK) < 1){
            sender.sendMessage(com.deadshotmdf.GLC_GUIS.ConfigSettings.getCannotOpenBankTarget(args[1]));
            return;
        }

        Double amount = BankUtils.getDouble(args[2]);

        if(amount == null){
            sender.sendMessage(ConfigSettings.getInvalidAmount());
            return;
        }

        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(target);
        double finalAmount = bankManager.modifyBank(targetPlayer, amount, modifyType);
        sender.sendMessage(ConfigSettings.getConsoleDepositWithdrawSuccessMessage(modifyType.getAlias(), finalAmount, targetPlayer.getName()));

        if(targetPlayer.isOnline())
            targetPlayer.getPlayer().sendMessage(modifyType == ModifyType.ADD ? ConfigSettings.getDepositSuccessMessage(finalAmount) : ConfigSettings.getWithdrawSuccessMessage(finalAmount));
    }

    @Override
    public List<String> tabCompleter(CommandSender sender, String[] args) {
        if(!canExecute(sender, 0, false) || args.length != 2)
            return BankCommand.EMPTY;

        if(!args[1].isEmpty())
            return bankManager.getName(args[1]);

        else
            return bankManager.getNames();
    }
}
