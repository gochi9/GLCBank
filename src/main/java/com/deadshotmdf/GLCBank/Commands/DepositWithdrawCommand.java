package com.deadshotmdf.GLCBank.Commands;

import com.deadshotmdf.GLCBank.ConfigSettings;
import com.deadshotmdf.GLCBank.Managers.BankManager;
import com.deadshotmdf.GLCBank.Objects.Enums.CommandType;
import com.deadshotmdf.GLCBank.Objects.Enums.ModifyType;
import com.deadshotmdf.GLCBank.Utils.BankUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DepositWithdrawCommand extends SubCommand {

    public DepositWithdrawCommand(BankManager bankManager, String permission, CommandType commandType, int argsRequired, String commandHelpMessage, String commandWrongSyntax) {
        super(bankManager, permission, commandType, argsRequired, commandHelpMessage, commandWrongSyntax);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
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
