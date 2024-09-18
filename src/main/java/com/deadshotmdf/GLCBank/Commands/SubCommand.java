package com.deadshotmdf.GLCBank.Commands;

import com.deadshotmdf.GLCBank.GLCB;
import com.deadshotmdf.GLCBank.Managers.BankManager;
import com.deadshotmdf.GLCBank.Objects.CommandType;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public abstract class SubCommand {

    protected final GLCB main;
    protected final BankManager bankManager;
    private final String permission;
    private final CommandType commandType;

    public SubCommand(GLCB main, BankManager bankManager, String permission, CommandType commandType) {
        this.main = main;
        this.bankManager = bankManager;
        this.permission = permission;
        this.commandType = commandType;
    }

    protected boolean canExecute(CommandSender sender){
        boolean isPlayer = sender instanceof Player;
        if(commandType == CommandType.PLAYER && !isPlayer){
            //msg
            return false;
        }

        if(commandType == CommandType.CONSOLE && !(sender instanceof ConsoleCommandSender)){
            //msg
            return false;
        }

        if(isPlayer && !sender.hasPermission(permission)){
            //msg
            return false;
        }

        return true;
    }

    public abstract void execute(CommandSender sender, String[] args);

}
