package com.deadshotmdf.GLCBank.Commands;

import com.deadshotmdf.GLCBank.ConfigSettings;
import com.deadshotmdf.GLCBank.Managers.BankManager;
import com.deadshotmdf.GLCBank.Objects.Enums.CommandType;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.UUID;

public class PeekBalance extends SubCommand{

    public PeekBalance(BankManager bankManager, String permission, CommandType commandType, int argsRequired, String commandHelpMessage, String commandWrongSyntax) {
        super(bankManager, permission, commandType, argsRequired, commandHelpMessage, commandWrongSyntax);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        UUID target = bankManager.getOfflineUUID(args[1]);

        if(target == null){
            sender.sendMessage(ConfigSettings.getPlayerNotFound(args[1]));
            return;
        }

        sender.sendMessage(ConfigSettings.getPlayerBalance(args[1], bankManager.getPlayerBank(target).getAmount()));
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
