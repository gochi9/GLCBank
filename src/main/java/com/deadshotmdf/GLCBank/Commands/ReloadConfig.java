package com.deadshotmdf.GLCBank.Commands;

import com.deadshotmdf.GLCBank.ConfigSettings;
import com.deadshotmdf.GLCBank.GLCB;
import com.deadshotmdf.GLCBank.Managers.BankManager;
import com.deadshotmdf.GLCBank.Objects.Enums.CommandType;
import org.bukkit.command.CommandSender;

public class ReloadConfig extends SubCommand{

    private final GLCB main;

    public ReloadConfig(GLCB main, BankManager bankManager, String permission, CommandType commandType, int argsRequired, String commandHelpMessage, String commandWrongSyntax) {
        super(bankManager, permission, commandType, argsRequired, commandHelpMessage, commandWrongSyntax);
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!canExecute(sender, args.length, true))
            return;

        ConfigSettings.reloadConfig(main);
        sender.sendMessage(ConfigSettings.getReloadConfig());
    }
}
