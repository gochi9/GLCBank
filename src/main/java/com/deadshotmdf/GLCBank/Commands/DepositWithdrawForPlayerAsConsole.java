package com.deadshotmdf.GLCBank.Commands;

import com.deadshotmdf.GLCBank.ConfigSettings;
import com.deadshotmdf.GLCBank.Managers.BankManager;
import com.deadshotmdf.GLCBank.Objects.CommandType;
import com.deadshotmdf.GLCBank.Objects.ModifyType;
import com.deadshotmdf.GLCBank.Utils.BankUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class DepositWithdrawForPlayerAsConsole extends SubCommand{

    public DepositWithdrawForPlayerAsConsole(BankManager bankManager, String permission, CommandType commandType, int argsRequired, String commandHelpMessage, String commandWrongSyntax) {
        super(bankManager, permission, commandType, argsRequired, commandHelpMessage, commandWrongSyntax);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!canExecute(sender, args.length, true))
            return;

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

        Double amount = BankUtils.getDouble(args[2]);

        if(amount == null){
            sender.sendMessage(ConfigSettings.getInvalidAmount());
            return;
        }

        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(target);
        sender.sendMessage(ConfigSettings.getConsoleDepositWithdrawSuccessMessage(modifyType.toString(), bankManager.modifyBank(targetPlayer, amount, modifyType), targetPlayer.getName()));
    }
}
