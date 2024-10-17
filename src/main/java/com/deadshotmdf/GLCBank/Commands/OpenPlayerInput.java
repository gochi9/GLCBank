package com.deadshotmdf.GLCBank.Commands;

import com.deadshotmdf.GLCBank.ConfigSettings;
import com.deadshotmdf.GLCBank.Managers.BankManager;
import com.deadshotmdf.GLCBank.Objects.Enums.CommandType;
import com.deadshotmdf.GLCBank.Objects.Enums.ModifyType;
import com.deadshotmdf.GLC_GUIS.Mayor.Enums.UpgradeType;
import com.deadshotmdf.GLC_GUIS.Mayor.MayorManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class OpenPlayerInput extends SubCommand{

    private final MayorManager mayorManager;

    public OpenPlayerInput(BankManager bankManager, MayorManager mayorManager, String permission, CommandType commandType, int argsRequired, String commandHelpMessage, String commandWrongSyntax) {
        super(bankManager, permission, commandType, argsRequired, commandHelpMessage, commandWrongSyntax);
        this.mayorManager = mayorManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
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

        if(mayorManager.getPlayerUpgrade(target, UpgradeType.UNLOCK_BANK) < 1){
            sender.sendMessage(com.deadshotmdf.GLC_GUIS.ConfigSettings.getCannotOpenBankTarget(args[1]));
            player.sendMessage(com.deadshotmdf.GLC_GUIS.ConfigSettings.getCannotOpenBank());
            return;
        }

        bankManager.openPlayerInputSign(player, modifyType);
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
