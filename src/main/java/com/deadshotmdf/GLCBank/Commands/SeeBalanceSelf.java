package com.deadshotmdf.GLCBank.Commands;

import com.deadshotmdf.GLCBank.ConfigSettings;
import com.deadshotmdf.GLCBank.Managers.BankManager;
import com.deadshotmdf.GLCBank.Objects.Enums.CommandType;
import com.deadshotmdf.GLC_GUIS.Mayor.Enums.UpgradeType;
import com.deadshotmdf.GLC_GUIS.Mayor.MayorManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SeeBalanceSelf extends SubCommand{

    private final MayorManager mayorManager;

    public SeeBalanceSelf(BankManager bankManager, MayorManager mayorManager, String permission, CommandType commandType, int argsRequired, String commandHelpMessage, String commandWrongSyntax) {
        super(bankManager, permission, commandType, argsRequired, commandHelpMessage, commandWrongSyntax);
        this.mayorManager = mayorManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(mayorManager.getPlayerUpgrade(((Player)sender).getUniqueId(), UpgradeType.UNLOCK_BANK) < 1){
            sender.sendMessage(com.deadshotmdf.GLC_GUIS.ConfigSettings.getCannotOpenBank());
            return;
        }
        sender.sendMessage(ConfigSettings.getPlayerBalance(sender.getName(), bankManager.getPlayerBank(((Player)sender).getUniqueId()).getAmount()));
    }
}
