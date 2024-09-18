package com.deadshotmdf.GLCBank.Commands;

import com.deadshotmdf.GLCBank.GLCB;
import com.deadshotmdf.GLCBank.Managers.BankManager;
import com.deadshotmdf.GLCBank.Objects.CommandType;
import com.deadshotmdf.GLCBank.Objects.ModifyType;
import com.deadshotmdf.GLCBank.Utils.BankUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class DepositWithdrawForPlayerAsConsole extends SubCommand{

    public DepositWithdrawForPlayerAsConsole(GLCB main, BankManager bankManager, String permission, CommandType commandType) {
        super(main, bankManager, permission, commandType);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!canExecute(sender))
            return;

        if(args.length < 4){
            //msg
            return;
        }

        ModifyType modifyType = ModifyType.getModifyType(args[3]);

        if(modifyType == null || modifyType == ModifyType.SET){
            //msg
            return;
        }

        UUID target = bankManager.getOfflineUUID(args[1]);

        if(target == null){
            //msg
            return;
        }

        Double amount = BankUtils.getDouble(args[2]);

        if(amount == null){
            //msg
            return;
        }

        bankManager.modifyBank(Bukkit.getOfflinePlayer(target), amount, modifyType);
    }
}
