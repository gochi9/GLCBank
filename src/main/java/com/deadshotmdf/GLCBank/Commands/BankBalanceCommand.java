package com.deadshotmdf.GLCBank.Commands;

import com.deadshotmdf.GLCBank.GLCB;
import com.deadshotmdf.GLCBank.Managers.BankManager;
import com.deadshotmdf.GLCBank.Objects.CommandType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.List;

public class BankBalanceCommand implements CommandExecutor {

    private final static List<String> commands = List.of("balance", "peek", "topself", "top", "deposit", "withdraw", "reload");

    private final HashMap<String, SubCommand> subCommands;

    public BankBalanceCommand(GLCB main, BankManager bankManager) {
        this.subCommands = new HashMap<>();
        this.subCommands.put("balance", new SeeBalanceSelf(main, bankManager, "glcbank.balance", CommandType.PLAYER));
        this.subCommands.put("peek", new PeekBalance(main, bankManager, "glcbank.peek", CommandType.BOTH));
        this.subCommands.put("consoledepositwithdraw", new DepositWithdrawForPlayerAsConsole(main, bankManager, "glcbank.peek", CommandType.CONSOLE));
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1){
            //msg
            return true;
        }

        SubCommand subCommand = subCommands.get(args[0]);

        if(subCommand == null){
            //msg
            return true;
        }

        subCommand.execute(sender, args);
        return true;
    }

}
