package com.deadshotmdf.GLCBank.Commands;

import com.deadshotmdf.GLCBank.ConfigSettings;
import com.deadshotmdf.GLCBank.Managers.BankManager;
import com.deadshotmdf.GLCBank.Objects.Enums.CommandType;
import com.deadshotmdf.GLCBank.Objects.Enums.ModifyType;
import com.deadshotmdf.GLCBank.Utils.BankUtils;
import com.deadshotmdf.GLC_GUIS.Mayor.Enums.UpgradeType;
import com.deadshotmdf.GLC_GUIS.Mayor.MayorManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DepositWithdrawCommand extends SubCommand {

    private final MayorManager mayorManager;

    public DepositWithdrawCommand(BankManager bankManager, MayorManager mayorManager, String permission, CommandType commandType, int argsRequired, String commandHelpMessage, String commandWrongSyntax) {
        super(bankManager, permission, commandType, argsRequired, commandHelpMessage, commandWrongSyntax);
        this.mayorManager = mayorManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(mayorManager.getPlayerUpgrade(((Player)sender).getUniqueId(), UpgradeType.UNLOCK_BANK) < 1){
            sender.sendMessage(com.deadshotmdf.GLC_GUIS.ConfigSettings.getCannotOpenBank());
            return;
        }

        ModifyType type = ModifyType.getModifyType(args[0]);

        if(type == null || type == ModifyType.SET){
            sender.sendMessage(ConfigSettings.getInvalidTransactionType());
            return;
        }

        Double amount = BankUtils.getDouble(args[1]);

        if(amount == null){
            sender.sendMessage(ConfigSettings.getInvalidAmount());
            return;
        }

        double finalAmount = bankManager.modifyBank(((Player)sender), amount, type);
        sender.sendMessage(type == ModifyType.ADD ? ConfigSettings.getDepositSuccessMessage(finalAmount) : ConfigSettings.getWithdrawSuccessMessage(finalAmount));
    }
}
