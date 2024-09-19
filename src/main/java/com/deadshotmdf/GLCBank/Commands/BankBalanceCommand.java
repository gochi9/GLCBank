package com.deadshotmdf.GLCBank.Commands;

import com.deadshotmdf.GLCBank.GLCB;
import com.deadshotmdf.GLCBank.Managers.BankManager;
import com.deadshotmdf.GLCBank.Objects.CommandType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class BankBalanceCommand implements CommandExecutor, TabCompleter {

    private final static List<String> commands = List.of("balance", "peek", "topself", "top", "deposit", "withdraw", "reload");
    public final static List<String> EMPTY = Collections.emptyList();

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

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0)
            return EMPTY;

        if(args.length > 1){
            SubCommand subCommand = subCommands.get(args[0]);
            return subCommand != null && subCommand.canExecute(sender, false) ? subCommand.tabCompleter(sender, args) : EMPTY;
        }

        List<String> allowedCommands = new LinkedList<>();
        subCommands.forEach((k, v) -> {
            if(v.canExecute(sender, false))
                allowedCommands.add(k);
        });

        return allowedCommands;
    }
}
