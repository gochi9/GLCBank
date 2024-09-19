package com.deadshotmdf.GLCBank.Commands;

import com.deadshotmdf.GLCBank.ConfigSettings;
import com.deadshotmdf.GLCBank.Managers.BankManager;
import com.deadshotmdf.GLCBank.Objects.Enums.CommandType;
import com.deadshotmdf.GLCBank.Objects.Enums.ModifyType;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class OpenPlayerInput extends SubCommand{

    public OpenPlayerInput(BankManager bankManager, String permission, CommandType commandType, int argsRequired, String commandHelpMessage, String commandWrongSyntax) {
        super(bankManager, permission, commandType, argsRequired, commandHelpMessage, commandWrongSyntax);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!canExecute(sender, args.length, true))
            return;

        UUID target = bankManager.getOfflineUUID(args[1]);

        if(target == null){
            sender.sendMessage(ConfigSettings.getPlayerNotFound(args[1]));
            return;
        }

        Player player = Bukkit.getPlayer(target);

        if(player == null){
            sender.sendMessage(ConfigSettings.getPlayerOffline(args[1]));
            return;
        }

        ModifyType modifyType = ModifyType.getModifyType(args[2]);

        if(modifyType == null){
            sender.sendMessage(ConfigSettings.getInvalidTransactionType());
            return;
        }

        bankManager.openPlayerInputSign(player, modifyType);
    }

    @Override
    public List<String> tabCompleter(CommandSender sender, String[] args) {
        if(!canExecute(sender, 0, false) || args.length != 2)
            return BankBalanceCommand.EMPTY;

        if(!args[1].isEmpty())
            return bankManager.getName(args[1]);

        else
            return bankManager.getNames();
    }
}
