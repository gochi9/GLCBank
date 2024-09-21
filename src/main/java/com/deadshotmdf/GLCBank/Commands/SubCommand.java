package com.deadshotmdf.GLCBank.Commands;

import com.deadshotmdf.GLCBank.ConfigSettings;
import com.deadshotmdf.GLCBank.Managers.BankManager;
import com.deadshotmdf.GLCBank.Objects.Enums.CommandType;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class SubCommand {

    protected final BankManager bankManager;
    private final String permission;
    private final CommandType commandType;
    private final int argsRequired;
    private final String commandHelpMessage;
    private final String commandWrongSyntax;

    public SubCommand(BankManager bankManager, String permission, CommandType commandType, int argsRequired, String commandHelpMessage, String commandWrongSyntax) {
        this.bankManager = bankManager;
        this.permission = permission;
        this.commandType = commandType;
        this.argsRequired = ++argsRequired;
        this.commandHelpMessage = commandHelpMessage;
        this.commandWrongSyntax = commandWrongSyntax;
    }

    protected boolean canExecute(CommandSender sender, int argsLength, boolean sendMessage){
        boolean isPlayer = sender instanceof Player;
        if(commandType == CommandType.PLAYER && !isPlayer){
            if(sendMessage)
                sender.sendMessage(ConfigSettings.getPlayersOnly());
            return false;
        }

        if(isPlayer && !sender.hasPermission(permission)){
            if(sendMessage)
                sender.sendMessage(ConfigSettings.getNoPermission());
            return false;
        }

        if(commandType == CommandType.CONSOLE && !(sender instanceof ConsoleCommandSender)){
            if(sendMessage)
                sender.sendMessage(ConfigSettings.getConsoleOnly());
            return false;
        }

        if(sendMessage && argsRequired > 1 && argsLength < argsRequired){
            sender.sendMessage(commandWrongSyntax);
            return false;
        }

        return true;
    }

    public abstract void execute(CommandSender sender, String[] args);
    public List<String> tabCompleter(CommandSender sender, String[] args){
        return BankCommand.EMPTY;
    }

    public String getCommandHelpMessage(){
        return commandHelpMessage;
    }

}
