package com.deadshotmdf.GLCBank.Commands;

import com.deadshotmdf.GLCBank.GLCB;
import com.deadshotmdf.GLCBank.Managers.BankManager;
import com.deadshotmdf.GLCBank.Objects.CommandType;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.UUID;

public class PeekBalance extends SubCommand{

    public PeekBalance(GLCB main, BankManager bankManager, String permission, CommandType commandType) {
        super(main, bankManager, permission, commandType);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!canExecute(sender, true))
            return;

        if(args.length < 2){
            //msg
            return;
        }

        UUID target = bankManager.getOfflineUUID(args[1]);

        if(target == null){
            //msg
            return;
        }

        sender.sendMessage(args[1] + ": " + bankManager.getPlayerBank(target).getAmount());
    }

    @Override
    public List<String> tabCompleter(CommandSender sender, String[] args) {
        if(!canExecute(sender, false) || args.length != 2)
            return BankBalanceCommand.EMPTY;

        if(!args[1].isEmpty())
            return bankManager.getName(args[1]);

        else
            return bankManager.getNames();
    }
}
