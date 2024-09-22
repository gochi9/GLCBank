package com.deadshotmdf.GLCBank.Commands;

import com.deadshotmdf.GLCBank.ConfigSettings;
import com.deadshotmdf.GLCBank.Managers.BankManager;
import com.deadshotmdf.GLCBank.Objects.Enums.CommandType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SeeBalanceSelf extends SubCommand{

    public SeeBalanceSelf(BankManager bankManager, String permission, CommandType commandType, int argsRequired, String commandHelpMessage, String commandWrongSyntax) {
        super(bankManager, permission, commandType, argsRequired, commandHelpMessage, commandWrongSyntax);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(ConfigSettings.getPlayerBalance(sender.getName(), bankManager.getPlayerBank(((Player)sender).getUniqueId()).getAmount()));
    }
}
