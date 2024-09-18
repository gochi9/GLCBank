package com.deadshotmdf.GLCBank.Commands;

import com.deadshotmdf.GLCBank.GLCB;
import com.deadshotmdf.GLCBank.Managers.BankManager;
import com.deadshotmdf.GLCBank.Objects.CommandType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SeeBalanceSelf extends SubCommand{

    public SeeBalanceSelf(GLCB main, BankManager bankManager, String permission, CommandType commandType) {
        super(main, bankManager, permission, commandType);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(canExecute(sender))
            sender.sendMessage(bankManager.getPlayerBank(((Player) sender).getUniqueId()).getAmount()+"");
    }
}
